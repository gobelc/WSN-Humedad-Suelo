/**
 * BaseSuelosC exercises the basic networking layers, collection and
 * dissemination. The application samples DemoSensorC at a basic rate
 * and sends packets up a collection tree. The rate is configurable
 * through dissemination.
 *
 * See TEP118: Dissemination, TEP 119: Collection, and TEP 123: The
 * Collection Tree Protocol for details.
 * 
 * @author Philip Levis
 * @version $Revision: 1.7 $ $Date: 2009/09/16 00:51:50 $
 */
#include "Formato_Paquetes.h"
#include "Ctp.h"
#include <Timer.h>

configuration BaseSuelosAppC {}
implementation {
  components BaseSuelosC, MainC, LedsC, ActiveMessageC;
  components CollectionC as Collector;
  components new CollectionSenderC(AM_DATOS_MSG);
  components new TimerMilliC();
  
  components new VoltageC();
  components new S10hsC();
  components new SerialAMSenderC(AM_DATOS_MSG);
  components SerialActiveMessageC;
#ifndef NO_DEBUG
  components new SerialAMSenderC(AM_COLLECTION_DEBUG) as UARTSender;
  components UARTDebugSenderP as DebugSender;
#endif
  components RandomC;
  components new QueueC(message_t*, 12);
  components new PoolC(message_t, 12);


  BaseSuelosC.Boot -> MainC;
  BaseSuelosC.RadioControl -> ActiveMessageC;
  BaseSuelosC.SerialControl -> SerialActiveMessageC;
  BaseSuelosC.RoutingControl -> Collector;
  BaseSuelosC.Leds -> LedsC;
  BaseSuelosC.Timer -> TimerMilliC;
  BaseSuelosC.TimerHora->TimerMilliC;
  BaseSuelosC.ReadSensor -> S10hsC;
  BaseSuelosC.RootControl -> Collector;
  BaseSuelosC.Receive -> Collector.Receive[AM_DATOS_MSG];

// Usando Dissemination //////////////////////////////////////////////////
  components DisseminationC;
  components new DisseminatorC(envio_solicitud_msg_t, CLAVE_PARA_DISEMINAR);
  BaseSuelosC.DisseminationControl -> DisseminationC;
  BaseSuelosC.RequestUpdate -> DisseminatorC;
  components new SerialAMReceiverC(AM_ENVIO_SOLICITUD_MSG) as SerialRequestReceiver;
  BaseSuelosC.SerialReceive -> SerialRequestReceiver;
///////////////////////////////////////////////////////////////////////////



  BaseSuelosC.UARTSend -> SerialAMSenderC.AMSend;
  BaseSuelosC.CollectionPacket -> Collector;
  BaseSuelosC.CtpInfo -> Collector;
  BaseSuelosC.CtpCongestion -> Collector;
  BaseSuelosC.Random -> RandomC;
  BaseSuelosC.Pool -> PoolC;
  BaseSuelosC.Queue -> QueueC;
  BaseSuelosC.RadioPacket -> ActiveMessageC;
  BaseSuelosC.ReadVoltage->VoltageC;
  BaseSuelosC.WaitTimer -> TimerMilliC;
  
#ifndef NO_DEBUG
  components new PoolC(message_t, 10) as DebugMessagePool;
  components new QueueC(message_t*, 10) as DebugSendQueue;
  DebugSender.Boot -> MainC;
  DebugSender.UARTSend -> UARTSender;
  DebugSender.MessagePool -> DebugMessagePool;
  DebugSender.SendQueue -> DebugSendQueue;
  Collector.CollectionDebug -> DebugSender;
  BaseSuelosC.CollectionDebug -> DebugSender;
#endif
  BaseSuelosC.AMPacket -> ActiveMessageC;
}
