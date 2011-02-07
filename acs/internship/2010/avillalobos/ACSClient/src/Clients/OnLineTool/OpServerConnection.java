package Clients.OnLineTool;

import alma.ACS.OffShoot;
import alma.ExecPrivateErrors.OperatorServerProblemEx;
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.AdvancedComponentClient;
import alma.exec.Executive;
import alma.exec.ExecutiveHelper;
import alma.exec.OperatorClient;
import alma.exec.OperatorClientHelper;
import alma.exec.operatorbase.portable.ExecRole;

/**
 * This class is responsible to the connection with OpServer, here decide if there is connection with the opserver to use
 * his information or there is not connection with the opserver, then the application must to use the offline information.
 * There is a class named ServerClient made by Marcus Shilling that allow me to get the information from opserver
 * through a listener, this listener is ServerClient and this class initialize the ServerClient and the connection to opserver
 */
public class OpServerConnection {

	/**
	 * @param opserver This executive parameter allow to register on opserver a new listener
	 */
	private Executive opserver;
	private OperatorClient operator;
	private ServerClient serverClient;
	private boolean online;
	private String clientName;
	private String operatorServerCurl;
	private AdvancedComponentClient client;
	
	/**
	 * This is the constructor of OpServerConnection, here is initialized opserver, operator and serverClient and also
	 * the client needed to register some logs on the ACS Logger. Also clients is used to get the container services
	 * to can get the references to the opserver.
	 * 
	 * @param _client This AdvancedComponentClient is used to get the container services and also write on ACS Logger
	 * @param _clientName This variable represent the client name 
	 * @param _operatorServerCurl This variable represent the curl to operator server
	 */
	public OpServerConnection(AdvancedComponentClient _client,String _clientName,String _operatorServerCurl) {
		this.client = _client;
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
	
	/**
	 * This method try to connect with the opserver, if is unable to connect with opserver then
	 * the online variable is set false, else is true 
	 */
	private void Connect(){
		String role = ExecRole.MONITOR.toString();
		if(this.opserver == null){
			this.client.getContainerServices().getLogger().info("########### opserver null");
			try {
				this.opserver = ExecutiveHelper.narrow(this.client.getContainerServices().getDefaultComponent(this.operatorServerCurl));
				this.client.getContainerServices().getLogger().info("########### Getting opserver reference");
				this.serverClient = new ServerClient(this.clientName);
				this.client.getContainerServices().getLogger().info("########### Creating serverClient listener");
				OffShoot off = this.client.getContainerServices().activateOffShoot(this.serverClient);
				this.client.getContainerServices().getLogger().info("########### getting offshot");
				this.operator = OperatorClientHelper.narrow(off);
				this.client.getContainerServices().getLogger().info("########### opserver initialized");
				opserver.login(this.operator, this.clientName, role, this.clientName);
				this.client.getContainerServices().getLogger().info("########### opserver conected");
				this.online = true;
				return;
			} catch (AcsJContainerServicesEx e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				this.online = false;
				this.client.getContainerServices().getLogger().info("########### opserver failed because there is not container services to get");
				return;
			} catch (OperatorServerProblemEx e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				this.client.getContainerServices().getLogger().info("########### opserver failed because there is some mysterious problems with opserver, " + e.getCause());
				return;
			}
		}
		this.online = this.opserver.readyForLogin();
		/**
		try {
			opserver.readyForLogin();
			opserver.login(this.operator, this.clientName, role, this.clientName);
			this.client.getContainerServices().getLogger().info("Connection successful to opserver");
			this.online = true;
			return;
		} catch (OperatorServerProblemEx e) {
			// TODO Auto-generated catch block
			this.opserver.logout(operator);
			//e.printStackTrace();
			this.client.getContainerServices().getLogger().info("########### opserver failed because there is some mysterious problems with opserver, " + e.getCause());
			this.online = false;
			return;
		} catch (Exception e){
			this.online = false;
			this.client.getContainerServices().getLogger().info("########### opserver conection failed, for some reason =D");
			return;
		}
		**/
	}

	/**
	 * This method indicate if was possible to connect with opserver
	 * 
	 * @return <li>true  : If there is connection with opserver </li>
	 * 		   <li>false : If there is not connection with opserver </li>
	 */
	public boolean isOpserverOnline(){
		Connect();
		return this.online;
	}
/*
	private LinkedList<String> getStatus() {
		// TODO Auto-generated method stub
		LinkedList<String> repo = new LinkedList<String>();
		if(this.serverClient == null){
			repo.add("{\"Type\":\"OpServerError\",\"Description\":\"doesn't initialized Listener to OpServer\"},");
			return repo;
		}else{
			repo.addAll(this.serverClient.getStatus());
		}
		
		if (repo.isEmpty()) {
			repo.add("{\"Type\":\"OpServer\",\"Description\":\"Opserver doesn't given information\"},");
			return repo;
		} else {
			String last = repo.removeLast();
			int i = last.lastIndexOf(",");
			String nuevo = last.substring(0, i);
			repo.addLast(nuevo);
			return repo;
		}
	}
	*/
	
	/**
	 * This method return the listener of opserver
	 * 
	 * @return An listener to opserver that contains all online status info
	 */
	public ServerClient getServerClient(){
		return this.serverClient;
	}
}
