package Clients.OnLineTool;

import java.util.LinkedList;

import alma.ACS.OffShoot;
import alma.ExecPrivateErrors.OperatorServerProblemEx;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.AdvancedComponentClient;
import alma.exec.Executive;
import alma.exec.ExecutiveHelper;
import alma.exec.OperatorClient;
import alma.exec.OperatorClientHelper;
import alma.exec.operatorbase.portable.ExecRole;

import Clients.ACSClient;

public class OpServerConnection extends ACSClient{

	private Executive opserver;
	private OperatorClient operator;
	private ServerClient serverClient;
	private boolean online;
	private String clientName;
	private String operatorServerCurl;
	
	public OpServerConnection(AdvancedComponentClient _client,String _clientName,String _operatorServerCurl) {
		super(_client);
		try {
			this.clientName = _clientName;
			this.operatorServerCurl = _operatorServerCurl;
			this.opserver = ExecutiveHelper.narrow(this.client.getContainerServices().getDefaultComponent(_operatorServerCurl));
			this.serverClient = new ServerClient(_clientName);
			OffShoot off = this.client.getContainerServices().activateOffShoot(this.serverClient);
			this.operator = OperatorClientHelper.narrow(off);
			Connect();
		} catch (AcsJContainerServicesEx e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.online = false;
		} catch (Exception e){
			System.out.println("Exception capturada " + e.getCause());
		}
	}
	
	private void Connect(){
		String role = ExecRole.MONITOR.toString();
		if(this.opserver == null){
			writeInfoMessage("########### opserver null");
			try {
				this.opserver = ExecutiveHelper.narrow(this.client.getContainerServices().getDefaultComponent(this.operatorServerCurl));
				writeInfoMessage("########### Getting opserver reference");
				this.serverClient = new ServerClient(this.clientName);
				writeInfoMessage("########### Creating serverClient listener");
				OffShoot off = this.client.getContainerServices().activateOffShoot(this.serverClient);
				writeInfoMessage("########### getting offshot");
				this.operator = OperatorClientHelper.narrow(off);
				writeInfoMessage("########### opserver initialized");
				opserver.login(this.operator, this.clientName, role, this.clientName);
				writeInfoMessage("########### opserver conected");
				this.online = true;
				return;
			} catch (AcsJContainerServicesEx e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				this.online = false;
				writeInfoMessage("########### opserver failed because there is not container services to get");
				return;
			} catch (OperatorServerProblemEx e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				writeInfoMessage("########### opserver failed because there is some mysterious problems with opserver, " + e.getCause());
				return;
			}
		}
		
		try {
			opserver.login(this.operator, this.clientName, role, this.clientName);
			writeFineMessage("Connection successful to opserver");
			this.online = true;
			return;
		} catch (OperatorServerProblemEx e) {
			// TODO Auto-generated catch block
			this.opserver.logout(operator);
			//e.printStackTrace();
			writeInfoMessage("########### opserver failed because there is some mysterious problems with opserver, " + e.getCause());
			this.online = false;
			return;
		} catch (Exception e){
			this.online = false;
			writeInfoMessage("########### opserver conection failed, for some reason =D");
			return;
		}
	}

	public boolean isOpserverOnline(){
		Connect();
		return this.online;
	}

	@Override
	public LinkedList<String> getStatus() {
		// TODO Auto-generated method stub
		LinkedList<String> repo = new LinkedList<String>();
		if(this.serverClient == null){
			repo.add("\"Type\":\"OpServerError\",\"Description\":\"doesn't initialized Listener to OpServer\"},");
			return repo;
		}else{
			repo.addAll(this.serverClient.getStatus());
		}
		
		if (repo.isEmpty()) {
			repo.add("\"Type\":\"OpServer\",\"Description\":\"Opserver doesn't given information\"},");
			return repo;
		} else {
			String last = repo.removeLast();
			int i = last.lastIndexOf(",");
			String nuevo = last.substring(0, i);
			repo.addLast(nuevo);
			return repo;
		}
	}
	
}
