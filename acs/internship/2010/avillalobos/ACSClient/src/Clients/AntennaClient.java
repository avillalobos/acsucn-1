package Clients;

import java.util.LinkedList;
import java.util.logging.Logger;

import alma.ACS.ACSComponent;
import alma.ACS.ACSComponentHelper;
import alma.Control.Antenna;
import alma.Control.AntennaHelper;
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
				ACSComponent acscomponent = ACSComponentHelper.narrow(antenna);
				antenna.getState().toString();
				 
				report.add("El reporte para la antena " + this.Antennas[i] +" online = " + antenna.getState().toString() + " substatus = " + acscomponent.componentState().toString());
			} catch (AcsJContainerServicesEx e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				report.add("No se pudo obtener informacion de la antenna " + this.Antennas[i]);
			}
		}
		return report;
	}
	
	public String getAllAntennas(){
		return "DV01";
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
