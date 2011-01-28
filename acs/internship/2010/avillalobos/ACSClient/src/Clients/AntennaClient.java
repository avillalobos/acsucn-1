package Clients;

import java.util.LinkedList;
import java.util.logging.Logger;

import alma.ACS.ACSComponent;
import alma.ACS.ACSComponentHelper;
import alma.Control.Antenna;
import alma.Control.AntennaHelper;
import alma.ControlExceptions.INACTErrorEx;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;

public class AntennaClient extends ACSClient{
	
	private String Antennas[];
	
	public AntennaClient(Logger logger, String managerLoc, String clientName) throws Exception{
		super(logger,managerLoc,clientName);
		String antennas = getAllAntennas();
		this.Antennas = antennas.split(",");
	}


	@Override
	public LinkedList<String> getStatus() {
		Antenna antenna = null;
		LinkedList<String> report = new LinkedList<String>();
		for(int i = 0; i < this.Antennas.length; i++){
			try {
				//Antenna antenna = AntennaHelper.narrow(getContainerServices().getComponent("CONTROL/DV01"));
				antenna = AntennaHelper.narrow(getContainerServices().getComponent("CONTROL/"+this.Antennas[i]));
				String Name = this.Antennas[i];
				String Array = getArray(antenna);
				String Status = getStatus(antenna);
				String Substatus = getSubStatus(antenna);
				String Online = getOnline(antenna);
				report.add("{Type:Antenna,Name:'"+Name+"',Array:'"+Array+"',Status:'"+Status+"',SubStatus:'"+Substatus+"',Online:'"+Online+"'}");
			} catch (AcsJContainerServicesEx e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				report.add("No se pudo obtener informacion de la antenna " + this.Antennas[i]);
			}
		}
		return report;
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
		return antenna.getErrorMessage();
	}
	
	public String getAllAntennas(){
		return "DV01,DV02,PM03";
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
