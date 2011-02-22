package Clients.OnLineTool;

import java.util.LinkedList;

import alma.acs.component.client.AdvancedComponentClient;

import Clients.ACSClient;

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
public class AlmaStatus extends ACSClient {

	/**
	 * @param opserver This parameter contains the connection to the opserver, which this references
	 * this class can get the Alma information and can be added to the ACSClients list.
	 */
	private OpServerConnection opserver;
	
	/**
	 * The constructor of AlmaStatus, this class need the client for ACSClient and a OpServerConnection to get
	 * the Alma information from Opserver
	 * 
	 * @param _client needed for ACSClient
	 * @param _opserver Connection to get information of ACSStatus
	 */
	public AlmaStatus(AdvancedComponentClient _client, OpServerConnection _opserver) {
		super(_client);
		this.opserver = _opserver;
		// TODO Auto-generated constructor stub
	}

	/**
	 * This is the override of getStatus and just retrieve the information produced by opserver related 
	 * to Alma Status
	 */
	@Override
	public LinkedList<String> getStatus() {
		return this.opserver.getServerClient().getStatusList("AlmaStatus");
	}

}
