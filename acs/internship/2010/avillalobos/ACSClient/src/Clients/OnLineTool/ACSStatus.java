package Clients.OnLineTool;

import java.util.LinkedList;

import alma.acs.component.client.AdvancedComponentClient;

import Clients.ACSClient;

public class ACSStatus extends ACSClient {

	private OpServerConnection opserver;
	public ACSStatus(AdvancedComponentClient _client, OpServerConnection _opserver) {
		super(_client);
		this.opserver = _opserver;
		// TODO Auto-generated constructor stub
	}

	@Override
	public LinkedList<String> getStatus() {
		return this.opserver.getServerClient().getStatusList("ACSStatus");
	}

}
