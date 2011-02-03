package Clients.OffLineTool;

import java.util.HashMap;
import java.util.LinkedList;

import Clients.ACSClient;
import alma.Control.Antenna;
import alma.Control.AntennaHelper;
import alma.ControlExceptions.INACTErrorEx;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.AdvancedComponentClient;

public class AntennaStatus extends ACSClient{
	
	private String antennasName[];
	private HashMap<String,Antenna> AntennaReferences;
	private boolean isFirstTime;
	private AntennaActiveMQMessageListener antennaActiveMQMessageListener;
	
	public AntennaStatus(AdvancedComponentClient _client,String _brokerURL){
		super(_client);
		String antennas = getAllAntennas();
		this.antennasName = antennas.split(",");
		this.AntennaReferences = new HashMap<String,Antenna>();
		getAntennaReferences();
		this.isFirstTime = true;
		this.antennaActiveMQMessageListener = new AntennaActiveMQMessageListener(_brokerURL);
		
	}

	@Override
	public LinkedList<String> getStatus() {
		if(this.AntennaReferences.size() < this.antennasName.length && !this.isFirstTime){
			System.out.println("tratando de obtener nuevas referencias de las antennas ");
			getReferencesOfLeftAntennas();	
		}
		if(this.isFirstTime){
			this.isFirstTime = false;
		}
		
		return getStatusFromAntennas();
	}
	
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
	
	private LinkedList<String> getStatusFromAntennas(){
		LinkedList<String> report = new LinkedList<String>();
		report.addAll(getStatusFromAntennaComponent());
		report.addAll(getStatusFromActiveMQ());
		return report;
	}
	
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
	
	private LinkedList<String> getStatusFromActiveMQ(){
		return this.antennaActiveMQMessageListener.getReport();
	}
	
	private void getReferencesOfLeftAntennas(){
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
	
	public String getOnline(Antenna antenna){
		return antenna.getState().toString();
	}
	
	public String getArray(Antenna antenna){
		try {
			return antenna.getArray();
		} catch (INACTErrorEx e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "Error";
		}
	}
	
	public String getStatus(Antenna antenna){
		return antenna.componentState().toString();
	}
	
	public String getSubStatus(Antenna antenna){
		if(!antenna.inErrorState())
			return "NoError";
		else{
			return antenna.getErrorMessage();
		}
	}
	
	public String getAllAntennas(){
		return "DV01,DV02,PM03";
	}
}