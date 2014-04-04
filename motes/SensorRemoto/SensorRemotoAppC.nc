/**
 * SensorRemotoC exercises the basic networking layers, collection and
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

configuration SensorRemotoAppC {}
implementation {
  components SensorRemotoC, MainC, LedsC, ActiveMessageC, new TimerMilliC() as TimerHora;
  components CollectionC as Collector;
  components new CollectionSenderC(AM_DATOS_MSG);
  components new TimerMilliC();
  
  components new VoltageC();
  components new S10hsC();
  components RandomC;


// Usando Dissemination //////////////////////////////////////////////////
  components DisseminationC;
  components new DisseminatorC(envio_solicitud_msg_t, CLAVE_PARA_DISEMINAR);
  SensorRemotoC.DisseminationControl -> DisseminationC;
  SensorRemotoC.RequestValue -> DisseminatorC;
  
///////////////////////////////////////////////////////////////////////////


  SensorRemotoC.Boot -> MainC;
  SensorRemotoC.RadioControl -> ActiveMessageC;
  SensorRemotoC.RoutingControl -> Collector;
  SensorRemotoC.Leds -> LedsC;
  SensorRemotoC.Timer -> TimerMilliC;
  SensorRemotoC.TimerHora->TimerHora;
  SensorRemotoC.WaitTimer -> TimerMilliC;

  SensorRemotoC.Send -> CollectionSenderC;
  SensorRemotoC.ReadSensor -> S10hsC;
  SensorRemotoC.RootControl -> Collector;
  SensorRemotoC.CollectionPacket -> Collector;
  SensorRemotoC.CtpInfo -> Collector;
  SensorRemotoC.CtpCongestion -> Collector;
  SensorRemotoC.Random -> RandomC;
  SensorRemotoC.RadioPacket -> ActiveMessageC;
  SensorRemotoC.ReadVoltage->VoltageC;
 
  SensorRemotoC.AMPacket -> ActiveMessageC;
}
