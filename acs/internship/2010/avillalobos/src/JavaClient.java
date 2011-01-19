
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.Any;

import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.ComponentClient;
import alma.acs.container.corba.AcsCorba;

public class JavaClient extends ComponentClient {

	private  JavaClient jc;
	
	// Constructor of JavaClient using only info for ComponentClient
	protected JavaClient(Logger logger, String managerLoc, String clientName,AcsCorba externalAcsCorba) throws Exception {
		super(logger, managerLoc, clientName, externalAcsCorba);
		// TODO Auto-generated constructor stub
	}
	
	public void doSomeStuff() throws AcsJContainerServicesEx{
		// This is the name of .xml that define the .idl
		String retornado = (getContainerServices().getComponent("Meteo")).toString();
		getContainerServices().getComponentNonSticky("Meteo");
		/*
		Object list[] = getContainerServices().getComponent("Meteo").getClass().getMethods();
		for(int i = 0; i < list.length; i++){
			System.out.println(list[i].toString());
		}
		*/
		System.out.println("retornado = " + retornado);
        //this.jc = (JavaClient) getContainerServices().getComponent("Meteo");
        System.out.println("Obteniendo el componente Meteorologic");
	}


	public static void main(String args[]) throws Exception{
		System.out.println("Hello world");
		//String managerLoc = System.getProperty("ACS.manager");
		String managerLoc = "corbaloc::10.195.17.114:3000/Manager";
		System.out.println("managerloc = " + managerLoc);
		String clientName = "Meteorologic";
		JavaClient javaclient = new JavaClient(null,managerLoc,clientName, null);
		javaclient.doSomeStuff();
	}
	
	
}