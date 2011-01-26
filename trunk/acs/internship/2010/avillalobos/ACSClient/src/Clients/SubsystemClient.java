package Clients;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CosPropertyService.InvalidPropertyName;
import org.omg.CosPropertyService.PropertyNotFound;

import alma.ACS.MasterComponent;
import alma.ACS.MasterComponentHelper;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;


public class SubsystemClient extends ACSClient{
	
	private String[] Subsystems;
	
	public SubsystemClient(Logger logger, String managerLoc, String clientName)
			throws Exception {
		super(logger, managerLoc, clientName);
		this.Subsystems = getSubsystems().split(",");
	}


	public Process getCCLInfo(){
		try {
			return Runtime.getRuntime().exec("ssh localhost htop");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public LinkedList<String> getStatus() {
		LinkedList<String> report = new LinkedList<String>();
		org.omg.CORBA.Object ref = null;
		MasterComponent masterComp = null;
		for(int i = 0; i < this.Subsystems.length; i++){
			try {
				ref = getContainerServices().getComponent("CONTROL_MASTER_COMP");
				masterComp = MasterComponentHelper.narrow(ref);
			} catch (AcsJContainerServicesEx e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BAD_OPERATION e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println("Cantidad de caracteristicas" + masterComp.get_all_characteristics().get_number_of_properties());
			//System.out.println("Estado actual del subsistema  " + masterComp.componentState().toString());
			report.add("Estado actual del subsistema  " + masterComp.componentState().toString());
			//alma.ACS.ROstringSeq baciProperty = masterComp.currentStateHierarchy();
		}
		
		return report;
	}
	
	private String getSubsystems(){
		return "CONTROL_MASTER_COMP";
	}

	@Override
	public void Start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Stop() {
		// TODO Auto-generated method stub
		
	}
	
}