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
public class CorbaStatus extends ACSClient{
	private OpServerConnection opserver;
	public CorbaStatus(AdvancedComponentClient _client) {
		super(_client);
		// TODO Auto-generated constructor stub
	}

	@Override
	public LinkedList<String> getStatus() {
		// TODO Auto-generated method stub
		this.opserver.getClass();
		return null;
	}

}
