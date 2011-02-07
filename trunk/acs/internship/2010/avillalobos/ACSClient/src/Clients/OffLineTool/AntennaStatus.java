package Clients.OffLineTool;

import java.util.HashMap;
import java.util.LinkedList;

import Clients.ACSClient;
import alma.Control.Antenna;
import alma.Control.AntennaHelper;
import alma.ControlExceptions.INACTErrorEx;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.AdvancedComponentClient;

/**
 * This class is responsible to get the offline antenna information, to do that, this class
 * obtain the antenna component reference, using the client from ACSClient, get the containerservices
 * and obtain a reference to antenna, this references is stored to reuse during the execution of the
 * program.
 * 
 * @author Andres Villalobos, 2011 Summerjob, Ingenieria de ejecucion en Computacion e Informatica, Universidad Catolica del norte
 * @see The project twiki http://almasw.hq.eso.org/almasw/bin/view/JAO/OfflineToolsSummerJob
 * 		My dailylog http://almasw.hq.eso.org/almasw/bin/view/Main/AndresVillalobosDailyLogOfflineTools
 * 		Contact to a.e.v.r.007@gmail.com
 */
public class AntennaStatus extends ACSClient{
	
	/**
	 * @param antennasName This array contains the name of the antennas to be displayed on the Antenna status
	 */
	private String antennasName[];
	
	/**
	 * @param AntennaReferences This HashMap contains the references to the antenna component, the key of the 
	 * hashmap is equal to the antenna name, because there is not 2 antennas with the same name.
	 */
	private HashMap<String,Antenna> AntennaReferences;
	
	/**
	 * @param isFirstTime This variable indicate if is the first time where the status will display the status,
	 * because if not the first time, we need to try to recover a new references, if this not exist.
	 */
	private boolean isFirstTime;
	
	/**
	 * @param antennaActiveMQMessageListener This parameter allow to get more information from antennas
	 */
	private AntennaActiveMQMessageListener antennaActiveMQMessageListener;
	
	/**
	 * This is the constructor of the antenna status, here obtain the name of all antennas 
	 * 
	 * @param _client The client needed by ACSClient
	 * @param _brokerURL The broker url to ActvieMQ server
	 */
	public AntennaStatus(AdvancedComponentClient _client,String _brokerURL){
		super(_client);
		String antennas = getAllAntennas();
		this.antennasName = antennas.split(",");
		this.AntennaReferences = new HashMap<String,Antenna>();
		getAntennaReferences();
		this.isFirstTime = true;
		this.antennaActiveMQMessageListener = new AntennaActiveMQMessageListener(_brokerURL);
		
	}

	/**
	 * This is the implementation of getStatus for Antenna status, this implementation try to get the
	 * references for antennas, if there is not antennas, then try to get all references again, if there is
	 * just some references, then try to get the remaining references and in both cases, return the status of
	 * all references, if this not exist, display a message saying which was the problem
	 * 
	 * @return A linked list on JSON format that display the needed information for antennas
	 */
	@Override
	public LinkedList<String> getStatus() {
		if(this.AntennaReferences.size() < this.antennasName.length && !this.isFirstTime){
			System.out.println("tratando de obtener nuevas referencias de las antennas ");
			getReferencesOfRemainingAntennas();	
		}
		if(this.isFirstTime){
			this.isFirstTime = false;
		}
		
		return getStatusFromAntennas();
	}
	
	/**
	 * This method try to get references for the needed antennas, if is unable to get this information
	 * then display a message on ACS Logger informing the exception
	 */
	private void getAntennaReferences(){
		Antenna antenna = null;
		for(String antennaName : this.antennasName){
			try {
				antenna = AntennaHelper.narrow(this.client.getContainerServices().getComponent("CONTROL/"+antennaName));
				this.AntennaReferences.put(antennaName, antenna);
				//this.AntennaReferences.set(i, antenna);
			} catch (AcsJContainerServicesEx e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				writeWarningMessage("Unable to get reference to antenna " + antennaName +" because of AcsJContainerServicesEx");
			} catch (NullPointerException err){
				writeWarningMessage("Unable to get reference to antenna " + antennaName + " null pointer exception");
			}
		}
	}
	
	/**
	 * This method get the information from Components and ActiveMQ and add on one report for antenna status.
	 * @return The all status of antennas (Components and ActiveMQ)
	 */
	private LinkedList<String> getStatusFromAntennas(){
		LinkedList<String> report = new LinkedList<String>();
		report.addAll(getStatusFromAntennaComponent());
		report.addAll(getStatusFromActiveMQ());
		return report;
	}
	
