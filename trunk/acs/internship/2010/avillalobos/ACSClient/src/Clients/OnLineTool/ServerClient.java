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

public class ServerClient extends OperatorClientImpl {
	
	private String clientName;
	private HashMap<String,String> AlmaStatus;
	//              Name  , Status
	private HashMap<String,String> AntennaStatus;
	//              Status,value
	private HashMap<String,String> ManagerStatus;
	//              Name  ,Status
	private HashMap<String,String> ComponentStatus;
	//              Name  ,Status
	private HashMap<String,String> SubSystemStatus;
	//              Name  ,Status
	private HashMap<String,String> ContainerStatus;
	private HashMap<String,String> oldContainerStatus;
	
	public ServerClient(String clientName){
		this.AlmaStatus = new HashMap<String,String>();
		this.AntennaStatus = new HashMap<String,String>();
		this.ManagerStatus = new HashMap<String,String>();
		this.ComponentStatus = new HashMap<String,String>();
		this.SubSystemStatus = new HashMap<String,String>();
		this.ContainerStatus = new HashMap<String,String>();
		this.oldContainerStatus = new HashMap<String,String>();
	}
	
	@Override
	public String name () {
		return clientName;
	}

	@Override
	public void acceptMessage (String sender, boolean broadcast, String message) {
		
		//pwriter.printf("%s Message: %s: %s%n", time(), sender, message);
	}

	public void receiveAlma (AlmastatusStruct[] structs, ReceiveType type) {
		// Anything that come here, must to be updated
		this.AlmaStatus.clear();
		this.AlmaStatus.put("Status", "{\"Type\":\"AlmaStatus\",\"Status\":\""+structs[0].status.toString()+"\"},");
		//AlmastatusStruct s = structs[0];
		//System.out.println(s.status.toString());
		//this.report.add("{\"Type\":\"AlmaStatus\",\"Status\":\""+s.status.toString()+"\"},");
		//pwriter.printf("%s Alma Status: %s%n", s.status);
	}

	@Override
	public void receiveManager (ServantstatusStruct[] structs, ReceiveType type) {
		this.ManagerStatus.clear();
		this.ManagerStatus.put("Status", "{\"Type\":\"ManagerStatus\",\"Status changed\":\""+structs[0].status.toString()+"\"},");
	}

	@Override
	public void receiveComponents (ComponentStruct[] structs, ReceiveType type) {
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
	}

	@Override
	public void receiveSubsystems (SubsystemStruct[] structs, ReceiveType type) {
		if(type == ReceiveType.TOTAL){
			this.SubSystemStatus.clear();
			String status = null;
			for (SubsystemStruct s : structs){
				if(s.status.length == 3){
					status = s.status[2];
				}else{
					status = s.status[1];
				}
				this.SubSystemStatus.put(s.name,"{\"Type\":\"SubsystemStatus\",\"Name\":\""+s.name+"\",\"SubSysStat\":\""+status+"\"},");
			}
		}else if(type == ReceiveType.DIFF){
			String status = null;
			for (SubsystemStruct s : structs){
					if (s.status.length == 3) {
						status = s.status[2];
					} else {
						status = s.status[1];
					}
					this.SubSystemStatus.put(s.name, "{\"Type\":\"SubsystemStatus\",\"Name\":\""+s.name+"\",\"SubSysStat\":\""+status+"\"},");				
			}
		}else{
			System.out.println("What the hell!!!!");
			// what!!!
		}
	}

	
	// Escribir codigo para cuando un container esta caido
	@Override
	public void receiveActiveContainers (NameStruct[] structs, ReceiveType type) {	
		if(type == ReceiveType.TOTAL){
			this.ContainerStatus.clear();
			for (NameStruct s : structs){
				this.ContainerStatus.put(s.name, "{\"Type\":\"ContainerStatus\",\"Name\":\""+s.name+"\",\"Status\":\"Ok\"},");
			}
			
		}else if(type == ReceiveType.DIFF){
			for (NameStruct s : structs){
				this.ContainerStatus.put(s.name, "{\"Type\":\"ContainerStatus\",\"Name\":\""+s.name+"\",\"Status\":\"Ok\"},");
			}
			
		}else{
			System.out.println("What the hell!!!!");
			// what!!!
		}
		Collection<Entry<String,String>> diff = this.oldContainerStatus.entrySet();
		for(Entry<String,String> e : diff){
			if(!this.ContainerStatus.containsKey(e.getKey()) && !this.ContainerStatus.containsValue("{\"Type\":\"ContainerStatus\",\"Name\":\""+e.getKey()+"\",\"Status\":\"Ok\"},")){
				this.ContainerStatus.put(e.getKey(), "{\"Type\":\"ContainerStatus\",\"Name\":\""+e.getKey()+"\",\"Status\":\"No such Container\"},");
			}
		}
		
		this.oldContainerStatus.clear();
		this.oldContainerStatus.putAll(this.ContainerStatus);
	}
	//protected HashSet<String> containers = new HashSet<String>();
	
	public void receiveAntennas (AntennaStruct[] structs, ReceiveType type) {
		if(type == ReceiveType.TOTAL){
			this.AntennaStatus.clear();
			for (AntennaStruct s : structs){
				this.AntennaStatus.put(s.antennaName, "{\"Type\":\"AntennaStatus\",\"Name\":\""+s.antennaName+"\",\"Array\":\""+s.arrayName+"\",\"Status\":\""+s.antennasState.toString()+"\",\"SubStatus\":\""+s.antennaSubstate.toString()+"\",\"Online\":\""+s.antennaMode+"\"},");
			}
		}else if(type == ReceiveType.DIFF){
			for (AntennaStruct s : structs){
				this.AntennaStatus.put(s.antennaName,"{\"Type\":\"AntennaStatus\",\"Name\":\""+s.antennaName+"\",\"Array\":\""+s.arrayName+"\",\"Status\":\""+s.antennasState.toString()+"\",\"SubStatus\":\""+s.antennaSubstate.toString()+"\",\"Online\":\""+s.antennaMode+"\"},");
			}
		}else{
			System.out.println("What the hell!!!!");
			// what!!!
		}
	}
	
	public LinkedList<String> getStatus(){
		LinkedList<String> report = new LinkedList<String>();
		report.addAll(generateReport(this.AlmaStatus));
		report.addAll(generateReport(this.SubSystemStatus));
		report.addAll(generateReport(this.AntennaStatus));
		report.addAll(generateReport(this.ManagerStatus));
		report.addAll(generateReport(this.ComponentStatus));
		report.addAll(generateReport(this.ContainerStatus));
		return report;
	}
	
	/*
	 * ####################################################################################################
	 * 											Generating reports
	 * ####################################################################################################
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

}
