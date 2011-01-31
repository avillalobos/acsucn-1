package Clients;

import java.util.HashMap;
import java.util.LinkedList;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.Object;
import alma.ACS.ROstringSeq;

import alma.ACS.MasterComponent;
import alma.ACS.MasterComponentHelper;
import alma.ACSErr.Completion;
import alma.ACSErr.CompletionHolder;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.AdvancedComponentClient;


public class SubsystemStatus extends ACSClient{
	
	private String[] subsystemsList;
	private HashMap<String,Object> SubSystemsProperties;
	private HashMap<String,String> SubSystemsStatus;
	
	public SubsystemStatus(AdvancedComponentClient _client){
		super(_client);
		this.subsystemsList = getInitialSubsystems().split(",");
		this.SubSystemsStatus = new HashMap<String,String>();
		this.SubSystemsProperties = new HashMap<String,Object>();
		getSubsystemsReferences();
	}
	
	private void getSubsystemsReferences(){
		for(String subsystem : this.subsystemsList){
			try {
				this.SubSystemsProperties.put(subsystem,this.client.getContainerServices().getComponent(subsystem));
			} catch (AcsJContainerServicesEx e) {
			} catch (BAD_OPERATION e) {
			}
		}
	}

	@Override
	public LinkedList<String> getStatus() {
		if(this.SubSystemsProperties.size() < this.subsystemsList.length){
			getReferenceOfLeftSubsystems();
		}
		LinkedList<String> report = new LinkedList<String>();
		MasterComponent masterComp = null;
		Object reference = null;
		for(String subsystem : this.subsystemsList){
			try {
				reference = this.SubSystemsProperties.get(subsystem);
				masterComp = MasterComponentHelper.narrow(reference);
				ROstringSeq baciProperty = masterComp.currentStateHierarchy();
				Completion compl = new Completion(System.currentTimeMillis(), 0, 0, new alma.ACSErr.ErrorTrace[]{});
				CompletionHolder complHolder = new CompletionHolder(compl);
				String[] statusList = baciProperty.get_sync(complHolder);
				String status = null;
				if(statusList.length == 3){
					status = statusList[2];
				}else{
					status = statusList[1];
				}
				this.SubSystemsStatus.put(subsystem, status);
				report.add("{Type:'Subsystem',Name:'"+masterComp.name()+"',CompStat:'"+masterComp.componentState().toString()+"',SubSysStat:'"+status+"'}");
				
				//report.add("Estado actual del subsistema  " + this.Subsystems[i] + " = " + masterComp.componentState().toString());
			} catch (BAD_OPERATION e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				report.add("{Type:'SubsystemError',Name:'"+subsystem+"',Description:'Bad Operation'}");
			} catch (NullPointerException npe){
				report.add("{Type:'SubsystemError',Name:'"+subsystem+"',Description:'Doesn't exist reference to the subsystem " + subsystem + "'}");
			}
		}
		
		return report;
	}
	
	private void getReferenceOfLeftSubsystems(){
		for(String subsystem : this.subsystemsList){
			try {
				this.SubSystemsProperties.containsKey(subsystem);
			} catch (NullPointerException npe) {
				try {
					this.SubSystemsProperties.put(subsystem,this.client.getContainerServices().getComponent(subsystem));
				} catch (AcsJContainerServicesEx e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		}
	}
	
	private String getInitialSubsystems(){
		return "CONTROL_MASTER_COMP,ARCHIVE_MASTER_COMP,SCHEDULING_MASTER_COMP,TELCAL_MASTER_COMP,PIPELINE_SCIENCE_MASTER_COMP,PIPELINE_QUICKLOOK_MASTER_COMP";
	}

	public String[] getSubsystems() {
		return subsystemsList;
	}

	public HashMap<String,String> getSubSystemsStatus() {
		return SubSystemsStatus;
	}
	
}