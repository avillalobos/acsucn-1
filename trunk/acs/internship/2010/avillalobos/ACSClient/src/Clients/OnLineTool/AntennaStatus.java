package Clients.OnLineTool;

import java.util.HashMap;
import java.util.LinkedList;

import alma.acs.component.client.AdvancedComponentClient;

import Clients.ACSClient;
import Clients.AntennaActiveMQMessageListener;

/**
 * This class is encharged of the Alma Status information comming from opserver. This class exist just to 
 * maintain the order, because all this information can be get from opserver, but in order to make more
 * clear the way which we are getting the information, i made this class just to call a function on
 * OpServerConnection and pass the information from OpServerConnection to Publisher
 *
 * @author Andres Villalobos, 2011 Summerjob, Ingenieria de ejecucion en Computacion e Informatica, Universidad Catolica del norte
 * @see DeveloperInfo
 *              The project  <a href="http://almasw.hq.eso.org/almasw/bin/view/JAO/OfflineToolsSummerJob">Twiki page</a> and
 *              my <a href="http://almasw.hq.eso.org/almasw/bin/view/Main/AndresVillalobosDailyLogOfflineTools">Dailylog</a> please
 *              Contact to a.e.v.r.007@gmail.com
 */
public class AntennaStatus extends ACSClient {

	/**
	 * @param opserver This parameter contains the connection to the opserver, which this references
	 * this class can get the Alma information and can be added to the ACSClients list.
	 */
	private OpServerConnection opserver;
	
	/**
	 * @param antennaActiveMQMessageListener This parameter make possible to get information from ActiveMQ
	 */
	private AntennaActiveMQMessageListener antennaActiveMQMessageListener;
	
	/**
	 * The constructor of AntennaStatus, this class need the client for ACSClient and a OpServerConnection to get
	 * the Antenna information from Opserver
	 * 
	 * @param _client Client needed by ACSClient
	 * @param _opserver references needed to get information to AntennaStatus
	 * @param _brokerURL Indicate the address to ActiveMQ server
	 */
	public AntennaStatus(AdvancedComponentClient _client, OpServerConnection _opserver,String _brokerURL, String[] _devices) {
		super(_client);
		this.opserver = _opserver;
		this.antennaActiveMQMessageListener = new AntennaActiveMQMessageListener(_brokerURL, _devices);
		// TODO Auto-generated constructor stub
	}

	/**
	 * This is the override of getStatus and just retrieve the information produced by opserver related 
	 * to Antenna Status
	 */
	@Override
	public LinkedList<String> getStatus() {
		return this.opserver.getServerClient().getStatusList("AntennaStatus");
	}
	
	/**
	 * This method return the hashmap with the antenna info on json format, the key of this
	 * hashmap is equal to the name of the antenna and the value is the status of this antenna
	 * on json format
	 * @return
	 */
	public HashMap<String,String> getAntennaInfo(){
		return this.opserver.getServerClient().getAntennaStatus();
	}
	
	/**
	 * This method return a hashmap with the device info on json format, the key of this
	 * hashmap is equal to the name of the device an the value is the status of this device.
	 * This information come from activeMQ queue
	 * @return
	 */
	public HashMap<String,String> getActiveMQInfo(){
		return this.antennaActiveMQMessageListener.getHashReport();
	}

}
