package Clients.OnLineTool;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import alma.exec.AlmastatusStruct;
import alma.exec.AntennaStruct;
import alma.exec.ComponentStruct;
import alma.exec.NameStruct;
import alma.exec.ReceiveType;
import alma.exec.ServantstatusStruct;
import alma.exec.SubsystemStruct;
import alma.exec.operatorbase.client.teamwork.auxiliary.OperatorClientImpl;

/**
 * This class is a listener of opserver, so the opserver send information to this listener and the listener
 * save the information on the corresponding HashMap, is there is not new information, then the status to display
 * is the same of the last iteration
 * 
 * @author mschilling
 * @author Andres Villalobos, 2011 Summerjob, Ingenieria de ejecucion en Computacion e Informatica, Universidad Catolica del norte
 * @see DeveloperInfo
 *              The project  <a href="http://almasw.hq.eso.org/almasw/bin/view/JAO/OfflineToolsSummerJob">Twiki page</a> and
 *              my <a href="http://almasw.hq.eso.org/almasw/bin/view/Main/AndresVillalobosDailyLogOfflineTools">Dailylog</a> please
 *              Contact to a.e.v.r.007@gmail.com
 */
public class ServerClient extends OperatorClientImpl {
	
	/**
	 * @param clientName This variable indicate the name with the client will be registered
	 */
	private String clientName;
	
	/**
	 * @param AlmaStatus This hashmap save the information of Alma from opserver, the key of this
	 * hashmap is "Status" and the value is the Status of Alma software but on json format
	 */
	private HashMap<String,String> AlmaStatus;
	
	/**
	 * @param AntennaStatus This hashmap save the information of all antennas, this information come from
	 * opserver, the key of this hashmap is the name of the antenna and the value is the status of the antenna
	 * on json format
	 */
	//              Name  , Status
	private HashMap<String,String> AntennaStatus;
	
	/**
	 * @param ManagerStatus This hashmap contains the information of manager status, this information come from 
	 * opserver, the key of this hashmap is "Status" and the value represent the status of the manager (ACS) on
	 * JSON format
	 */
	//              Status,value
	private HashMap<String,String> ManagerStatus;
	
	/**
	 * @param ComponentStatus This hashmap contains the information of ACS component, this information come from 
	 * opserver, the key of this hashmap is the name of component and the value represent the status of those components on
	 * JSON format
	 */
	//              Name  ,Status
	private HashMap<String,String> ComponentStatus;
	
	/**
	 * @param SubSystemStatus This hashmap contains the information of subsystems, this information come from 
	 * opserver, the key of this hashmap is the name of the subsystem and the value represent the status of each subsystems on
	 * JSON format
	 */
	//              Name  ,Status
	private HashMap<String,String> SubSystemStatus;
	
	/**
	 * @param ContainerStatus This hashmap contains the information of all containers, this information come from 
	 * opserver, the key of this hashmap is container's name and the value represent the status of each container on
	 * JSON format
	 */
	//              Name  ,Status
	private HashMap<String,String> ContainerStatus;
	
	/**
	 * @param oldContainerStatus This variable is used to make the diff between the new container information and old
	 * container information, if there is not changed, then no containers is down, if on the new container information 
	 * there is less containers than old container status, then a container is down, if on new container information 
	 * there is more containers than the old container status, then there is a new container.
	 */
	private HashMap<String,String> oldContainerStatus;
	
	/**
	 * This is the constructor of ServerClient, and just receive the name that will identify this client. On the constructor
	 * all the HashMaps has been initialized and are ready to be used.
	 * 
	 * @param clientName The name of the client
	 */
	public ServerClient(String clientName){
		this.AlmaStatus = new HashMap<String,String>();
		this.AntennaStatus = new HashMap<String,String>();
		this.ManagerStatus = new HashMap<String,String>();
		this.ComponentStatus = new HashMap<String,String>();
		this.SubSystemStatus = new HashMap<String,String>();
		this.ContainerStatus = new HashMap<String,String>();
		this.oldContainerStatus = new HashMap<String,String>();
	}
	
	/**
	 * This method indicate the name of the client
	 * 
	 * @param A string with the name of this client
	 */
	@Override
	public String name () {
		return clientName;
	}

	/**
	 * This method override the acceptMessage from OperatorClientImpl and receive all the new information from 
	 * opserver
	 */
	@Override
	public void acceptMessage (String sender, boolean broadcast, String message) {
		
		//pwriter.printf("%s Message: %s: %s%n", time(), sender, message);
	}

	/**
	 * This method is override from OperatorClientImpl and receive all the new information from opserver related to Alma status
	 * 
	 * @param structs Contains the alma status sent from opserver
	 * @param type The type of message received
	 */
	public synchronized void receiveAlma (AlmastatusStruct[] structs, ReceiveType type) {
		System.out.println("\n\t ## Incomming message from opserver to Alma status ## \n");
		// Anything that come here, must to be updated
		this.AlmaStatus.clear();
		this.AlmaStatus.put("Status", "{\"Type\":\"AlmaStatus\",\"Name\":\"Alma Status\",\"Status\":\""+structs[0].status.toString()+"\"},");
	}

