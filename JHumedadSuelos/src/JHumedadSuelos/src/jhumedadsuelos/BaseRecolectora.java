package jhumedadsuelos;


//import Util;


import com.csvreader.CsvWriter;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import info.monitorenter.gui.chart.ITrace2D;
import net.tinyos.message.*;
import java.lang.Math;
import java.util.List;
//import net.tinyos.packet.*;

public class BaseRecolectora implements Runnable, MessageListener{
	  private MoteIF moteIF;
	  private CsvWriter csvWriter; //taba private
	  private String [] record;
	  private Date date;
	  private String filename;
	  private long startt;
	  private boolean loggin = false;
	  private ITrace2D trace1;
          private ITrace2D trace2;
          private ITrace2D trace3;
          private int index_node;
          private List lista_sensores;
          public static final int PERIODO_DORMIDO = 100;
          private double bateria_minimo=2150; //en mV
          private JPanelPedidos jpp;
          private MoteDatabase moteDatabase;
          private float thresholdMax;
          private float thresholdMin;
          private boolean ALARM_HUM_MAX=false;
          private boolean ALARM_HUM_MIN=false;
          private boolean ALARM_BATTERY=false;
	    
	   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
	   DecimalFormat formateador = new DecimalFormat("###.####");
	   String line = "";
	   int[] ert = new int[1];
	   BufferedWriter logfileStream;
	   FileWriter file;
	    /*
	    This function starts the logging process and
	    creates a file with the date, and time.
	    */

	    public void startLogging() {
	        date = new Date();
	        filename = date.toString() + ".csv";
	        filename = filename.replaceAll(":", "-");
	        filename = filename.replaceAll(" ", "_");
	        //startTime = date.getTime();
	        startLogging(filename);
                
	    }

	    private void startLogging(String filename) {
	            csvWriter = new CsvWriter(filename);
	            record = new String[12];
	            record[0] = " Nodo ";
	            record[1] = " Sens ";
	            record[2] = " Hora ";
	            record[3] = " Calidad ";
	            record[4] = " Padre ";
	            record[5] = " Respuesta ";
	            record[6] = " Dato ";
	            record[7] = " Nsec ";
                    record[8] = " Bateria ";
                    record[9] = " Alarma humedad alta ";
                    record[10] = " Alarma humedad baja ";
                    record[11] = " Alarma bateria baja ";
                    
                    
                    
	            try {
	                //csvWriter.writeComment("File used to store data gathered by WiseMan");
	                csvWriter.writeRecord(record);
	                csvWriter.flush();
	                jpp.addText("Guardando Datos en " + filename +"\n");
	            } catch (Exception e) {
	                e.printStackTrace();
                        //csvWriter.close();
	            }
	    }


	   
	  public BaseRecolectora(MoteIF enlace, ITrace2D trace1,ITrace2D trace2,ITrace2D trace3,JPanelPedidos jpp, MoteDatabase moteDatabase,int index_node) {
		this.moteIF = enlace;
		this.trace1 = trace1;
                this.trace2 = trace2;
                this.trace3 = trace3;
                this.jpp = jpp;
                this.moteDatabase = moteDatabase;
                this.index_node=index_node;
		startt = System.currentTimeMillis();
		startLogging();
		loggin = true;   
	  }

		/*
		The thread just does a loop, sleeping and incrementing
		a counter with a frequency of 10 Hz (100 ms). The max
		values of the counters are defined to choose the frequency
		of repainting the mapPanel and chartPanel;
	*/
          
         public double calibrarHumedad(double valorMv) {

        double valorm3m3 = 2.97 * Math.pow(10, -9) * Math.pow(valorMv, 3) - 7.37 * Math.pow(10, -6) * Math.pow(valorMv, 2) + 6.69 * Math.pow(10, -3) * valorMv - 1.92;
        return valorm3m3;

    }
          
          
	public void run() {
		moteIF.registerListener(new DatosMsg(), this);
		while(true) {
			try {
				Thread.sleep(PERIODO_DORMIDO);
			} catch (Exception e) { e.printStackTrace();}
		}
  }

