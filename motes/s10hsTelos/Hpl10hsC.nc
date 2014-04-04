/**
 * @version Version 1.0
 * @Date 2009/05/31
 * @author mazzara
*/
  // Low-level, platform-specific glue-code 
  // to access the 10hs sensor
  // on telos-family motes - here  the HPL just provides resource management


  configuration Hpl10hsC {
    provides interface GeneralIO as PowerPin;
  }

  implementation {
    // Pins used to access the 10hs
    components HplMsp430GeneralIOC;     
    components new Msp430GpioC() as Reg5PWRPIN;
    Reg5PWRPIN -> HplMsp430GeneralIOC.Port61;  
    PowerPin = Reg5PWRPIN;
  }

