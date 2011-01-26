import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CosPropertyService.InvalidPropertyName;
import org.omg.CosPropertyService.PropertyNotFound;

import si.ijs.maci.ContainerInfo;

import alma.maci.cdbtypes.*;

import alma.ACS.ROdouble;
import alma.WeatherData.*;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.ComponentClient;
import alma.acs.container.corba.AcsCorba;
import alma.acs.container.AcsContainer;
import alma.acs.container.AcsManagerProxy;
import alma.exec.operatorbase.portable.ExecRole;
import alma.exec.operatorbase.portable.MainController;
import alma.exec.operatormasterclient.OperatorMasterClientLogic;
import alma.maci.*;
import alma.maci.managerconfig.Manager;

import alma.ACS.ACSComponent;
import alma.ACS.ACSComponentHelper;
import alma.ACS.ComponentStates;
import alma.ACS.MasterComponent;
import alma.ACS.MasterComponentHelper;
import alma.ACSErr.Completion;
import alma.ACSErr.CompletionHolder;

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
		// Casting JavaClient to ACSComponent
		ACSComponent acscomponent = ACSComponentHelper.narrow(this.jc);
		// Getting state from ACSComponent
		ComponentStates state = acscomponent.componentState();
		System.out.println("El nombre del comnponente es " + this.getContainerServices().getName() + " y el stado del componente es " + state.toString());
		this.jc.displayMessage("   ## Mensaje enviado desde el cliente ##   ");
		// Getting temperature from JavaClient
		ROdouble t = this.jc.temperature();
		System.out.println("la temperatura en el OSF es de " + t.default_value());
        System.out.println("Obteniendo el componente Meteorologic");
        return t.default_value();
	}
	
	// Write a message into java logger
	public void loggerFINEmessage(String msg){
		this.m_logger.fine(msg);
	}
	
	// Return the state from a component name
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
	
	// Send to getStatusFromAllknowComponents a list of components to check
	public void TestingAllComponentStatus(){
		String components[] = {"Meteo"};
		String status[][] = getStatusFromAllknowComponents(components);
		for(int i = 0; i < 1; i++){
			System.out.println("Status from component " + status[i][0] + " is " + status[i][1]);
		}
	}
	
	// Get the name-status from a list of component 
	public String[][] getStatusFromAllknowComponents(String components[]){
		String status[][] = new String[2][3];
		for(int i = 0; i < 1; i ++){
			try {
				ACSComponent acscomponent = ACSComponentHelper.narrow(this.getContainerServices().getComponent(components[i]));
				
				status[i][0] = components[i];
				status[i][1] = acscomponent.componentState().toString();
				//return acscomponent.componentState().toString();
			} catch (AcsJContainerServicesEx e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//return "Component Doesn't Exist";
				status[i][0] = "Component Doesn't Exist";
				status[i][1] = "FAILED";
			}
		}
		return status;
	}
	
	// This is just to test if component is running according to the name entered
	public void TestingNameStatusComponent() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Ingrese el nombre del componente");
		String componentName = br.readLine();
		while (!componentName.equals("exit")){
			System.out.println("El stado del componente es " + getStateFromComponent(componentName));
			System.out.println("Ingrese el nombre del componente");
			componentName = br.readLine();
		}
	}
	
	public void getStateFromContainer(){
		//AcsManagerProxy acsmgrproxy = new AcsManagerProxy("corbaloc::10.195.17.114:3000/Manager", null, m_logger);
		Manager mgr = new Manager();
		OperatorMasterClientLogic op = new OperatorMasterClientLogic();
		initRemoteLogging();
		//op.start(new MainController.StartupSettings(), "almadev", null);
		op.getClientConnectivity().getClass().toString();
		System.out.println(op.getClientConnectivity().getContainerServices().getName());
		System.out.println("Dato del operatos master client logic  " + op.getClientData().omcNetName.toString());
		/*
		ContainerInfo cinfo = new ContainerInfo();
		Array arr = mgr.getServiceComponents();
		ArrayItem arri[] = arr.getArrayItem();
		for(int i = 0; i < arri.length; i++){
			System.out.println("Array element " + arri[i].get().getString());
		}
		*/
	}
	
	public void Something(){
		//javaclient.TestingNameStatusComponent();
		//javaclient.getStateFromContainer();
		/*AcsCorba corba = new AcsCorba(javaclient.m_logger);
		corba.initCorbaForClient(false);
		corba.runCorba();
		//corba.getORB().get_current();
		 */
		MyOperatorMasterClientLogic op = new MyOperatorMasterClientLogic();
		op.start(new MainController.StartupSettings(), "almadev", ExecRole.fromString("Software Developer"));
		//op.getConnectivity().getContainerServices().toString();
		System.out.println(op.getClientConnectivity().getMaciInformation().getContainer("bilboContainer").toString());
	}
	
	public void getStateFromSubsystems(){
		org.omg.CORBA.Object ref = null;
		MasterComponent masterComp = null;
		try {
			ref = getContainerServices().getComponent("CONTROL_MASTER_COMP");
			masterComp = MasterComponentHelper.narrow(ref);
			masterComp.get_all_characteristics().get_property_value("status").extract_string();
		} catch (AcsJContainerServicesEx e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BAD_OPERATION e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPropertyName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Cantidad de caracteristicas" + masterComp.get_all_characteristics().get_number_of_properties());
		System.out.println("Estado actual del subsistema  " + masterComp.componentState().toString());
		alma.ACS.ROstringSeq baciProperty = masterComp.currentStateHierarchy();

		Completion compl = new Completion(System.currentTimeMillis(), 0, 0, new alma.ACSErr.ErrorTrace[]{});
		CompletionHolder complHolder = new CompletionHolder(compl);
		
		String[] status = baciProperty.get_sync(complHolder);
		for(int i =0; i < status.length; i++){
			System.out.println(status[i]);
		}
	}


	public static void main(String args[]) throws Exception{
		
		//String managerLoc = System.getProperty("ACS.manager");
		String managerLoc = "corbaloc::10.195.17.114:3000/Manager";
		System.out.println("managerloc = " + managerLoc);
		String clientName = "ACS Generic Java Client";
		JavaClient javaclient = new JavaClient(null,managerLoc,clientName, null);
		//double d = javaclient.doSomeStuff();
		System.out.println("Status from component " + javaclient.getStateFromComponent("CONTROL/DV01"));
		//System.out.println("La temperatura retornada es de " + d);
		javaclient.getStateFromSubsystems();
		javaclient.loggerFINEmessage("Exiting from java Client");
	}
}