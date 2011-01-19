
import java.util.logging.Level;
import java.util.logging.Logger;


import alma.acs.container.ContainerServicesBase;
import alma.JavaContainerError.*;
import alma.acs.component.client.ComponentClient;
import alma.acs.container.corba.AcsCorba;

public class JavaClient extends ComponentClient {

	private  JavaClient jc;
	
	protected JavaClient(Logger logger, String managerLoc, String clientName,
			AcsCorba externalAcsCorba) throws Exception {
		super(logger, managerLoc, clientName, externalAcsCorba);
		// TODO Auto-generated constructor stub
	}
	
	public void doSomeStuff() throws AcsJContainerServicesEx{
        this.jc = (JavaClient) getContainerServices().getComponent("Meteorologic");
	}


	public static void main(String args[]){
		System.out.println("Hello world");
	}
	
	
}
