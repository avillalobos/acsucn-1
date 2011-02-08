package Clients.OffLineTool;

import java.util.HashMap;
import java.util.LinkedList;

import Clients.ACSClient;
import alma.acs.component.client.AdvancedComponentClient;

/**
 * This class is responsible to "calculate" the Alma status based on the Sub systems status,
 * thats why on the constructor receive a SubsystemsStatus instance. The algorithm to get
 * the status of Alma software was copied from runOMC but is implemented on another way,
 * here obtain the status of each subsystem, the algorithm is the following:
 * 		1) If any status is on error, then Alma is on error
 * 		2) If any status is on unavailable, then alma is on unavailable
 * 		3) If all status are on shutdown, then alma is on shutdown
 * 		4) If all status are on operational, then alma is on operational
 * 		5) If All status are on online or operational, the alma status is starting
 * 		6) If all status are on initializing or shutdown or online or operational, then is starting
 * 		7) If all status are on shutting down or preshutdown or shutdown or operational, then the alma status is stopping
 * 		8) For any another combination, the alma status is transiting
 * 
 * @author Andres Villalobos, 2011 Summerjob, Ingenieria de ejecucion en Computacion e Informatica, Universidad Catolica del norte
 * @see The project twiki http://almasw.hq.eso.org/almasw/bin/view/JAO/OfflineToolsSummerJob
 * 		My dailylog http://almasw.hq.eso.org/almasw/bin/view/Main/AndresVillalobosDailyLogOfflineTools
 * 		Contact to a.e.v.r.007@gmail.com
 */
public class AlmaStatus extends ACSClient {

	/**
	 * @param SubsystemStatus This variable is used to get the offline status from all subsystems
	 */
	private SubsystemStatus SubsystemStatus;
	
	/**
	 * Constructor of AlmaStatus class
	 * 
	 * @param _client This client is required for ACSClient
	 * @param _SubsystemStatus This is the instance of SubsystemStatus used to get the the required
	 * subsystems status to generate the alma status
	 */
	public AlmaStatus(AdvancedComponentClient _client,SubsystemStatus _SubsystemStatus) {
		super(_client);
		this.SubsystemStatus = _SubsystemStatus;
		// TODO Auto-generated constructor stub
	}

	/**
	 * This is the implementation of getStatus, here a linked list is generated with the status of
	 * alma software on JSON format
	 * 
	 * @return A linked list containing the Alma status, those could be:
	 * 		<li>{\"Type\":\"AlmaStatus\",\"Status\":\"ERROR"\"}</>
	 * 		<li>{\"Type\":\"AlmaStatus\",\"Status\":\"UNAVAILABLE"\"}</>
	 * 		<li>{\"Type\":\"AlmaStatus\",\"Status\":\"SHUTDOWN"\"}</>
	 * 		<li>{\"Type\":\"AlmaStatus\",\"Status\":\"OPERATIONAL"\"}</>
	 * 		<li>{\"Type\":\"AlmaStatus\",\"Status\":\"STARTING"\"}</>
	 * 		<li>{\"Type\":\"AlmaStatus\",\"Status\":\"STOPPING"\"}</>
	 * 		<li>{\"Type\":\"AlmaStatus\",\"Status\":\"TRANSITING"\"}</>
	 */
	@Override
	public LinkedList<String> getStatus() {
		// TODO Auto-generated method stub
		LinkedList<String> report = new LinkedList<String>();
		report.add("{\"Type\":\"AlmaStatus\",\"Status\":\""+calculateStatus()+"\"}");
		return report;
	}
	
	/**
	 * This method return the status of alma software depending of subystems status, the algorithm is 
	 * explained on the class definition
	 * 
	 * @return A string indicating the alma status, those could be:
	 * 		<li>ERROR</>
	 * 		<li>UNAVAILABLE</>
	 * 		<li>SHUTDOWN</>
	 * 		<li>OPERATIONAL</>
	 * 		<li>STARTING</>
	 * 		<li>STOPPING</>
	 * 		<li>TRANSITING</>
	 */
	public String calculateStatus(){
		HashMap<String,String> SubSystemsStatus = this.SubsystemStatus.getSubSystemsStatus();
		String SubSystemList[] = this.SubsystemStatus.getSubsystems();
		int SHUTDOWN = 0, OPERATIONAL = 0, STARTING1 = 0, STARTING2 = 0, STOPPING = 0;
		for(String subsystem : SubSystemList){
			String status = SubSystemsStatus.get(subsystem);
			//System.out.println("subsystem " + subsystem + " status " + status);
			if (status != null) {
				if (status.equalsIgnoreCase("UNAVAILABLE")) {
				return "UNAVAILABLE";
				}else if (status.equalsIgnoreCase("ERROR")) {
					return "ERROR";
				} else if (status.equalsIgnoreCase("SHUTDOWN")) {
					SHUTDOWN++;
				} else if (status.equalsIgnoreCase("OPERATIONAL")) {
					OPERATIONAL++;
				}

				if (status.equalsIgnoreCase("ONLINE")
						|| status.equalsIgnoreCase("OPERATIONAL")) {
					STARTING1++;
				} else if (status.equalsIgnoreCase("INITIALIZING_PASS1")
						|| status.equalsIgnoreCase("INITIALIZING_PASS2")
						|| status.equalsIgnoreCase("SHUTDOWN")
						|| status.equalsIgnoreCase("ONLINE")
						|| status.equalsIgnoreCase("OPERATIONAL")) {
					STARTING2++;
				} else if (status.equalsIgnoreCase("SHUTTINGDOWN_PASS1")
						|| status.equalsIgnoreCase("PRESHUTDOWN")
						|| status.equalsIgnoreCase("SHUTTINGDOWN_PASS2")
						|| status.equalsIgnoreCase("SHUTDOWN")
						|| status.equalsIgnoreCase("OPERATIONAL")) {
					STOPPING++;
				}
			}
		}
		if(SHUTDOWN == SubSystemList.length){
			return "SHUTDOWN";
		}else if(OPERATIONAL == SubSystemList.length){
			return "OPERATIONAL";
		}else if(STARTING1 == SubSystemList.length || STARTING2 == SubSystemList.length){
			return "STARTING";
		}else if(STOPPING == SubSystemList.length){
			return "STOPPING";
		}else {
			return "TRANSITING";
		}
	}

}