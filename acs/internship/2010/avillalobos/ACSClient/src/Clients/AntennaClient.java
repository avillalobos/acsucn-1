package Clients;

import java.util.logging.Logger;

import alma.ACS.ACSComponent;
import alma.ACS.ACSComponentHelper;
import alma.Control.Antenna;
import alma.Control.AntennaHelper;
import alma.Control.AntennaPOA;
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
	public String[] getStatus() {
		Antenna antenna = null;
		String report[] = new String[this.Antennas.length];
		for(int i = 0; i < this.Antennas.length; i++){
			try {
				//Antenna antenna = AntennaHelper.narrow(getContainerServices().getComponent("CONTROL/DV01"));
				antenna = AntennaHelper.narrow(getContainerServices().getComponent("CONTROL/"+this.Antennas[i]));
				ACSComponent acscomponent = ACSComponentHelper.narrow(antenna);
				antenna.getState().toString();
				 
				report[i] = "El reporte para la antena " + this.Antennas[i] +" online = " + antenna.getState().toString() + " substatus = " + acscomponent.componentState().toString();
			} catch (AcsJContainerServicesEx e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				report[i] = "No se pudo obtener informacion de la antenna " + this.Antennas[i];
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
