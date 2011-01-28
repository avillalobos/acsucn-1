package Clients;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.omg.CORBA.BAD_OPERATION;

import alma.ACS.MasterComponent;
import alma.ACS.MasterComponentHelper;
import alma.ACSErr.Completion;
import alma.ACSErr.CompletionHolder;
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
				ref = getContainerServices().getComponent(this.Subsystems[i]);
				masterComp = MasterComponentHelper.narrow(ref);
				report.add("Estado actual del subsistema  " + this.Subsystems[i] + " = " + masterComp.componentState().toString());
			} catch (AcsJContainerServicesEx e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				report.add("No se puede obtener la informacion del subsistema " +this.Subsystems[i]);
			} catch (BAD_OPERATION e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				report.add("No se puede obtener la informacion del subsistema " +this.Subsystems[i]);
			}
			//System.out.println("Cantidad de caracteristicas" + masterComp.get_all_characteristics().get_number_of_properties());
			//System.out.println("Estado actual del subsistema  " + masterComp.componentState().toString());
			alma.ACS.ROstringSeq baciProperty = masterComp.currentStateHierarchy();
			alma.ACSErr.Completion compl = new Completion(System.currentTimeMillis(), 0, 0, new alma.ACSErr.ErrorTrace[]{});
			alma.ACSErr.CompletionHolder complHolder = new CompletionHolder(compl);
			String[] status = baciProperty.get_sync(complHolder);
			System.out.println("Imprimiendo status");
			for(int j =0 ; j < status.length; j++){
				System.out.println(status[i]);
			}
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