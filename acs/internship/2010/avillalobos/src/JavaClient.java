import java.util.logging.Logger;

import alma.ACS.ROdouble;
import alma.WeatherData.*;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.ComponentClient;
import alma.acs.container.corba.AcsCorba;

public class JavaClient extends ComponentClient {

	private  Meteorologic jc;
	
	// Constructor of JavaClient using only info for ComponentClient
	protected JavaClient(Logger logger, String managerLoc, String clientName,AcsCorba externalAcsCorba) throws Exception {
		super(logger, managerLoc, clientName, externalAcsCorba);
		// TODO Auto-generated constructor stub
	}
	
	public void doSomeStuff() throws AcsJContainerServicesEx{
		// This is the name of .xml that define the .idl
		this.jc = MeteorologicHelper.narrow(getContainerServices().getComponentNonSticky("Meteo"));
		this.jc.displayMessage("y desde el cliente te digo, componente CTM!");
		ROdouble t = this.jc.temperature();
		System.out.println("la temperatura en el OSF es de " + t.default_value());
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