/**
 * TestNetworkC exercises the basic networking layers, collection and
 * dissemination. The application samples DemoSensorC at a basic rate
 * and sends packets up a collection tree. The rate is configurable
 * through dissemination. The default send rate is every 10s.
 *
 * See TEP118: Dissemination and TEP 119: Collection for details.
 * 
 * @author Philip Levis
 * @version $Revision: 1.11 $ $Date: 2010/01/14 21:53:58 $
 */

#include "Timer.h"
#include "Formato_Paquetes.h"
#include "CtpDebugMsg.h"

module BaseSuelosC {
  uses interface Boot;
  uses interface SplitControl as RadioControl;
  uses interface SplitControl as SerialControl;
  uses interface StdControl as RoutingControl;

  uses interface StdControl as DisseminationControl;
  uses interface DisseminationUpdate<envio_solicitud_msg_t>  			// for the root
		as RequestUpdate;
  
 // uses interface Send;
  uses interface LocalTime<TMilli>;
  uses interface Read<uint16_t> as ReadVoltage;
  uses interface Leds;
  uses interface Read<uint16_t> as ReadSensor;
  uses interface Timer<TMilli>;
  uses interface Timer<TMilli> as WaitTimer;
  uses interface Timer<TMilli> as TimerHora;
  

  uses interface RootControl;
  uses interface Receive as Receive;
  // Agregamos recepcion serie /////////////
  uses interface Receive as SerialReceive;
//////////////////////////////////////////
  
