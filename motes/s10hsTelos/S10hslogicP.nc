/**
 * @version Version 1.0
 * @Date 2009/05/31
 * @author mazzara
*/

#include "S10hs_const.h"

module S10hslogicP @safe()
{
  provides interface Read<uint16_t> as ReadHume;
  uses interface Read<uint16_t>;
  uses interface Timer<TMilli> as TimerEstable;
  uses interface GeneralIO as PowerPin;
 }

implementation{

  uint8_t syh=0;
  uint32_t medida=0;

  event void TimerEstable.fired() {
        medida = 0;
 	call Read.read();
#warning "usando sensor 10HS"

  }
 command error_t ReadHume.read() {
       call PowerPin.clr(); // power-on sensor
       call TimerEstable.startOneShot(ESPERO_ESTABLE);
       return SUCCESS;  
 }
 event void Read.readDone(error_t result, uint16_t data) {
    if (syh==MEDIDAS)
    {
      call PowerPin.set(); // power-off
      syh=0;
      medida>>=MEDIDAS_SHIFT;
      signal ReadHume.readDone(SUCCESS,medida);
    }
    else
    {
      syh++;
      medida=medida+data;
      call Read.read();
    }  

}
}

