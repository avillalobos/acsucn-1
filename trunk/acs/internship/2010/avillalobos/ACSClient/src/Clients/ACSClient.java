package Clients;

import java.util.LinkedList;
import java.util.logging.Logger;

import alma.acs.component.client.AdvancedComponentClient;

public abstract class ACSClient extends AdvancedComponentClient {

	public ACSClient(Logger logger, String managerLoc, String clientName) throws Exception{
		super(logger,managerLoc,clientName);
	}
	public abstract LinkedList<String> getStatus();
	public abstract void Start();
	public abstract void Stop();
}
