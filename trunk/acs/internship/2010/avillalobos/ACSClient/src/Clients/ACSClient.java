package Clients;

import java.util.LinkedList;
import alma.acs.component.client.AdvancedComponentClient;

public abstract class ACSClient{

	protected AdvancedComponentClient client;
	
	public ACSClient(AdvancedComponentClient _client){
		this.client = _client;
	}
	public abstract LinkedList<String> getStatus();
	
	public void writeInfoMessage(String msg){
		this.client.getContainerServices().getLogger().info(msg);
	}
	
	public void writeWarningMessage(String msg){
		this.client.getContainerServices().getLogger().warning(msg);
	}
	
	public void writeFineMessage(String msg){
		this.client.getContainerServices().getLogger().info(msg);
	}
}