  uses interface AMSend as UARTSend;
  uses interface CollectionPacket;
  uses interface CtpInfo;
  uses interface CtpCongestion;
  uses interface Random;
  uses interface Queue<message_t*>;
  uses interface Pool<message_t>;
  #ifndef NO_DEBUG
  uses interface CollectionDebug;
  #endif
  uses interface AMPacket;
  uses interface Packet as RadioPacket;
  
}
implementation{
  task void uartEchoTask();
  task void serialSendTask();
  
  message_t packet;
  message_t packet2;
  message_t uartpacket;
  message_t* recvPtr = &uartpacket;
  uint8_t msglen;
  bool sendBusy = FALSE;
  bool uartbusy = FALSE;
  bool firstTimer = TRUE;
  bool readCondition = TRUE;
  
  
  
  uint16_t seqno;
  uint32_t hora;
  uint8_t sensor;
 
  uint16_t samplingPeriod = 10*DEFAULT_SAMPLING_PERIOD;
  uint16_t alpha = 40;
  uint16_t max_num_nodes = 30;
  uint16_t waitT;
  uint16_t thresholdMax;
  uint16_t thresholdMin;
  uint16_t thresholdMax;
  uint16_t thresholdMin;
  bool modeAuto, sleeping, root=FALSE;
  uint16_t humedad_suelo;
  uint16_t bateria;
  uint16_t temp_interna;
  uint8_t readMode = 1;
  datos_msg_t msgLocal;
  
  
  
  
  enum {
    SEND_INTERVAL = 8192
  };
  
  void fillPacket();

  event void ReadSensor.readDone(error_t err, uint16_t val) { 
  if (err == SUCCESS ) msgLocal.data = val; 
  call Leds.led1Toggle();
  }  
  
  event void ReadVoltage.readDone(error_t err, uint16_t val2) { 
   void* out;
      if (err!= SUCCESS) {
        val2 = 0xffff;
        call Leds.led0On();
      }
      msgLocal.carga = val2;//*3/4096; 
      fillPacket();
      if (!call Pool.empty() && call Queue.size() < call Queue.maxSize()) {
        message_t* uartmsg = call Pool.get();
	out = (message_t*)call RadioPacket.getPayload(uartmsg, sizeof(datos_msg_t));			
	memcpy(out, &msgLocal, sizeof(datos_msg_t));
        call Queue.enqueue(uartmsg);
        if (!uartbusy) post uartEchoTask();
      }
  
  
  
  
  }  
  
  /*
		The quality of the link with the parent and the parent Id
		are filled here.
	*/
	
	void fillPacket() {
    		uint16_t metric;
    		am_addr_t parent = 1;
    		call CtpInfo.getParent(&parent);
    		call CtpInfo.getEtx(&metric);
		msgLocal.metric = metric;
		msgLocal.parent = parent;
	} 
  
  static void reportProblem() { call Leds.led0Toggle(); } // if there is any problem
  void processRequest(envio_solicitud_msg_t *newRequest);
  
  event void Boot.booted() {
  	msgLocal.source = TOS_NODE_ID;
    call SerialControl.start();
	hora=0;
	
	
  }
  event void SerialControl.startDone(error_t err) {
    call RadioControl.start();
  }
  event void RadioControl.startDone(error_t err) {
    if (err != SUCCESS) {
      call RadioControl.start();
    }
    else {

      call DisseminationControl.start();

      call RoutingControl.start();
     
	call RootControl.setRoot();
     
      seqno = 0;
        call Timer.startOneShot(call Random.rand16() & 0x1ff);
    }
  }



  event void WaitTimer.fired() {
		  call Timer.startPeriodic(samplingPeriod); 
  }

  event void Timer.fired() {
	  uint32_t nextInt;
	  dbg("TestNetworkC", "TestNetworkC: Timer fired.\n");
	  nextInt = call Random.rand32() % SEND_INTERVAL;
	  nextInt += SEND_INTERVAL >> 1;
	  call Timer.startOneShot(nextInt);
	  call ReadVoltage.read();
  }



  event void RadioControl.stopDone(error_t err) {}
  event void SerialControl.stopDone(error_t err) {}	
 
  event void TimerHora.fired(){
	  hora=hora+1000;
  }


 

  void failedSend() {
    dbg("App", "%s: Send failed.\n", __FUNCTION__);
    #ifndef NO_DEBUG
    call CollectionDebug.logEvent(NET_C_DBG_1);
    #endif
  }
 
 
#warning "usando Dissemination por paquetes"
	/* 
		ROOT : Cuando recibe un pedido por el puerto serie, 
		lo difundimos llamando el comando change 
		luego lo ejecutamos.
	*/
	
  	event message_t *SerialReceive.receive(message_t* msg, void* payload, uint8_t len) {
		envio_solicitud_msg_t *newRequest = payload;
		if (len == sizeof(envio_solicitud_msg_t)) {
			call RequestUpdate.change(newRequest);
			
			processRequest(newRequest); // si es que la base procesa algo
		}
		return msg;
  	}
  	
  uint8_t prevSeq = 0;
  uint8_t firstMsg = 0;
  	
  	
 /***********************************************************************\
	*		Funcion que se procesa en cada nuevo pedido		*
	\***********************************************************************/
	void processRequest(envio_solicitud_msg_t *newRequest) {
		if (newRequest->targetId == TOS_NODE_ID || newRequest->targetId == 0xFFFF) {
			switch(newRequest->motivo) {
			case SET_MODE_AUTO_REQUEST:
				modeAuto = TRUE;
				call Timer.stop();
				waitT = (1+TOS_NODE_ID%MAX_NUM_NODES)*alpha;		
				call WaitTimer.startOneShot(waitT);
				break;
			case SET_MODE_QUERY_REQUEST:
				modeAuto = FALSE;
				call Timer.stop();
				break;
			case SET_PERIOD_REQUEST:
				samplingPeriod = newRequest->parameters;
				call Timer.stop();
				alpha = samplingPeriod/(max_num_nodes+1);
				waitT = (1+TOS_NODE_ID)*alpha;
				if(sleeping == FALSE)
					call WaitTimer.startOneShot(waitT);   
				break;
			case SET_THRESHOLD_MAX_REQUEST:
				thresholdMax = newRequest->parameters;
				break;
			case SET_THRESHOLD_MIN_REQUEST:
				thresholdMin = newRequest->parameters;
				break;
			case SET_TIME:
				hora = newRequest->parametersLong;
				call TimerHora.startPeriodic(1000);
				msgLocal.hora=hora;
				fillPacket();
				SEND_TASK;
				break;	
				
			case GET_PERIOD_REQUEST:		// we send the period value
				if(sleeping == FALSE) {
					msgLocal.reply = PERIOD_REPLY;
					msgLocal.data = samplingPeriod;
					fillPacket();
					
					post serialSendTask();
					
				}
				break;
				case GET_THRESHOLD_MAX_REQUEST:		// we send the threshold value
				if(sleeping == FALSE) {
					msgLocal.reply = THRESHOLD_MAX_REPLY;
					msgLocal.data = thresholdMax;
					fillPacket();
					SEND_TASK;
				}
				break;
				case GET_THRESHOLD_MIN_REQUEST:		// we send the threshold value
				if(sleeping == FALSE) {
					msgLocal.reply = THRESHOLD_MIN_REPLY;
					msgLocal.data = thresholdMin;
					fillPacket();
					SEND_TASK;
				}
				break;	
			case GET_READING_REQUEST:		// we send the sensor value (request-driven mode)
				readMode = newRequest->parameters;
				readCondition = FALSE;
				
				if ((readMode & 15) == 1)			
					call ReadSensor.read();
				if ((readMode & 15) == 2)			
					call ReadVoltage.read();
				if ((readMode & 15) == 3)			
					;//call TemperaturaInterna.read();
				
				break;
		#ifdef DUTY_CYCLE
			case SLEEP_REQUEST:
				if(!root) {					// the gateway do NOT sleep
					sleeping = TRUE;
					setLocalDutyCycle();
					call Timer.stop();
				}
				break;
			case WAKE_UP_REQUEST:
				if(!root) {			// the gateway do NOT sleep
					sleeping = FALSE;
					setLocalDutyCycle();
					if (modeAuto)
						call Timer.startPeriodic(samplingPeriod);
				}
				break;
			case SET_SLEEP_DUTY_CYCLE_REQUEST:
				sleepDutyCycle = newRequest->parameters;
				setLocalDutyCycle();
				break;
			case SET_AWAKE_DUTY_CYCLE_REQUEST:
				awakeDutyCycle = newRequest->parameters;
				setLocalDutyCycle();
				break;
			case GET_AWAKE_DUTY_CYCLE_REQUEST:
				msgLocal.reply = AWAKE_DUTY_CYCLE_REPLY;
				msgLocal.data = awakeDutyCycle;
				fillPacket();
				post serialSendTask();
				
				//SEND_TASK
				break;
		#endif
			}
		}
	}
 
  	
  



  
#ifdef USE_DISS  
  event void DisseminationPeriod.changed() {
    const uint32_t* newVal = call DisseminationPeriod.get();
    call Timer.stop();
    call Timer.startPeriodic(*newVal);
  }
#endif

  

  event message_t* 
  Receive.receive(message_t* msg, void* payload, uint8_t len) {
    datos_msg_t* tnmsg = payload;
    void* out; 
   
    dbg("TestNetworkC", "Received packet at %s from node %hhu.\n", sim_time_string(), call CollectionPacket.getOrigin(msg));
    call Leds.led1Toggle();

    if (call CollectionPacket.getOrigin(msg) == 0) {
      if (firstMsg == 1) {
	if (call CollectionPacket.getSequenceNumber(msg) - prevSeq > 1) {
	  call Leds.led2On();
	}
      } else {
	firstMsg = 1;
      }
      prevSeq = call CollectionPacket.getSequenceNumber(msg);
    }

    if (!call Pool.empty() && call Queue.size() < call Queue.maxSize()) {
      message_t* tmp = call Pool.get();
      if (tmp == NULL) {
		  // drop the message on the floor if we run out of queue space.
   	      return msg;
	  }
	  out = (message_t*)call RadioPacket.getPayload(msg, sizeof(datos_msg_t));			
	  memcpy(out, tnmsg, sizeof(datos_msg_t));
      //call AMPacket.setType(msg, AM_DATOS_MSG);
      call Queue.enqueue(msg);
      if (!uartbusy) {
        post uartEchoTask();
      }
	  return tmp;
    }

    return msg;
 }
 
 // Envia inmediatamente por la uart
 task void serialSendTask() {

//		if (!uartbusy) {
//			datos_msg_t * o = (datos_msg_t *)call UARTSend.getPayload(&uartpacket, sizeof(datos_msg_t));
//			memcpy(o, &msgLocal, sizeof(datos_msg_t));
//			if (call UARTSend.send(0xffff, &uartpacket, sizeof(datos_msg_t)) == SUCCESS)
//				uartbusy = TRUE;
//			else
//				reportProblem();
//		}

 }
 

 


 task void uartEchoTask() {
    dbg("Traffic", "Sending packet to UART.\n");
   if (call Queue.empty()) {
     return;
   }
   else if (!uartbusy) {
     message_t* msg = call Queue.dequeue();
     //call AMPacket.setType(msg, AM_DATOS_MSG);
     //call SerialPacket.setPayloadLength(msg, sizeof(datos_msg_t));
     dbg("Traffic", "Sending packet to UART.\n");
     if (call UARTSend.send(0xffff, msg, sizeof(datos_msg_t) /*call RadioPacket.payloadLength(msg)*/) == SUCCESS) {
       uartbusy = TRUE;
     }
     else {
     #ifndef NO_DEBUG
      call CollectionDebug.logEventMsg(NET_C_DBG_2,
				       call CollectionPacket.getSequenceNumber(msg),
				       call CollectionPacket.getOrigin(msg),
				       call AMPacket.destination(msg));
	 #endif
     }
   }
 }

  event void UARTSend.sendDone(message_t *msg, error_t error) {
    dbg("Traffic", "UART send done.\n");
    uartbusy = FALSE;
    call Pool.put(msg);
    if (!call Queue.empty()) {
      post uartEchoTask();
    } 
    else {
      //        call CtpCongestion.setClientCongested(FALSE);
    }
  }

  /* Default implementations for CollectionDebug calls.
   * These allow CollectionDebug not to be wired to anything if debugging
   * is not desired. */
#ifndef NO_DEBUG
    default command error_t CollectionDebug.logEvent(uint8_t type) {
        return SUCCESS;
    }
    default command error_t CollectionDebug.logEventSimple(uint8_t type, uint16_t arg) {
        return SUCCESS;
    }
    default command error_t CollectionDebug.logEventDbg(uint8_t type, uint16_t arg1, uint16_t arg2, uint16_t arg3) {
        return SUCCESS;
    }
    default command error_t CollectionDebug.logEventMsg(uint8_t type, uint16_t msg, am_addr_t origin, am_addr_t node) {
        return SUCCESS;
    }
    default command error_t CollectionDebug.logEventRoute(uint8_t type, am_addr_t parent, uint8_t hopcount, uint16_t metric) {
        return SUCCESS;
    }
    #endif
 
}
