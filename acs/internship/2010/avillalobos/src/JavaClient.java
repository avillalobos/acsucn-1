import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import alma.ACS.ROdouble;
import alma.WeatherData.*;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.ComponentClient;
import alma.acs.container.corba.AcsCorba;

import alma.ACS.ACSComponent;
import alma.ACS.ACSComponentHelper;
import alma.ACS.ComponentStates;

public class JavaClient extends ComponentClient {

	private  Meteorologic jc;
	
	// Constructor of JavaClient using only info for ComponentClient
	protected JavaClient(Logger logger, String managerLoc, String clientName,AcsCorba externalAcsCorba) throws Exception {
		super(logger, managerLoc, clientName, externalAcsCorba);
		// TODO Auto-generated constructor stub
	}
	
	public double doSomeStuff() throws AcsJContainerServicesEx{
		// This is the name of .xml that define the .idl
		this.jc = MeteorologicHelper.narrow(getContainerServices().getComponent("Meteo"));
		ACSComponent acscomponent = ACSComponentHelper.narrow(this.jc);
		ComponentStates state = acscomponent.componentState();
		System.out.println("El nombre del comnponente es " + this.getContainerServices().getName() + " y el stado del componente es " + state.toString());
		this.jc.displayMessage("y desde el cliente te digo, componente CTM!");
		ROdouble t = this.jc.temperature();
		System.out.println("la temperatura en el OSF es de " + t.default_value());
        System.out.println("Obteniendo el componente Meteorologic");
        return t.default_value();
	}
	
	public void loggerFINEmessage(String msg){
		this.m_logger.fine(msg);
	}
	
	public String getStateFromComponent(String name){
		try {
			ACSComponent acscomponent = ACSComponentHelper.narrow(this.getContainerServices().getComponent(name));
			return acscomponent.componentState().toString();
		} catch (AcsJContainerServicesEx e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "Component Doesn't Exist";
		}
	}

	public static void main(String args[]) throws Exception{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//String managerLoc = System.getProperty("ACS.manager");
		String managerLoc = "corbaloc::10.195.17.114:3000/Manager";
		System.out.println("managerloc = " + managerLoc);
		String clientName = "ACS Generci Java Client";
		JavaClient javaclient = new JavaClient(null,managerLoc,clientName, null);
		double d = javaclient.doSomeStuff();
		System.out.println("La temperatura retornada es de " + d);
		System.out.println("Ingrese el nombre del componente");
		String componentName = br.readLine();
		while (!componentName.equals("exit")){
			System.out.println("El stado del componente es " + javaclient.getStateFromComponent(componentName));
			System.out.println("Ingrese el nombre del componente");
			componentName = br.readLine();
		}
		javaclient.loggerFINEmessage("Exiting from java Client");
	}
}