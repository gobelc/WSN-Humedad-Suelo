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



module SensorRemotoC {
  uses interface Boot;
  uses interface SplitControl as RadioControl;
  uses interface StdControl as RoutingControl;

//Usando Dissemination
  uses interface StdControl as DisseminationControl;
  uses interface DisseminationValue<envio_solicitud_msg_t> as RequestValue;

  uses interface Send;
  uses interface LocalTime<TMilli>;
  uses interface Read<uint16_t> as ReadVoltage;
  uses interface Leds;
  uses interface Read<uint16_t> as ReadSensor;
  uses interface Timer<TMilli>;
  uses interface Timer<TMilli> as WaitTimer;
  uses interface Timer<TMilli> as TimerHora;
  
 
  uses interface RootControl;
  uses interface CollectionPacket;
  uses interface CtpInfo;
  uses interface CtpCongestion;
  uses interface Random;
  uses interface AMPacket;
  uses interface Packet as RadioPacket;
  
}
implementation{

  task void collectSendTask();
  task void incrementarHora();
  void fillPacket();
  void setLocalDutyCycle();
  
  message_t packet;
  message_t uartpacket;
  message_t* recvPtr = &uartpacket;
  uint8_t msglen;
  bool sendBusy = FALSE;
  bool uartbusy = FALSE;
  bool firstTimer = TRUE;
  bool conditionAuto=FALSE;
  bool readCondition = TRUE;
  
  
  uint16_t seqno;
  uint32_t hora=0;
  uint32_t segundos;
  uint8_t sensor;
  uint16_t samplingPeriod = DEFAULT_SAMPLING_PERIOD;
  uint16_t alpha = 40;
  uint16_t max_num_nodes = 30;
  uint16_t waitT;
  uint16_t thresholdMax;
  uint16_t thresholdMin;
  uint16_t thresholdBatMax=4200;
  uint16_t thresholdBatMin=1000;
  
  bool modeAuto, sleeping, root=FALSE;
  uint16_t humedad_suelo;
  uint16_t bateria;
  uint16_t temp_interna;
  uint8_t readMode = 1;
  datos_msg_t msgLocal;
  uint16_t sleepDutyCycle, awakeDutyCycle;
  
  enum {
    SEND_INTERVAL = 8192
  };

  static void reportProblem() { call Leds.led0Toggle(); } // if there is any problem
  void processRequest(envio_solicitud_msg_t *newRequest);


  event void ReadSensor.readDone(error_t err, uint16_t val) { 
  if (err == SUCCESS ) msgLocal.data = val; 
  call Leds.led1Toggle();
  }  
  
  event void ReadVoltage.readDone(error_t err, uint16_t val2) { 
  if (err == SUCCESS ) msgLocal.carga = val2;
  }  

  event void Boot.booted() {
	  msgLocal.source = TOS_NODE_ID;
	  msgLocal.sensor = SENSOR_10HS;
	  call TimerHora.startPeriodic(1000);
	  modeAuto = TRUE;
	  msgLocal.reply=READING_REPLY;
	  call RadioControl.start();
	
  }


  event void RadioControl.startDone(error_t err) {
    if (err != SUCCESS) {
      call RadioControl.start();
    }
    else {
      call DisseminationControl.start();
      call RoutingControl.start();
      if (TOS_NODE_ID % 500 == 0) {
	call RootControl.setRoot();
      }
      seqno = 0;
        call Timer.startOneShot(call Random.rand16() & 0x1ff);
    }
  }

    event void RadioControl.stopDone(error_t err)   {
  	}
	
	event void TimerHora.fired() {
			post incrementarHora();
	}
	
	

  void failedSend() {
    dbg("App", "%s: Send failed.\n", __FUNCTION__);
        #ifndef NO_DEBUG
    call CollectionDebug.logEvent(NET_C_DBG_1);
        #endif
  }
 
 
	
	void fillPacket() {
    		uint16_t metric;
    		am_addr_t parent = 0;//EStaba en 1
    		call CtpInfo.getParent(&parent);
    		call CtpInfo.getEtx(&metric);
		msgLocal.metric = metric;
		msgLocal.parent = parent;
		msgLocal.hora=hora;
	
	}   


  	
  
   
  void sendMessage() {
    datos_msg_t* msg = (datos_msg_t*)call Send.getPayload(&packet, sizeof(datos_msg_t));
    fillPacket();
	msgLocal.reply=READING_REPLY; 
    
	
    	
    msg->source = TOS_NODE_ID;
    msg->seqno = seqno;
    msg->data =msgLocal.data;
	msg->hora=msgLocal.hora;
    msg->carga=msgLocal.carga;
    msg->sensor=SENSOR_10HS;
    msg->parent = msgLocal.parent;
    msg->metric = msgLocal.metric;
    msg->reply = msgLocal.reply;
    

    if (call Send.send(&packet, sizeof(datos_msg_t)) != SUCCESS) {
      failedSend();
      call Leds.led0On();
      dbg("TestNetworkC", "%s: Transmission failed.\n", __FUNCTION__);
    }
    else {
      sendBusy = TRUE;
      seqno++; 
      dbg("TestNetworkC", "%s: Transmission succeeded.\n", __FUNCTION__);
    }
  }

  /*
		NODO : Cuando recibe un pedido,
		se lo ejecuta.
	*/
	

	event void RequestValue.changed() {
		envio_solicitud_msg_t *newRequest = (envio_solicitud_msg_t *)call RequestValue.get();
		processRequest(newRequest);
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
	  call ReadSensor.read();
	  call ReadVoltage.read();
	  if (!sendBusy){
		  sendMessage();
	  }}

  event void Send.sendDone(message_t* m, error_t err) {
    if (err != SUCCESS) {
     call Leds.led0On();
    }
    sendBusy = FALSE;
    dbg("TestNetworkC", "Send completed.\n");
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
				msgLocal.reply=READING_REPLY;
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
				call Timer.stop();
				alpha = samplingPeriod/(max_num_nodes+1);
				waitT = (1+TOS_NODE_ID)*alpha;
				if(sleeping == FALSE)
					call WaitTimer.startOneShot(waitT);  
				break;
			case SET_THRESHOLD_MIN_REQUEST:
				thresholdMin = newRequest->parameters;
				call Timer.stop();
				alpha = samplingPeriod/(max_num_nodes+1);
				waitT = (1+TOS_NODE_ID)*alpha;
				if(sleeping == FALSE)
					call WaitTimer.startOneShot(waitT);  
				break;	
			case SET_TIME:
				hora = newRequest->parametersLong;
				msgLocal.hora=hora;
				call Timer.stop();
				alpha = samplingPeriod/(max_num_nodes+1);
				waitT = (1+TOS_NODE_ID)*alpha;
				if(sleeping == FALSE)
					call WaitTimer.startOneShot(waitT);  
				call TimerHora.stop();
				call TimerHora.startPeriodic(1000);
				msgLocal.reply=SET_TIME_REPLY;
				fillPacket();
				SEND_TASK;
				break;		
				
			case GET_PERIOD_REQUEST:		// we send the period value
				if(sleeping == FALSE) {
					modeAuto = FALSE;
					msgLocal.reply = PERIOD_REPLY;
					msgLocal.data = samplingPeriod;
					fillPacket();
					SEND_TASK;
				}
				break;
				
				case GET_THRESHOLD_MAX_REQUEST:		// we send the threshold value
				if(sleeping == FALSE) {
					modeAuto = FALSE;
					msgLocal.reply = THRESHOLD_MAX_REPLY;
					msgLocal.data = thresholdMax;
					fillPacket();
					SEND_TASK;
				}
				break;
				
				case GET_THRESHOLD_MIN_REQUEST:		// we send the threshold value
				if(sleeping == FALSE) {
					modeAuto = FALSE;
					msgLocal.reply = THRESHOLD_MIN_REPLY;
					msgLocal.data = thresholdMin;
					fillPacket();
					SEND_TASK;
				}
				break;	
				
				case GET_READING_REQUEST:		// we send the sensor value (request-driven mode)
				modeAuto = FALSE;
				call ReadSensor.read();
				msgLocal.reply=READING_REPLY;
				fillPacket();
				SEND_TASK;
				break;
			
				case GET_READING_BATTERY_REQUEST:
				modeAuto = FALSE;
				call ReadVoltage.read();
				msgLocal.reply=READING_BATTERY_REPLY;
				fillPacket();
				SEND_TASK;
				break;
				
				case GET_METRIC_REQUEST:
				modeAuto = FALSE;
				msgLocal.reply=METRIC_REPLY;
				fillPacket();
				SEND_TASK;
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
				SEND_TASK
				break;
				#endif
			}
		}
	}

	

  task void collectSendTask() {
		if (!sendBusy && !root) {
			datos_msg_t *o = (datos_msg_t *)call Send.getPayload(&packet, sizeof(datos_msg_t));
			memcpy(o, &msgLocal, sizeof(datos_msg_t));
			if (call Send.send(&packet, sizeof(datos_msg_t)) == SUCCESS)
				sendBusy = TRUE;
			else
				reportProblem();
	    }
	}
  
	task void incrementarHora() {
		hora += 1000;
	}
  
 
        #ifdef DUTY_CYCLE
  void setLocalDutyCycle() {
	if (sleeping)
		call LowPowerListening.setLocalDutyCycle(sleepDutyCycle);
	else{
		call LowPowerListening.setLocalDutyCycle(awakeDutyCycle);
	}	
  }
#endif


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
