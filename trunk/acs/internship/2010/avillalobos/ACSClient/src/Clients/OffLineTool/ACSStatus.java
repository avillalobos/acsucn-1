package Clients.OffLineTool;

import java.util.LinkedList;

import Clients.ACSClient;
import alma.acs.component.client.AdvancedComponentClient;

/**
 * This class is responsible to get the ACS information using offline tools, to do that, just make a ping to the ACS manager,
 * if this method return true, then ACS is up, else ACS is down.
 *
 * @author Andres Villalobos, 2011 Summerjob, Ingenieria de ejecucion en Computacion e Informatica, Universidad Catolica del norte
 * @see DeveloperInfo
 *              The project  <a href="http://almasw.hq.eso.org/almasw/bin/view/JAO/OfflineToolsSummerJob">Twiki page</a> and
 *              my <a href="http://almasw.hq.eso.org/almasw/bin/view/Main/AndresVillalobosDailyLogOfflineTools">Dailylog</a> please
 *              Contact to a.e.v.r.007@gmail.com
 */
public class ACSStatus extends ACSClient {

	/**
	 * This is the constructor of ACSStatus, here just initialize the client from ACSClient
	 * 
	 * @param _client The client to make ping to the manager
	 */
	public ACSStatus(AdvancedComponentClient _client) {
		super(_client);
		// TODO Auto-generated constructor stub
	}

	/**
	 * This is the implementation of getStatus for ACSClient, here we make ping to manager
	 * if the method return true, then ACS is up, else is down. This information is added to 
	 * the report list and JSON format and returned to be written on cacheFile
	 * 
	 *  @return A Linked list with the status of ACS, this could be :
	 *  		1) {\"Type:\"ACSStatus\",\"Status\":\"Ok\"}
	 *  		2) {\"Type:\"ACSStatus\",\"Status\":\"Down\"}
	 */
	@Override
	public LinkedList<String> getStatus() {
		LinkedList<String> report = new LinkedList<String>();
		if(isAcsOnline()){
			report.add("{\"Type:\"ACSStatus\",\"Name\":\"ACS Status\",\"Status\":\"Ok\"}");
		}else{
			report.add("{\"Type:\"ACSStatus\",\"Name\":\"ACS Status\",\"Status\":\"Down\"}");
		}
		return report;
	}
}
