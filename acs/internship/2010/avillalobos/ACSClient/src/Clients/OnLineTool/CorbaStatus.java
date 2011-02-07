package Clients.OnLineTool;

import java.util.LinkedList;

import alma.acs.component.client.AdvancedComponentClient;

import Clients.ACSClient;

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
