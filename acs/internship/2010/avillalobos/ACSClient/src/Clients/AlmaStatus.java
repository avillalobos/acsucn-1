package Clients;

import java.util.HashMap;
import java.util.LinkedList;

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
		report.add("{Type:Alma,Status:'"+calculateStatus()+"'}");
		return report;
	}
	
	public String calculateStatus(){
		HashMap<String,String> SubSystemsStatus = this.SubsystemStatus.getSubSystemsStatus();
		String SubSystemList[] = this.SubsystemStatus.getSubsystems();
		int SHUTDWON = 0, OPERATIONAL = 0, STARTING1 = 0, STARTING2 = 0, STOPPING = 0;
		for(String subsystem : SubSystemList){
			String status = SubSystemsStatus.get(subsystem);
			if(status.equalsIgnoreCase("ERROR")){
				return "ERROR";
			}else if(status.equalsIgnoreCase("UNAVAILABLE")){
				return "UNAVAILABLE";
			}else if(status.equalsIgnoreCase("SHUTDOWN")){
				SHUTDWON++;
			}else if(status.equalsIgnoreCase("OPERATIONAL")){
				OPERATIONAL++;
			}
			
			if(status.equalsIgnoreCase("ONLINE") || status.equalsIgnoreCase("OPERATIONAL")){
				STARTING1++;
			}else if(status.equalsIgnoreCase("INITIALIZING_PASS1") || status.equalsIgnoreCase("INITIALIZING_PASS2") || status.equalsIgnoreCase("SHUTDOWN") || status.equalsIgnoreCase("ONLINE") || status.equalsIgnoreCase("OPERATIONAL")){
				STARTING2++; 
			}else if(status.equalsIgnoreCase("SHUTTINGDOWN_PASS1") || status.equalsIgnoreCase("SHUTTINGDOWN_PASS2") || status.equalsIgnoreCase("SHUTDOWN") || status.equalsIgnoreCase("OPERATIONAL")){
				STOPPING++;
			}
		}
		if(SHUTDWON == SubSystemList.length){
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