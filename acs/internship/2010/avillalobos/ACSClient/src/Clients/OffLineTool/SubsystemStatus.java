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

/**
 * This class show the subsystem status, to do that, using the container services from the AdvancedComponentClient on
 * ACSClient, this class get the reference to each subsystem and obtain the needed information, then create a report with
 * all subsystems on JSON format to be returned throw getStatus method.
 *
 * @author Andres Villalobos, 2011 Summerjob, Ingenieria de ejecucion en Computacion e Informatica, Universidad Catolica del norte
 * @see DeveloperInfo
 *              The project  <a href="http://almasw.hq.eso.org/almasw/bin/view/JAO/OfflineToolsSummerJob">Twiki page</a> and
 *              my <a href="http://almasw.hq.eso.org/almasw/bin/view/Main/AndresVillalobosDailyLogOfflineTools">Dailylog</a> please
 *              Contact to a.e.v.r.007@gmail.com
 */
public class SubsystemStatus extends ACSClient{
	
	/**
	 * @param subsystemsList This array contains the name of subsystems that this class will give status
	 */
	private String[] subsystemsList;
	
	/**
	 * @param SubSystemsProperties This hashmap contains the references to all subsystems according to the subsystemsList.
	 * The key of this hashmap is the name of the subsystem and the value a reference to this subsystem.
	 */
	private HashMap<String,Object> SubSystemsProperties;
	
	/**
	 * @param SubSystemsStatus This hashmap contains the name of the subsystem as the key and the report on JSON format as a value
	 * and is used by AlmaStatus from Offline package to make the Alma status
	 */
	private HashMap<String,String> SubSystemsStatus;
	
	/**
	 * @param firstTiem This variable indicate if its the first status to be reported, if is not the first time, then is needed
	 * to check if some subsystems remains, and obtain this references.
	 */
	private boolean firstTime;
	
	/**
	 * This is the constructor of SubsystemStatus, here subsystemsList is initialized, SubsystemsStatus is initialized and
	 * loaded with the available information, the SubSystemsProperties is initialized and load with all the available references
	 * to the subsystems and the firsTime varialbe is set on true. Then the constructor try to get the references, if can't get 
	 * the references just display a message on the ACS Logger and continue with the next subsystem. 
	 * 
	 * @param _client The client needed to initialize ACSClient
	 */
	public SubsystemStatus(AdvancedComponentClient _client){
		super(_client);
		this.subsystemsList = getInitialSubsystems().split(",");
		this.SubSystemsStatus = new HashMap<String,String>();
		this.SubSystemsProperties = new HashMap<String,Object>();
		this.firstTime = true;
		getSubsystemsReferences();
	}
	
	/**
	 * This method get all references to the subsystems, if its unable to do that, then display a message
	 * on ACS Logger.
	 */
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

	/**
	 * This is the implementation of getStatus for SubsystemsStatus. If when this class was initialized couldn't retrieve
	 * information from any subsystems, then try to get it again, and then prepare the subsystem status to be writed later 
	 * on cacheFile
	 * 
	 * @return A list of strings that contains the status of subsystems on JSON format
	 */
	@Override
	public LinkedList<String> getStatus() {
		/*
		if(!isAcsOnline()){
			this.SubSystemsProperties.clear();
			getSubsystemsReferences();
		}
		*/
		if(this.SubSystemsProperties.size() < this.subsystemsList.length && !this.firstTime){
			getReferenceOfRemainsSubsystems();
		}
		if(this.firstTime){
			this.firstTime = false;
		}
		return getStatusOfSubsystems();
	}
	
	/**
	 * This method create the subsystem status based on the information retrieved from the references on the
	 * HashMap (SubSystemsProperties), if there is some problem here, this is reported on the ACSLogger and is displayed
	 * on the status report
	 * 
	 * @return A list of strings that contains the status of subsystems on JSON format
	 */
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
				report.add("{\"Type\":\"SubsystemStatus\",\"Name\":\""+masterComp.name()+"\",\"Status\":\""+status+"\"},");
				
				//report.add("Estado actual del subsistema  " + this.Subsystems[i] + " = " + masterComp.componentState().toString());
			} catch (BAD_OPERATION e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				report.add("{\"Type\":\"SubsystemStatus\",\"Name\":\""+subsystem+"\",\"Status\":\"Bad Operation\"},");
				writeInfoMessage("could't retrieve information from subsystem " + subsystem +" when getting status because of Bad operation exception");
				this.SubSystemsStatus.put(subsystem, "UNAVAILABLE");
			} catch (NullPointerException npe){
				report.add("{\"Type\":\"SubsystemStatus\",\"Name:\""+subsystem+"\",\"Status\":\"Unable to connect with the subsystem " + subsystem + "\"},");
				writeInfoMessage("Unable to get information from subsystem " + subsystem + " because there is not reference to this subsystem");
				this.SubSystemsStatus.put(subsystem, "UNAVAILABLE");
			} catch (Exception e){
				report.add("{\"Type\":\"SubsystemStatus\",\"Name:\""+subsystem+"\",\"Status\":\"Unable to reconnect with manager \"},");
				writeInfoMessage("Unable to get information from subsystem " + subsystem);
				this.SubSystemsStatus.put(subsystem, "UNAVAILABLE");
			}
		}
		return report;
	}
	
	/**
	 * This method charge on SubsystemsProperties the remains references to the subsystems, if something happen
	 * is reported on ACSLogger
	 */
	private void getReferenceOfRemainsSubsystems(){
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
	
	/**
	 * This method return the name of subsystems where the status is needed.
	 * 
	 * @return A string with the name of the subsystems separated by ","
	 */
	private String getInitialSubsystems(){
		return "CONTROL_MASTER_COMP,ARCHIVE_MASTER_COMP,SCHEDULING_MASTER_COMP,TELCAL_MASTER_COMP,PIPELINE_SCIENCE_MASTER_COMP,PIPELINE_QUICKLOOK_MASTER_COMP";
	}

	/**
	 * This method return the array of subsystems
	 * 
	 * @return An array of strings where each block contains the name of on subsystem
	 */
	public String[] getSubsystems() {
		return subsystemsList;
	}

	/**
	 * This method return the status of the subsystems on a hashmap, the key of this hashmap is equal to the name of the 
	 * subsystem, and the value is equal to the status of the subsystem, but just the status, is not on JSON format
	 * 
	 * @return
	 */
	public HashMap<String,String> getSubSystemsStatus() {
		return SubSystemsStatus;
	}
	
}
