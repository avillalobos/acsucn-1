package Clients.OffLineTool;

import java.util.HashMap;
import java.util.LinkedList;

import Clients.ACSClient;
import alma.acs.component.client.AdvancedComponentClient;

public class AlmaStatus extends ACSClient {

	private SubsystemStatus SubsystemStatus;
	
	public AlmaStatus(AdvancedComponentClient _client,SubsystemStatus _SubsystemStatus) {
		super(_client);
		this.SubsystemStatus = _SubsystemStatus;
		// TODO Auto-generated constructor stub
	}

	@Override
	public LinkedList<String> getStatus() {
		// TODO Auto-generated method stub
		LinkedList<String> report = new LinkedList<String>();
		report.add("{\"Type\":\"AlmaStatus\",\"Status\":\""+calculateStatus()+"\"}");
		return report;
	}
	
	public String calculateStatus(){
		HashMap<String,String> SubSystemsStatus = this.SubsystemStatus.getSubSystemsStatus();
		String SubSystemList[] = this.SubsystemStatus.getSubsystems();
		int SHUTDOWN = 0, OPERATIONAL = 0, STARTING1 = 0, STARTING2 = 0, STOPPING = 0;
		for(String subsystem : SubSystemList){
			String status = SubSystemsStatus.get(subsystem);
			//System.out.println("subsystem " + subsystem + " status " + status);
			if (status != null) {
				if (status.equalsIgnoreCase("ERROR")) {
					return "ERROR";
				} else if (status.equalsIgnoreCase("UNAVAILABLE")) {
					return "UNAVAILABLE";
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