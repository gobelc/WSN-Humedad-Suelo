#ifndef FORMATO_PAQUTES_H
#define FORMATO_PAQUTES_H

#include "AM.h"
#define SEND_TASK post serialSendTask();

enum {
	AM_DATOS_MSG = 0xec,
	AM_ENVIO_SOLICITUD_MSG = 0xed,
	AM_BROADCAST_MSG = 0xee,
	SET_MODE_AUTO_REQUEST = 1,
	SET_MODE_QUERY_REQUEST = 2,
	SET_PERIOD_REQUEST = 3,
	SET_THRESHOLD_MAX_REQUEST = 4,
	SET_THRESHOLD_MIN_REQUEST = 40,
	GET_STATUS_REQUEST = 5,
	SLEEP_REQUEST = 6,
	WAKE_UP_REQUEST = 7,
	GET_PERIOD_REQUEST = 8,
	GET_THRESHOLD_MAX_REQUEST = 9,
	GET_THRESHOLD_MIN_REQUEST = 90,
	GET_READING_REQUEST = 10,
	READING_REPLY=100,
	NO_REPLY = 0x00,
	BATTERY_AND_MODE_REPLY = 0x20,
	PERIOD_REPLY = 0x40,
	THRESHOLD_MAX_REPLY = 0x60,
	THRESHOLD_MIN_REPLY = 0x61,
	DEFAULT_SAMPLING_PERIOD = 60000,
	MAX_NUM_NODES = 41,
	CLAVE_PARA_DISEMINAR = 55,
	SENSOR_10HS = 127,
	
	
	//BATERIA
	GET_READING_BATTERY_REQUEST=20,
    READING_BATTERY_REPLY=102,
    
    //METRICA
    GET_METRIC_REQUEST=21,
    METRIC_REPLY=103,
    
    //TIEMPO
	SET_TIME=104,
	SET_TIME_REPLY=105,
	
};

typedef nx_struct envio_solicitud_msg {
	nx_am_addr_t targetId;
	nx_uint8_t motivo;
	nx_uint16_t parameters;
	nx_uint32_t parametersLong; // Puede ser para programar la hora
} envio_solicitud_msg_t;


typedef nx_struct datos_msg {
	nx_am_addr_t source; /* Mote id of sending mote. */
	nx_uint16_t seqno; /* nro de secuencia */
	nx_uint16_t data;
	nx_uint32_t hora;
	nx_uint16_t carga;
	nx_uint8_t sensor;
	nx_uint16_t metric;
	nx_am_addr_t parent;
	nx_uint8_t reply;
} datos_msg_t;



#endif
