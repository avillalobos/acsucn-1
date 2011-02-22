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
public class ContainerStatus extends ACSClient {

	/**
	 * @param opserver This is the reference to the opserver listener, with this parameter we can get
	 * the information from opserver.
	 */
	private OpServerConnection opserver;
	
	/**
	 * This is the constructor of ContainerStatus
	 * 
	 * @param _client This parameter is needed by ACSClient
	 * @param _opserver This parameter is need to get the information from opserver
	 */
	public ContainerStatus(AdvancedComponentClient _client, OpServerConnection _opserver) {
		super(_client);
		this.opserver = _opserver;
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method return the list of container that are active and the container that are down.
	 * This information is returned by a linkedlist of string with json format
	 */
	@Override
	public LinkedList<String> getStatus() {
		return this.opserver.getServerClient().getStatusList("ContainerStatus");
	}

}