	/**
	 * The manager status represent the ACS status, so, if i can't connect with manager, represent that ACS is down
	 * 
	 * @param structs Contains the manager status sent from opserver
	 * @param type The type of message received 
	 */
	@Override
	public synchronized void receiveManager (ServantstatusStruct[] structs, ReceiveType type) {
		System.out.println("\n\t ## Incomming message from opserver to Manager status ## \n");
		this.ManagerStatus.clear();
		this.ManagerStatus.put("Status", "{\"Type\":\"ACSStatus\",\"Name\":\"ACS Status\",\"Status\":\""+structs[0].status.toString()+"\"},");
	}

	/**
	 * This method receive the new information from opserver related to the component status
	 * 
	 * @param structs Contains the component status sent from opserver
	 * @param type The type of message received
	 */
	@Override
	public synchronized void receiveComponents (ComponentStruct[] structs, ReceiveType type) {
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\n\t ## Incomming message from opserver to Components Status ## \n");
		if(type == ReceiveType.TOTAL){
			this.ComponentStatus.clear();
			for (ComponentStruct s : structs){
				this.ComponentStatus.put(s.name, "{\"Type\":\"ComponentStatus\",\"Name\":\""+s.name+"\",\"Status\":\""+s.componentState.toString()+"\",\"SubStatus\":\""+s.substatus+"\"},");
			}
		}else if(type == ReceiveType.DIFF){
			for (ComponentStruct s : structs){
				this.ComponentStatus.put(s.name,"{\"Type\":\"ComponentStatus\",\"Name\":\""+s.name+"\",\"Status\":\""+s.componentState.toString()+"\",\"SubStatus\":\""+s.substatus+"\"},");
			}
		}else{
			System.out.println("What the hell!!!!");
			// what!!!
		}
		notify();
	}

	/**
	 * This method receive the information from opserver about subsystems status
	 * 
	 * @param structs Contains the subsystems status sent from opserver
	 * @param type The type of message received
	 */
	@Override
	public synchronized void receiveSubsystems (SubsystemStruct[] structs, ReceiveType type) {
		System.out.println("\n\t ## Incomming message from opserver to subsystems status ## \n");
		if(type == ReceiveType.TOTAL){
			this.SubSystemStatus.clear();
			String status = null;
			for (SubsystemStruct s : structs){
				if(s.status.length == 3){
					status = s.status[2];
				}else{
					status = s.status[1];
				}
				this.SubSystemStatus.put(s.name,"{\"Type\":\"SubsystemStatus\",\"Name\":\""+s.name+"\",\"Status\":\""+status+"\"},");
			}
		}else if(type == ReceiveType.DIFF){
			String status = null;
			for (SubsystemStruct s : structs){
					if (s.status.length == 3) {
						status = s.status[2];
					} else {
						status = s.status[1];
					}
					this.SubSystemStatus.put(s.name, "{\"Type\":\"SubsystemStatus\",\"Name\":\""+s.name+"\",\"Status\":\""+status+"\"},");				
			}
		}else{
			System.out.println("What the hell!!!!");
			// what!!!
		}
	}

	/**
	 * This method receive the infomation from opserver related to containers status. The opserver send information about active containers
	 * so when a container down, then doesnt receive information about this container, so to can show when a container is down, you must 
	 * to store the pass status of all containers and if the new information contains less information, then the containers that on the
	 * past interations exist and now no is a fall container. 
	 * 
	 * @param structs Contains the containers status sent from opserver
	 * 
	 */
	@Override
	public synchronized void receiveActiveContainers (NameStruct[] structs, ReceiveType type) {
		System.out.println("\n\t ## Incomming message from opserver to Active Containers ## \n");
		if (type == ReceiveType.TOTAL) {
			this.ContainerStatus.clear();
			for (NameStruct s : structs) {
				this.ContainerStatus.put(s.name,"{\"Type\":\"ContainerStatus\",\"Name\":\"" + s.name+ "\",\"Status\":\"Ok\"},");
			}

		} else if (type == ReceiveType.DIFF) {
			for (NameStruct s : structs) {
				this.ContainerStatus.put(s.name,"{\"Type\":\"ContainerStatus\",\"Name\":\"" + s.name+ "\",\"Status\":\"Ok\"},");
			}

		} else {
			System.out.println("What the hell!!!!");
			// what!!!
		}
		Collection<Entry<String, String>> diff = this.oldContainerStatus.entrySet();
		for (Entry<String, String> e : diff) {
			if (!this.ContainerStatus.containsKey(e.getKey()) && !this.ContainerStatus.containsValue("{\"Type\":\"ContainerStatus\",\"Name\":\"" + e.getKey() + "\",\"Status\":\"Ok\"},")) {
				this.ContainerStatus.put(e.getKey(),"{\"Type\":\"ContainerStatus\",\"Name\":\""	+ e.getKey() + "\",\"Status\":\"No such Container\"},");
			}
		}

		this.oldContainerStatus.clear();
		this.oldContainerStatus.putAll(this.ContainerStatus);
	}
	
