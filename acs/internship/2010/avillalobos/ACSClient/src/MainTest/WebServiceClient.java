package MainTest;

import java.util.LinkedList;

import Clients.ACSClient;
import Clients.AntennaClient;
import Clients.SubsystemClient;

public class WebServiceClient {
	public LinkedList<String> getStatus(){
		String managerLoc = "corbaloc::10.195.17.114:3000/Manager";
		LinkedList<ACSClient> clientes= new LinkedList<ACSClient>();
		LinkedList<String> report = new LinkedList<String>();
		try {
			clientes.add(new SubsystemClient(null, managerLoc, "ACS Status Client"));
			clientes.add(new AntennaClient(null, managerLoc, "ACS Status Client"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(ACSClient acs : clientes){
			report.addAll(acs.getStatus());
		}
		return report;
	}
}
