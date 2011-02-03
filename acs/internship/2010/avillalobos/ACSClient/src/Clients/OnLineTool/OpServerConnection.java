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
	
	public OpServerConnection(AdvancedComponentClient _client,String _clientName,String _operatorServerCurl) {
		super(_client);
		try {
			this.clientName = _clientName;
			this.opserver = ExecutiveHelper.narrow(this.client.getContainerServices().getDefaultComponent(_operatorServerCurl));
			this.serverClient = new ServerClient(_clientName);
			OffShoot off = this.client.getContainerServices().activateOffShoot(this.serverClient);
			this.operator = OperatorClientHelper.narrow(off);
			Connect();
		} catch (AcsJContainerServicesEx e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.online = false;
		}
	}
	
	private void Connect(){
		String role = ExecRole.MONITOR.toString();;
		try {
			opserver.login(this.operator, this.clientName, role, this.clientName);
			this.online = true;
		} catch (OperatorServerProblemEx e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.online = false;
		} catch (Exception e){
			this.online = false;
			writeFineMessage(e.getMessage());
		}
	}

	public boolean isOpserverOnline(){
		Connect();
		return this.online;
	}

	@Override
	public LinkedList<String> getStatus() {
		writeFineMessage("ejecutando status");
		// TODO Auto-generated method stub
		LinkedList<String> repo = new LinkedList<String>();
		repo.addAll(this.serverClient.getStatus());
		if (repo.isEmpty()) {
			return repo;
		} else {
			String last = repo.removeLast();
			int i = last.lastIndexOf(",");
			String nuevo = last.substring(0, i);
			repo.addLast(nuevo);
			if(repo.size() == 0){
				repo.add("\"Type\":\"OpServer\",\"Description\":\"Opserver doesn't given information\"},");
			}
			return repo;
		}
	}
	
}
