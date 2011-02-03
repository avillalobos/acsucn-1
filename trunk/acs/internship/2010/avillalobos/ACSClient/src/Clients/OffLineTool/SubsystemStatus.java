package Clients.OffLineTool;

import java.util.HashMap;
import java.util.LinkedList;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.Object;

import Clients.ACSClient;
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
	private boolean firstTime;
	
	public SubsystemStatus(AdvancedComponentClient _client){
		super(_client);
		this.subsystemsList = getInitialSubsystems().split(",");
		this.SubSystemsStatus = new HashMap<String,String>();
		this.SubSystemsProperties = new HashMap<String,Object>();
		this.firstTime = true;
		getSubsystemsReferences();
	}
	
	private void getSubsystemsReferences(){
		for(String subsystem : this.subsystemsList){
			try {
				this.SubSystemsProperties.put(subsystem,this.client.getContainerServices().getComponent(subsystem));
			} catch (AcsJContainerServicesEx e) {
				writeInfoMessage("could't retrieve information from subsystem " + subsystem +" because of AcsJContainerServicesEx exception");
			} catch (BAD_OPERATION e) {
				writeInfoMessage("could't retrieve information from subsystem " + subsystem +" because of Bad operation exception");				
			} catch (Exception e){
				System.out.println("Exception capturada " + e.getCause());
			}
		}
	}

	@Override
	public LinkedList<String> getStatus() {
		if(this.SubSystemsProperties.size() < this.subsystemsList.length && !this.firstTime){
			getReferenceOfLeftSubsystems();
		}
		if(this.firstTime){
			this.firstTime = false;
		}
		return getStatusOfSubsystems();
	}
	
	private LinkedList<String> getStatusOfSubsystems(){
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
				//report.add("{\"Type\":\"SubsystemStatus\",\"Name\":\""+masterComp.name()+"\",\"CompStat\":\""+masterComp.componentState().toString()+"\",\"SubSysStat\":\""+status+"\"},");
				report.add("{\"Type\":\"SubsystemStatus\",\"Name\":\""+masterComp.name()+"\",\"SubSysStat\":\""+status+"\"},");
				
				//report.add("Estado actual del subsistema  " + this.Subsystems[i] + " = " + masterComp.componentState().toString());
			} catch (BAD_OPERATION e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				report.add("{\"Type\":\"SubsystemError\",\"Name\":\""+subsystem+"\",\"Description\":\"Bad Operation\"},");
				writeInfoMessage("could't retrieve information from subsystem " + subsystem +" when getting status because of Bad operation exception");
				this.SubSystemsStatus.put(subsystem, "UNAVAILABLE");
			} catch (NullPointerException npe){
				report.add("{\"Type\":\"SubsystemError\",\"Name:\""+subsystem+"\",\"Description\":\"Unable to connect with the subsystem " + subsystem + "\"},");
				writeInfoMessage("Unable to get information from subsystem " + subsystem + " because there is not reference to this subsystem");
				this.SubSystemsStatus.put(subsystem, "UNAVAILABLE");
			} catch (Exception e){
				report.add("{\"Type\":\"SubsystemError\",\"Name:\""+subsystem+"\",\"Description\":\"Unable to reconnect with manager \"},");
				writeInfoMessage("Unable to get information from subsystem " + subsystem);
				this.SubSystemsStatus.put(subsystem, "UNAVAILABLE");
			}
		}
		return report;
	}
	
	private void getReferenceOfLeftSubsystems(){
		for(String subsystem : this.subsystemsList){
			try {
				if(this.SubSystemsProperties.get(subsystem) == null){
					this.SubSystemsProperties.put(subsystem,this.client.getContainerServices().getComponent(subsystem));
				}
				this.SubSystemsProperties.containsKey(subsystem);
			} catch (NullPointerException npe) {
				try {
					this.SubSystemsProperties.put(subsystem,this.client.getContainerServices().getComponent(subsystem));
				} catch (AcsJContainerServicesEx e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					writeInfoMessage("could't retrieve information from subsystem " + subsystem +" because of AcsJContainerServicesEx exception tying to get the subsystem reference again");
				}
			} catch (AcsJContainerServicesEx e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (Exception e){
				
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