	/**
	 * This method get all information from antenna components and add this information on json format on a report
	 * to be returned
	 * 
	 * @return A list of string with antenna status on json format
	 */
	private LinkedList<String> getStatusFromAntennaComponent(){
		LinkedList<String> report = new LinkedList<String>();
		Antenna antenna = null;
		int i = 0;
		for(String antennaName : this.antennasName){
			antenna = this.AntennaReferences.get(antennaName);
			try {
				String Name = antenna.getName();
				String Array = getArray(antenna);
				String Status = getStatus(antenna);
				String Substatus = getSubStatus(antenna);
				String Online = getOnline(antenna);
				report.add("{\"Type\":\"AntennaStatus\",\"Name\":\""+Name+"\",\"Array\":\""+Array+"\",\"Status\":\""+Status+"\",\"SubStatus\":\""+Substatus+"\",\"Online\":\""+Online+"\"},");
			} catch (INACTErrorEx e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				report.add("{\"Type\":\"AtennaError\",\"Name\":\"" + antennaName + "\", \"Description\":\"Unable to get information from " + antennaName + " because this references doesn't have this information\"},");
				writeInfoMessage("INACTErrorEx when try to get information from antenna " + antennaName);
			} catch (NullPointerException npe) {
				report.add("{\"Type\":\"AtennaError\",\"Name\":\"" + antennaName + "\",\"Description\":\"Unable to get information from " + antennaName + " because there is not reference\"},");
				writeInfoMessage("Null pointer exception when trying to get information from atenna " + antennaName);
			} catch (org.omg.CORBA.OBJECT_NOT_EXIST ONE){
				report.add("{\"Type\":\"AtennaError\",\"Name\":\"" + antennaName + "\",\"Description\":\"Unable to get information from " + antennaName + " probably the container is down\"},");
				writeInfoMessage("Corba Object not exist for atenna " + antennaName + " probably container is down");
			} catch (org.omg.CORBA.TRANSIENT T){
				report.add("{\"Type\":\"AtennaError\",\"Name\":\"" + antennaName + "\",\"Description\":\"Unable to get information from " + antennaName + " unable to connect to the antenna, probably the container is down\"},");
				writeInfoMessage("Could'n connect to manager for atenna " + antennaName + " probably container is down");
			}
			i++;
		}
		return report;
	}
	
	/**
	 * This method get the information from antennaActiveMQMessageListener, this information must to be on JSON format
	 * and contain the relevant information for antennas
	 * 
	 * @return A List of antenna status from ActiveMQ on JSON format
	 */
	private LinkedList<String> getStatusFromActiveMQ(){
		return this.antennaActiveMQMessageListener.getReport();
	}
	
	/**
	 * This method try to get the information for the remaining antennas, to do that, use the key on the HashMap
	 * that contains the antenna references, and get the references, if its unable to use this reference, then 
	 * this references not exist, so try to get the reference to this antenna.
	 */
	private void getReferencesOfRemainingAntennas(){
		Antenna antenna = null;
		for(String antennaName : this.antennasName){
			antenna = this.AntennaReferences.get(antennaName);
			try {
				antenna.getName();
			} catch (INACTErrorEx e) {
				// TODO Auto-generated catch block
				writeInfoMessage("INACTErrorEx when try to get information from antenna " + antennaName);
				this.AntennaReferences.remove(antennaName);
				try {
					this.AntennaReferences.put(antennaName, antenna = AntennaHelper.narrow(this.client.getContainerServices().getComponent("CONTROL/"+antennaName)));
				} catch (AcsJContainerServicesEx e1) {
					// TODO Auto-generated catch block
					writeInfoMessage("Unable to get reference to antenna " + antennaName + " because of AcsJContainerServicesException");
				}
			} catch (NullPointerException npe) {
				writeInfoMessage("Null pointer exception when trying to get information from antenna " + antennaName);
				this.AntennaReferences.remove(antennaName);
				this.AntennaReferences.remove(antennaName);
				try {
					this.AntennaReferences.put(antennaName, antenna = AntennaHelper.narrow(this.client.getContainerServices().getComponent("CONTROL/"+antennaName)));
				} catch (AcsJContainerServicesEx e1) {
					// TODO Auto-generated catch block
					writeInfoMessage("Unable to get reference to antenna " + antennaName + " because null pointer exception");
				}
			}
		}
	}
	
	/**
	 * This method return the mode (Online or Offline) of the antenna
	 * 
	 * @param antenna The reference to the antenna that mode is needed
	 * @return The mode of the antenna
	 */
	public String getOnline(Antenna antenna){
		return antenna.getState().toString();
	}
	
	/**
	 * This method return the array where the antenna was assigned
	 * 
	 * @param antenna The reference to the antenna where array is needed
	 * @return The name of the array where this antenna belong
	 */
	public String getArray(Antenna antenna){
		try {
			return antenna.getArray();
		} catch (INACTErrorEx e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "Error";
		}
	}
	
	/**
	 * This method return the status of the antenna
	 * 
	 * @param antenna The reference to the antenna where status is needed
	 * @return The status of the antenna
	 */
	public String getStatus(Antenna antenna){
		return antenna.componentState().toString();
	}
	
	/**
	 * This method return the subsystem status for a antenna. This method must to be chequed, because the implementation
	 * make me some doubts, actually i'm using the error message to display something here, and if there is not erro, then 
	 * i'm displaying the "NoError" message 
	 * 
	 * @param antenna The reference to the antenna where sub system status is needed
	 * @return
	 */
	public String getSubStatus(Antenna antenna){
		if(!antenna.inErrorState())
			return "NoError";
		else{
			return antenna.getErrorMessage();
		}
	}
	
	/**
	 * This method return the name of all antennas that this client will display
	 * 
	 * @return A string with the name of all antennas that this client will display, separated by ","
	 */
	public String getAllAntennas(){
		return "DV01,DV02,PM03";
	}
}