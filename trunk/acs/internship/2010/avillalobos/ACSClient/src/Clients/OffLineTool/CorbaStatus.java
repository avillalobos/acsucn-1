package Clients.OffLineTool;

import java.util.LinkedList;

import org.omg.CORBA.SystemException;

import Clients.ACSClient;
import alma.acs.component.client.AdvancedComponentClient;

/**
 * This class display the corba status, to do that, just try to get the list of initilized services, 
 * if its possible to retrieve this list, then Corba is up, else corba could be on error or Unknown.
 * This way of obtain the corba status was copied from runOMC.
 * 
 * @author Andres Villalobos, 2011 Summerjob, Ingenieria de ejecucion en Computacion e Informatica, Universidad Catolica del norte
 * @see The project twiki http://almasw.hq.eso.org/almasw/bin/view/JAO/OfflineToolsSummerJob
 * 		My dailylog http://almasw.hq.eso.org/almasw/bin/view/Main/AndresVillalobosDailyLogOfflineTools
 * 		Contact to a.e.v.r.007@gmail.com
 */
public class CorbaStatus extends ACSClient {

	/**
	 * This is the constructor of CorbaStatus, and just initialize the AdvancedComponentClient from ACSClient
	 * 
	 * @param _client The client that will be used to get the ORB and test the corba status
	 */
	public CorbaStatus(AdvancedComponentClient _client) {
		super(_client);
		// TODO Auto-generated constructor stub
	}

	/**
	 * This is the implementation of getStatus for CorbaStatus, this implementation return a list of string
	 * that contain the corba status on JSON format, the status of corba could be the following:
	 * 		1) {\"Type\":\"CorbaStatus\",\"Status\":\"Ok\"}
	 * 		2) {\"Type\":\"CorbaStatus\",\"Status\":\"Error\"}
	 * 		3) {\"Type\":\"CorbaStatus\",\"Status\":\"Unknown\"}
	 */
	@Override
	public LinkedList<String> getStatus() {
		LinkedList<String> report = new LinkedList<String>();
		try {
			this.client.getAcsCorba().getORB().list_initial_services();
			report.add("{\"Type\":\"CorbaStatus\",\"Status\":\"Ok\"},");
		} catch (SystemException exc) {
			// org.omg.CORBA.SystemException is what we expect in the "ordinary" error case
			report.add("{\"Type\":\"CorbaStatus\",\"Status\":\"Error\"},");
		} catch (Throwable t) {
			// any other error is not foreseen
			report.add("{\"Type\":\"CorbaStatus\",\"Status\":\"Unknown\"},");
		}
		return report;
	}

	

}
