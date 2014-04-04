/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jhumedadsuelos;

import com.csvreader.CsvWriter;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.tinyos.message.MoteIF;
import net.tinyos.packet.BuildSource;
import net.tinyos.packet.PhoenixSource;

/**
 *
 * @author pablo
 */

public class HumedadSuelo extends JFrame implements WindowListener{
    private static String MOTECOM = "serial@/dev/ttyUSB0:57600";
    //private static String MOTECOM = "serial@/dev/ttyUSB1:57600";
    //private static String MOTECOM = "serial@COM26:57600";
    private static MoteIF enlace;
    private static int index_node=2;
    private static MsgSender sender;
    private static Chart2D chartPanel;
    private static JPanelPedidos panelPedidos;
    public static HumedadSuelo marco = new HumedadSuelo();
    private static MapPanel mapPanel;
    private static MoteDatabase moteDatabase;
    private static String IPdestino="192.168.2.135";
    /**
     * @param args the command line arguments
     */

    	private static void crearGUI() {
            sender = new MsgSender(enlace);

            chartPanel = new Chart2D();
            
            //create first trace
            ITrace2D trace1 = new Trace2DLtd(200);
            trace1.setColor(Color.RED);
            chartPanel.addTrace(trace1);
            
            //create second trace
            ITrace2D trace2 = new Trace2DLtd(200);
            trace2.setColor(Color.BLUE);
            chartPanel.addTrace(trace2);
            
            //create third trace
            ITrace2D trace3 = new Trace2DLtd(200);
            trace3.setColor(Color.ORANGE);
            chartPanel.addTrace(trace3);
            
            
            moteDatabase = new MoteDatabase();
            mapPanel = new MapPanel(moteDatabase);

            panelPedidos = new JPanelPedidos(sender);
            panelPedidos.setVisible(true);
            Thread recolector = new Thread(new BaseRecolectora(enlace, trace1,trace2,trace3, panelPedidos, moteDatabase, index_node));
            JTabbedPane rightTabbedPanel = new JTabbedPane();
            rightTabbedPanel.addTab("Msj Radio", null, panelPedidos,
                  	"Send requests over the network");
            JTabbedPane leftTabbedPanel = new JTabbedPane();
            leftTabbedPanel.addTab("Grafica Lecturas", null, chartPanel,
                  	"Display a chart of the sensor values of the network");
            leftTabbedPanel.addTab("Network Map", null, mapPanel,
                  	"Display a map of the network");
            marco.getContentPane().setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(5,5,5,5);
            c.fill = GridBagConstraints.BOTH; c.weighty = 1; c.weightx = 1;
            c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.gridheight = 1; c.anchor = GridBagConstraints.FIRST_LINE_START;
            marco.getContentPane().add(leftTabbedPanel, c);
            leftTabbedPanel.setPreferredSize(new Dimension(600,400)); // 600x400 cambiar proporciones
            c.fill = GridBagConstraints.HORIZONTAL; c.weighty = 0; c.weightx = 1;
            c.gridx = 0; c.gridy = 1; c.gridwidth = 1; c.anchor = GridBagConstraints.LAST_LINE_START;
            c.fill = GridBagConstraints.VERTICAL; c.weighty = 1; c.weightx = 0;
            c.gridx = 1; c.gridy = 0; c.gridheight = 2; c.anchor = GridBagConstraints.LINE_END;
            marco.getContentPane().add(rightTabbedPanel, c);
            //Display the window.
            try { // Set System L&F
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());	// System Theme
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());	// Metal Theme
		//UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            } catch (Exception e) {e.printStackTrace();}
            
            marco.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            SwingUtilities.updateComponentTreeUI(marco);

            recolector.start();
            sender.start();
            marco.pack();
            marco.setVisible(true);
            marco.addWindowListener(marco);

        }
    public static void main(String[] args) {
       
        //PhoenixSource ps = BuildSource.makePhoenix(BuildSource.makeSF(IPdestino,9002),net.tinyos.util.PrintStreamMessenger.err); //Mandar por TCP-IP
        PhoenixSource ps = BuildSource.makePhoenix(MOTECOM,net.tinyos.util.PrintStreamMessenger.err);
			
			enlace = new MoteIF(ps);

		if (enlace == null) {
                        System.out.println("no se encontro un enlace");
			System.exit(-1);
		}

                javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                crearGUI();
            }
        });
    }
	public void windowOpened(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {
		//recolector.start()
		System.out.print("Cerrando la Base ...");
		marco.dispose();
		System.exit(0);
	}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}


}
