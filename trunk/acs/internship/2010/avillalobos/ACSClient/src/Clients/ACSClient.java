package Clients;

import java.util.LinkedList;
import alma.acs.component.client.AdvancedComponentClient;

public abstract class ACSClient{

	protected AdvancedComponentClient client;
	
	public ACSClient(AdvancedComponentClient _client){
		this.client = _client;
	}
	public abstract LinkedList<String> getStatus();
	
}
