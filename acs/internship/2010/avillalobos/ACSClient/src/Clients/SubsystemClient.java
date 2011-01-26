package Clients;

import java.io.IOException;
import java.util.logging.Logger;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CosPropertyService.InvalidPropertyName;
import org.omg.CosPropertyService.PropertyNotFound;

import alma.ACS.MasterComponent;
import alma.ACS.MasterComponentHelper;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;


public class SubsystemClient extends ACSClient{
	
	public SubsystemClient(Logger logger, String managerLoc, String clientName)
			throws Exception {
		super(logger, managerLoc, clientName);
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
	public String[] getStatus() {
		String list[] = new String[1];
		org.omg.CORBA.Object ref = null;
		MasterComponent masterComp = null;
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
		System.out.println("Cantidad de caracteristicas" + masterComp.get_all_characteristics().get_number_of_properties());
		System.out.println("Estado actual del subsistema  " + masterComp.componentState().toString());
		alma.ACS.ROstringSeq baciProperty = masterComp.currentStateHierarchy();
		return list;
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