	/**
	 * This method receive information from opserver related to antenna status
	 * 
	 * @param structs Contains the antenna status sent from opserver
	 * @param type
	 */
	public synchronized void receiveAntennas (AntennaStruct[] structs, ReceiveType type) {
		System.out.println("\n\t ## Incomming message from opserver to Antenna Status ## \n");
		if (type == ReceiveType.TOTAL) {
			this.AntennaStatus.clear();
			for (AntennaStruct s : structs) {
				this.AntennaStatus.put(s.antennaName,"{\"Type\":\"AntennaStatus\",\"Name\":\"" + s.antennaName + "\",\"Array\":\"" + s.arrayName + "\",\"Status\":\"" + s.antennasState.toString() + "\",\"SubStatus\":\"" + s.antennaSubstate.toString() + "\",\"Online\":\"" + s.antennaMode + "\"}");
			}
		} else if (type == ReceiveType.DIFF) {
			for (AntennaStruct s : structs) {
				this.AntennaStatus.put(s.antennaName,"{\"Type\":\"AntennaStatus\",\"Name\":\"" + s.antennaName + "\",\"Array\":\"" + s.arrayName + "\",\"Status\":\"" + s.antennasState.toString() + "\",\"SubStatus\":\"" + s.antennaSubstate.toString() + "\",\"Online\":\"" + s.antennaMode + "\"}");
			}
		} else {
			System.out.println("What the hell!!!!");
			// what!!!
		}
	}
	
	/*
	public LinkedList<String> getStatus(){
		LinkedList<String> report = new LinkedList<String>();
		report.addAll(generateReport(this.ManagerStatus));
		report.addAll(generateReport(this.AlmaStatus));
		report.addAll(generateReport(this.SubSystemStatus));
		report.addAll(generateReport(this.AntennaStatus));
		report.addAll(generateReport(this.ComponentStatus));
		report.addAll(generateReport(this.ContainerStatus));
		return report;
	}
	*/
	
	/*
	 * ####################################################################################################
	 * 											Generating reports
	 * ####################################################################################################
	 */
	
	/**
	 * This method generate a report from a specific HashMap, so each of the HashMaps representing ACSStatus, AlmaStatus, AntennaStatus, ContainerStatus and SubsystemStatus 
	 * can be passed as a parameter and will return a linkedlist of Status on json format.
	 * 
	 * @param status A hashmap wich status information
	 * @return A linked list with strings representing information on json format
	 */
	private LinkedList<String> generateReport(HashMap<String,String> status){
		// Container Status on 
		Collection<Entry<String,String>> set = status.entrySet();
		LinkedList<String> report = new LinkedList<String>();
		for(Entry<String,String> e : set){
			report.add(e.getValue());
		}
		return report;
	}
	
	/**
	 * This method recieve as parameter the name of the system to monitor and return a linked list 
	 * with this information 
	 * 
	 * @param NameOfStatus Ths kind of status that you need
	 * @return A linked list with the needed information
	 */
	public LinkedList<String> getStatusList(String NameOfStatus){
		if(NameOfStatus.equalsIgnoreCase("ACSStatus")){
			return generateReport(this.ManagerStatus);
		}else if(NameOfStatus.equalsIgnoreCase("AlmaStatus")){
			return generateReport(this.AlmaStatus);
		}else if(NameOfStatus.equalsIgnoreCase("AntennaStatus")){
			return generateReport(this.AntennaStatus);
		}else if(NameOfStatus.equalsIgnoreCase("ContainerStatus")){
			return generateReport(this.ContainerStatus);
		}else if(NameOfStatus.equalsIgnoreCase("CorbaStatus")){
			LinkedList<String> failed = new LinkedList<String>();
			failed.add("{\"Type\":\""+NameOfStatus+"Error\",\"Description\":\"Unable to get information to the requested type\"},");
			return failed;
		}else if(NameOfStatus.equalsIgnoreCase("SubsystemStatus")){
			return generateReport(this.SubSystemStatus);
		}else{
			LinkedList<String> failed = new LinkedList<String>();
			failed.add("{\"Type\":\""+NameOfStatus+"Error\",\"Description\":\"Unable to get information to the requested type\"},");
			return failed;
		}
	}

	/**
	 * This method return the entire information of antenna received by this listener
	 * @return A hashmap with the antenna information, the key of this hashmap is equal to the name o the
	 * antenna and the value have the status on json format
	 */
	public HashMap<String, String> getAntennaStatus() {
		return this.AntennaStatus;
	}
	
	/**
	 * This method clear all the hashmaps
	 */
	public void CleanAllInformation(){
		this.AlmaStatus.clear();
		this.AntennaStatus.clear();
		this.ComponentStatus.clear();
		this.ContainerStatus.clear();
		this.ManagerStatus.clear();
		this.SubSystemStatus.clear();
		this.oldContainerStatus.clear();
	}

}
