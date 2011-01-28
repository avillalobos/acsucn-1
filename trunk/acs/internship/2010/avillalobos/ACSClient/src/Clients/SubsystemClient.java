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
		for(String subsystem : this.Subsystems){
			try {
				ref = getContainerServices().getComponent(subsystem);
				masterComp = MasterComponentHelper.narrow(ref);
				alma.ACS.ROstringSeq baciProperty = masterComp.currentStateHierarchy();
				alma.ACSErr.Completion compl = new Completion(System.currentTimeMillis(), 0, 0, new alma.ACSErr.ErrorTrace[]{});
				alma.ACSErr.CompletionHolder complHolder = new CompletionHolder(compl);
				String[] status = baciProperty.get_sync(complHolder);
				report.add("{Type:'Subsystem',Name:'"+subsystem+"',CompStat:'"+masterComp.componentState().toString()+"',SubSysStat:'"+status[1]+"'}");
				//report.add("Estado actual del subsistema  " + this.Subsystems[i] + " = " + masterComp.componentState().toString());
			} catch (AcsJContainerServicesEx e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				report.add("No se puede obtener la informacion del subsistema " +subsystem);
			} catch (BAD_OPERATION e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				report.add("No se puede obtener la informacion del subsistema " +subsystem);
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