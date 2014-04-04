/**
 * @version Version 1.0
 * @Date 2009/05/31
 * @author mazzara
*/

/** Modulo para sensor 10HS en Telos
 *  Configuracion ADC0:  U2 pin 3
*/

#include "Timer.h"
#include "Msp430Adc12.h"


module S10hsP @safe()
{

  provides interface DeviceMetadata;
  provides interface AdcConfigure<const msp430adc12_channel_config_t*>;

}

implementation
{

  msp430adc12_channel_config_t config = {
    inch: INPUT_CHANNEL_A0,  // U2 pin 3
    sref: REFERENCE_VREFplus_AVss,
    ref2_5v: REFVOLT_LEVEL_1_5,
    adc12ssel: SHT_SOURCE_SMCLK,
    adc12div: SHT_CLOCK_DIV_1,
    sht: SAMPLE_HOLD_256_CYCLES,
    sampcon_ssel: SAMPCON_SOURCE_SMCLK,
    sampcon_id: SAMPCON_CLOCK_DIV_1
  };
/*
  const msp430adc12_channel_config_t configADC = {
    INPUT_CHANNEL_A7, REFERENCE_VREFplus_AVss, REFVOLT_LEVEL_2_5,
    SHT_SOURCE_SMCLK, SHT_CLOCK_DIV_1, SAMPLE_HOLD_256_CYCLES,
    SAMPCON_SOURCE_SMCLK, SAMPCON_CLOCK_DIV_1 
   };
*/
  command uint8_t DeviceMetadata.getSignificantBits() { return 12; }

  async command const msp430adc12_channel_config_t* AdcConfigure.getConfiguration() {
    return &config;
  }

}

