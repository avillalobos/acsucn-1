package Clients;

import java.util.HashMap;
import java.util.LinkedList;

//import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.AdvancedComponentClient;
import alma.acs.component.client.ComponentClient;

public class DevicesStatus extends ACSClient {

	private HashMap<String,ComponentClient> References;
	private String[] devices;
	
	public DevicesStatus(AdvancedComponentClient _client,String[] devices) {
		super(_client);
		this.References = new HashMap<String,ComponentClient>();
		this.devices = devices;
		// TODO Auto-generated constructor stub
	}

	@Override
	public LinkedList<String> getStatus() {
		// TODO Auto-generated method stub
		LinkedList<String> report = new LinkedList<String>();
		
		return report;
	}
	
	/*
	private ComponentClient getReferences(String componentName){
		try {
			ComponentClient cc = (ComponentClient) this.client.getContainerServices().getComponentNonSticky(componentName);
			return cc;
		} catch (AcsJContainerServicesEx e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	*/
}
