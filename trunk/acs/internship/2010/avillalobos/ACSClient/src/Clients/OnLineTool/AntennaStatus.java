package Clients.OnLineTool;

import java.util.HashMap;
import java.util.LinkedList;

import alma.acs.component.client.AdvancedComponentClient;

import Clients.ACSClient;

public class AntennaStatus extends ACSClient {

	private OpServerConnection opserver;
	public AntennaStatus(AdvancedComponentClient _client, OpServerConnection _opserver) {
		super(_client);
		this.opserver = _opserver;
		// TODO Auto-generated constructor stub
	}

	@Override
	public LinkedList<String> getStatus() {
		return this.opserver.getServerClient().getStatusList("ACSStatus");
	}
	
	public HashMap<String,String> getAntennaInfo(){
		return this.opserver.getServerClient().getAntennaStatus();
	}

}
