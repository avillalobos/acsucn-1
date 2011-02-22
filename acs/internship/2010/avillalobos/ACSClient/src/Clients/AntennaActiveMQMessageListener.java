package Clients;

import java.util.HashMap;
import java.util.LinkedList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * This class is encharged to receive the information from ActiveMQ queue. The devices
 * that receive by parameter on the constructor is under $AXIS2_HOME/WebOMCFiles/Devices
 * and are listed one by one, all devices with information on ActiveMQ must to be on this file
 * because the Map message need the name of the device to get the json object to display. Example:
 * 
 * 	DV01_LLC
 * 	DV01_LO
 *
 * @author Andres Villalobos, 2011 Summerjob, Ingenieria de ejecucion en Computacion e Informatica, Universidad Catolica del norte
 * @see DeveloperInfo
 *              The project  <a href="http://almasw.hq.eso.org/almasw/bin/view/JAO/OfflineToolsSummerJob">Twiki page</a> and
 *              my <a href="http://almasw.hq.eso.org/almasw/bin/view/Main/AndresVillalobosDailyLogOfflineTools">Dailylog</a> please
 *              Contact to a.e.v.r.007@gmail.com
 */ 
public class AntennaActiveMQMessageListener implements MessageListener {
	
	/**
	 * @param factory This parameter allow to connect with ActiveMQ broker
	 */
    private ConnectionFactory factory;
    
    /**
     * @param connection This parameter have the instance to the connection, provided by the
     * singleton class on ActiveMQ
     */
    private Connection connection;
    
    /**
     * @param session This parameter allow to this client create session to get information from
     * some Queue or Topic
     */
    private Session session;
    
    /**
     * @param consumer This parameter save the session created to the Queue or Topic, and later
     * you can use this parameter to send and receive messages. Also, allow to register the 
     * client, on this case, this class is registered as a listener.
     */
    private MessageConsumer consumer;
    
    /**
     * @param report This parameter saved the message on json format to display it later, when getStatus has been called
     */
    private LinkedList<String> report;
    
    /**
     * @param HashMapReport This parameter saved the message on json format to display it later, when getStatus has been called
     */
    private HashMap<String,String> HashMapReport;
    
    /**
     * @param Devices The devices that are sending status to ActiveMQ queue called AntennaStatus
     */
    private String[] Devices;
	
    /**
     * The constructor of AntennaActiveMQMessageListener
     * 
     * @param brokerURL Indicate where ir the AntennaStatus queue
     * @param _devices indicate which devices will have information on the queue
     */
    public AntennaActiveMQMessageListener(String brokerURL, String[] _devices){
    	this.Devices = _devices;
    	this.factory = new ActiveMQConnectionFactory(brokerURL);
    	this.report = new LinkedList<String>();
    	this.HashMapReport = new HashMap<String,String>();
    	try {
			this.connection = factory.createConnection();
			this.connection.start();
	    	this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    	this.consumer = session.createConsumer(session.createQueue("AntennaStatus"));
	    	this.consumer.setMessageListener(this);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
    	
    }
	
    /**
     * This is the method executed when there is a message on the queue. 
     */
	@Override
	public synchronized void onMessage(Message message) {
		// TODO Auto-generated method stub
		if(message instanceof MapMessage){
			MapMessage txt = (MapMessage)message;
			try {
				for (String dev : this.Devices) {
					System.out.println("\t ## Device "+dev + " exist ? = " + txt.itemExists(dev));
					if (txt.itemExists(dev)) {
						System.out.println("\n\t #### A message from activeMQ is comming = "+ txt.getString(dev) + " #### \n");
						this.HashMapReport.put(dev, txt.getString(dev));
						// this.report.add(txt.getString("LLC"));
					}
				}
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				this.report.add("{\"Type\":\"AntennaActiveMQError\",\"Description\":\"Couldn't retrieve information from ActiveMQ\"},");
			}
		}
	}

	/**
	 * This method return a list of status on json format
	 * Example:
	 * 		{"Type":"DV01_LLC", "Name": "DV01/LLC", "MonitorPoint":"AMBIENT_TEMPERATURE","Value":"301.4","Timestamp":"2011-01-30T00:00:27"}
	 * 
	 * @return A list of status on json format
	 */
	public synchronized LinkedList<String> getListReport() {
		LinkedList<String> copy = new LinkedList<String>(this.report);
		this.report.clear();
		if(copy.size() == 0){
			copy.add("{\"Type\":\"AntennaActiveMQError\",\"Description\":\"There is not new information from ActiveMQ\"},");
		}
		return copy;
	}
	
	/**
	 * This method return a hashmap with the information about antenna and devices, this information can be access through the full name
	 * of the device or antenna, replacing "/" by "_". 
	 * @return
	 */
	public synchronized HashMap<String,String> getHashReport(){
		HashMap<String, String> copy = new HashMap<String,String>();
		copy.putAll(this.HashMapReport);
		this.HashMapReport.clear();
		return copy;
	}
}
