COMPONENT=SensorRemotoAppC

CFLAGS += -I$(TOSDIR)/lib/net \
          -I$(TOSDIR)/lib/net/drip \
          -I$(TOSDIR)/lib/net/4bitle \
          -I$(TOSDIR)/lib/net/ctp -DNO_DEBUG
CFLAGS += -I./s10hsTelos

TFLAGS += -I$(TOSDIR)/../apps/tests/TestDissemination \
          -I$(TOSDIR)/../support/sdk/c \
          -I$(TOSDIR)/types \
          -I.

BUILD_EXTRA_DEPS = EnvioSolicitudMsg.class DatosMsg.class
MIGFILES = EnvioSolicitudMsg.java EnvioSolicitudMsg.class DatosMsg.java DatosMsg.class 



DatosMsg.class: DatosMsg.java
	javac DatosMsg.java
DatosMsg.java: Formato_Paquetes.h
	mig java -target=$(PLATFORM) $(CFLAGS) -java-classname=DatosMsg Formato_Paquetes.h datos_msg -o $@

	
EnvioSolicitudMsg.class: EnvioSolicitudMsg.java
	javac EnvioSolicitudMsg.java
EnvioSolicitudMsg.java: Formato_Paquetes.h
	mig java -target=$(PLATFORM) $(CFLAGS) -java-classname=EnvioSolicitudMsg Formato_Paquetes.h envio_solicitud_msg -o $@

include $(MAKERULES)
migclean:
	rm -rf $(MIGFILES)
	
