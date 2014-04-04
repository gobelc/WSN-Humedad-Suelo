/**
 * @version Version 1.0
 * @Date 2009/05/31
 * @author mazzara
*/

/** Interconexion del modulo que se encarga de tomar una medida del sensor comercial Decagon 10HS
*/

generic configuration S10hsC()
{
  provides interface DeviceMetadata;
  provides interface Read<uint16_t>;
  provides interface ReadStream<uint16_t>;
}
implementation
{
  components new TimerMilliC() as TimerEstable;

  components new AdcReadClientC();
  //Read = AdcReadClientC;

  components new AdcReadStreamClientC();
  ReadStream = AdcReadStreamClientC;

  components S10hsP;
  DeviceMetadata = S10hsP;
  AdcReadClientC.AdcConfigure -> S10hsP;
  AdcReadStreamClientC.AdcConfigure -> S10hsP;

  components S10hslogicP;
  Read = S10hslogicP.ReadHume;
  S10hslogicP.Read -> AdcReadClientC;
  S10hslogicP.TimerEstable -> TimerEstable;

  components Hpl10hsC;
  S10hslogicP.PowerPin -> Hpl10hsC.PowerPin;

}