	  public void messageReceived(int to, Message message) {
		  long t = System.currentTimeMillis();
                  long dif=1352914676400L; //desfasaje producido por pasar de 64 a 32 bits
                  long hora_int=t;
                  double Humedad=0;
                  Date trec = new Date(t);
                  String hora = sdf.format(trec) ;
  
                  
                 
		  if (message instanceof DatosMsg){
                          DatosMsg dm = (DatosMsg) message;
                          Mote localMote;
                          localMote = moteDatabase.getMote(dm.get_source());
			  //System.out.print("" + hora + ": ");
			  //System.out.println(message);
                          
                          
			  int moteID = dm.get_source();
			  int seqno = dm.get_seqno();
                          double data = dm.get_data();
                          double mem1,mem2,mem3,mem4,mem5,mem6;
			  int tipoSensor = dm.get_sensor();
			  int calidad = dm.get_metric();
			  int padre = dm.get_parent();
			  int reply = dm.get_reply();
                          //System.out.println("respuesta: "+reply);
                          float carga = (float) dm.get_carga()*3/4096;
                          
                          
                          if ((reply==Constants.READING_REPLY) && (moteID!=0)){
                            Humedad=(float) calibrarHumedad(data);
                          }
                          
                          /// HORA
                          
                          hora_int=dm.get_hora();
                          hora_int+=dif;
                          Date trec_sensor = new Date(hora_int);
                          String hora_sensor = sdf.format(trec_sensor) ;
                          
                          if (carga<bateria_minimo/1000){
                               ALARM_BATTERY=true;
                               jpp.addTextAlarm(hora_sensor+" Batería baja en Nodo:"+moteID+"\n");
                              
                          }
                          
                         
                         
                         if (reply==Constants.READING_REPLY && moteID!=0){
                            mem1=data;
                            jpp.addResponse(formateador.format(calibrarHumedad(mem1)));
                         }
                         
                         if (reply==Constants.PERIOD_REPLY && moteID!=0){
                             mem2=data;
                            jpp.addResponse(formateador.format(mem2));
                         }
                          
                          if (reply==Constants.METRIC_REPLY){
                              mem3=calidad;
                              jpp.addResponse(formateador.format(mem3));
                          }
                          
                          if (reply==Constants.READING_BATTERY_REPLY){
                              mem4=carga;
                              jpp.addResponse(formateador.format(mem4));
                          }
                          
                         
                          if (reply==Constants.THRESHOLD_MAX_REPLY){
                          thresholdMax=(float)data/100;
                                  }
                          
                          if (reply==Constants.THRESHOLD_MIN_REPLY){
                          thresholdMin=(float)data/100;
                                  }
                          
                           if (reply==Constants.THRESHOLD_MAX_REPLY){
                              mem5=data;
                              jpp.addResponse(formateador.format(mem5));
                          }
                           
                             if (reply==Constants.THRESHOLD_MIN_REPLY){
                               mem6=data;
                              jpp.addResponse(formateador.format(mem6));
                          }
                          
                         if ((dm.get_sensor() == Constants.SENSOR_10HS) && (moteID == 1) && (reply == 100)) {
                          jpp.addText(hora_sensor + " | Nodo: " + moteID + " | Lectura sensor 10HS: " + (float) Humedad + " m3/m3" + "\n");
                          trace1.setColor(Color.RED);
                          trace1.addPoint(((double) System.currentTimeMillis() - startt) / 1000, Humedad);

                           if ((float)Humedad > thresholdMax) {
                              ALARM_HUM_MAX = true;
                              jpp.addTextAlarm(hora_sensor+" Humedad elevada en Nodo:"+moteID+" Lectura: "+ (float) Humedad + " m3/m3"+"\n");
                          } else {
                              ALARM_HUM_MAX = false;
                          }

                          if ((float)Humedad < thresholdMin) {
                              ALARM_HUM_MIN = true;
                              jpp.addTextAlarm(hora_sensor+" Humedad baja en Nodo:"+moteID+" Lectura: "+ (float) Humedad + " m3/m3"+"\n");
                          } else {
                              ALARM_HUM_MIN = false;
                          }

                      }
                         
                         if ((dm.get_sensor()== Constants.SENSOR_10HS) && (moteID==2) && (reply==100)){
                               jpp.addText(hora_sensor+" | Nodo: " + moteID+ " | Lectura sensor 10HS: " + (float) Humedad + " m3/m3"+"\n");
				trace2.setColor(Color.BLUE);
                                       
				trace2.addPoint(((double) System.currentTimeMillis() - startt)/1000, Humedad);
                          
                               if ((float)Humedad > thresholdMax) {
                              ALARM_HUM_MAX = true;
                              jpp.addTextAlarm(hora_sensor+" Humedad elevada en Nodo:"+moteID+" Lectura: "+ (float) Humedad + " m3/m3"+"\n");
                          } else {
                              ALARM_HUM_MAX = false;
                          }

                          if ((float)Humedad < thresholdMin) {
                              ALARM_HUM_MIN = true;
                              jpp.addTextAlarm(hora_sensor+" Humedad baja en Nodo:"+moteID+" Lectura: "+ (float) Humedad + " m3/m3"+"\n");
                          } else {
                              ALARM_HUM_MIN = false;
                          }
                         
                         
                         }

                         if ((dm.get_sensor()== Constants.SENSOR_10HS) && (index_node==3) && (reply==100)) {
                                jpp.addText(hora_sensor+" | Nodo: " + moteID+ " | Lectura sensor 10HS: " + (float) Humedad + " m3/m3"+"\n");
				trace2.setColor(Color.ORANGE);
				trace2.addPoint(((double) System.currentTimeMillis() - startt)/1000, Humedad);
                          
                         if ((float)Humedad > thresholdMax) {
                              ALARM_HUM_MAX = true;
                              jpp.addTextAlarm(hora_sensor+" Humedad elevada en Nodo:"+moteID+" Lectura: "+ (float) Humedad + " m3/m3"+"\n");
                          } else {
                              ALARM_HUM_MAX = false;
                          }

                          if ((float)Humedad < thresholdMin) {
                              ALARM_HUM_MIN = true;
                              jpp.addTextAlarm(hora_sensor+" Humedad baja en Nodo:"+moteID+" Lectura: "+ (float) Humedad + " m3/m3"+"\n");
                          } else {
                              ALARM_HUM_MIN = false;
                          }
                         
                         
                         }
      
                          
                          
                          
                          
                          
                          
                          
                          ///
                          
                          if(localMote != null) { 	// if the mote is in the database
				// we update the database
				localMote.setCount(dm.get_seqno());
				localMote.setReading(dm.get_data());
				//localMote.setReadingLong(dm.get_readingLong());
				//localMote.setScale(dm.get_scale());
				//localMote.setEnergy(dm.get_energy());
				localMote.setParentId(dm.get_parent());
				localMote.setQuality(dm.get_metric());
				date = new Date();
				localMote.setLastTimeSeen(date.getTime());
				//logger.addRecord(localMote);
				//chartPanel.addRecord(localMote);
			   } else {					// if the mote is not in the database
				// we add the mote
				date = new Date();
				moteDatabase.addMote(new Mote(	dm.get_source(),
								dm.get_seqno(),
								dm.get_data(),
								0, //dm.get_readingLong(),
								0,//dm.get_scale(),
								0,//dm.get_energy(),
								dm.get_parent(),
								dm.get_metric(),
								date.getTime()));
				localMote = moteDatabase.getMote(dm.get_source());
				if (localMote != null) {
				//logger.addRecord(localMote);

				//chartPanel.addRecord(localMote);
				}

                          			// else if it's a reply
                 else {
			if(localMote == null) 		// if the mote is NOT in the database
				;//consolePanel.append("Reply from unknown mote id=" + collectedMsg.get_moteId(), Util.MSG_MESSAGE_RECEIVED);
				else {
					//consolePanel.append("Reply from mote id=" + collectedMsg.get_moteId(), Util.MSG_MESSAGE_RECEIVED);
					localMote.setParentId(dm.get_parent()); // to change (stack feature)
					localMote.setQuality(dm.get_metric());
					localMote.setLastTimeSeen(date.getTime());
					switch (reply & Constants.REPLY_MASK) {
						case (Constants.BATTERY_AND_MODE_REPLY):
							localMote.setBattery(dm.get_data());
							if((reply & Constants.MODE_MASK) == Constants.MODE_AUTO) {
								localMote.setModeAuto();
							} else {
								localMote.setModeQuery();
							}
							if((reply & Constants.SLEEP_MASK) == Constants.SLEEPING) {
								localMote.setSleeping();
							} else {
								localMote.setAwake();
							}
							break;
						case (Constants.PERIOD_REPLY):
							localMote.setSamplingPeriod(dm.get_data());
							break;
						case (Constants.THRESHOLD_REPLY):
							localMote.setThreshold(dm.get_data());
							break;
						case (Constants.SLEEP_DUTY_CYCLE_REPLY):
							localMote.setSleepDutyCycle(dm.get_data());
							break;
						case (Constants.AWAKE_DUTY_CYCLE_REPLY):
							localMote.setAwakeDutyCycle(dm.get_data());
							break;
					}
				}
			}
			moteDatabase.releaseMutex();
			//requestPanel.moteUpdatedEvent(localMote);
			//applicationPanel.moteUpdatedEvent(localMote);
			//alert.moteUpdatedEvent(collectedMsg.get_readingLong());
	  }


           // log data
	   if (loggin){
              int i;            
              record[0] = "" + moteID;
              record[1] = "" + tipoSensor;
              record[2] = "" + hora;
              record[3] = "" + calidad;
              record[4] = "" + padre;
              record[5] = "" + reply;
              record[6] = "" + calibrarHumedad(data);
              record[7] = "" + seqno;
              record[8] = "" + carga;
              record[9] = "" +  ALARM_HUM_MAX;
              record[10] = "" + ALARM_HUM_MIN;
              record[11] = "" + ALARM_BATTERY;
              try {
                  csvWriter.writeRecord(record);
                  csvWriter.flush();
                  String s = "";
                  for (i = 0; i < 12; i++) {
                      s = s.concat(record[i] + ", ");
                  }
              } catch (Exception e) {
                  e.printStackTrace();
                  //csvWriter.close();
                  
              }
//			  //int timeout=100000000;
//			  if (t-startt > 100000) {
//				  csvWriter.close(); 
//				  loggin = false;
//				  jpp.addText("Se guardaron los Datos"+"\n");
//			  } 
			  }
			  
		  }
	  }

          
          public void endLogging(){
              csvWriter.close();
              loggin = false;
              jpp.addText("Se guardaron los Datos"+"\n");
          }
	/*  

	  public static void main(String[] args) throws Exception {

	    BaseRecolectora br = new BaseRecolectora();
	    br.start();
	  }
	 */

	}
	
