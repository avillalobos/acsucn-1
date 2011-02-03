package Clients.OffLineTool;

import java.util.LinkedList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class AntennaActiveMQMessageListener implements MessageListener {

	//public static String brokerURL = "tcp://localhost:61616";
	 
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
	
    public AntennaActiveMQMessageListener(String brokerURL){
    	this.factory = new ActiveMQConnectionFactory(brokerURL);
    	this.report = new LinkedList<String>();
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
    
	private LinkedList<String> report;
	
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		if(message instanceof TextMessage){
			TextMessage txt = (TextMessage)message;
			try {
				report.add(txt.getText());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				this.report.add("{\"Type\":\"AntennaActiveMQError\",\"Description\":\"Couldn't retrieve information from ActiveMQ\"},");
			}
		}
	}

	public LinkedList<String> getReport() {
		LinkedList<String> copy = new LinkedList<String>(this.report);
		this.report.clear();
		if(copy.size() == 0){
			copy.add("{\"Type\":\"AntennaActiveMQError\",\"Description\":\"There is not new information from ActiveMQ\"},");
		}
		return copy;
	}
	
}