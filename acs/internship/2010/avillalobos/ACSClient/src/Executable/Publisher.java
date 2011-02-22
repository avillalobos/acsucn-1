package Executable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import alma.acs.component.client.AdvancedComponentClient;
import alma.acs.logging.ClientLogManager;

import Clients.ACSClient;
import Clients.OffLineTool.AlmaStatus;
import Clients.OffLineTool.AntennaStatus;
import Clients.OffLineTool.CorbaStatus;
import Clients.OffLineTool.SubsystemStatus;
import Clients.OnLineTool.ACSStatus;
import Clients.OnLineTool.ContainerStatus;
import Clients.OnLineTool.OpServerConnection;
import Clients.OnLineTool.SubsystemsStatus;

import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * This class make possible the administration of Online and Offline information, here we can decide 
 * which kind of status display and where write it. The general status (Corba, Acs, Alma, Subsystems and opserver)
 * is written on cacheFile located on $AXIS2_HOME/WebOMCFiles, if this file not exist, using {@link WebOMCLauncher.createFile}
 * we can create this file, but, if $AXIS2_HOME not exist or WebOMCFiles directory not exist, then the program will
 * fail. This program is a runnable so, on the run implementation, we decide when write the status on the files and
 * wait some time specified on the constructor, with a refresh parameter.
 * 
 * @author Andres Villalobos, 2011 Summerjob, Ingenieria de ejecucion en Computacion e Informatica, Universidad Catolica del norte
 * @see DeveloperInfo
 *              The project  <a href="http://almasw.hq.eso.org/almasw/bin/view/JAO/OfflineToolsSummerJob">Twiki page</a> and
 *              my <a href="http://almasw.hq.eso.org/almasw/bin/view/Main/AndresVillalobosDailyLogOfflineTools">Dailylog</a> please
 *              Contact to a.e.v.r.007@gmail.com
 */
public class Publisher implements Runnable{
	
	/**
	 * @param client This allow us get information for Offline status and to decide the Corba status based on list_initial_services();
	 */
	private AdvancedComponentClient client;

	/**
	 * @param files This hashmap contain all files where this application can write, the key of hashmap is equal to
	 * file name and all files was initialized to write on it.
	 */
	private HashMap<String,Archivo> files;
	
	/**
	 * @param timeToRefresh This parameter indicate how often to update the information
	 */
	private Long timeToRefresh;
	
	/**
	 * @param Multiplier This parameter is used to print the quantity of times that the file was written
	 */
	private double Mutliplier;
	
	/**
	 * @param ExecutedRefresh This parameter is used to print the quantity of time that the file was written, if this 
	 * variable is overbound, then multiplier add 1 and this is set on 0
	 */
	private double ExecutedRefresh;
	
	/**
	 * @param offLineClients This parameter is used to save all class that give offline information. As all are inherited from
	 * ACSClient, then all have the same method getStatus, then is possible put another class that inherit ACSClient and
	 * add to this list to add another status information.
	 */
	private LinkedList<ACSClient> offLineClients;
	
	/**
	 * @param onLineClients This parameter is used to save all class that give online information. As all are inherited from 
	 * ACSClient, then all have the same method getStatus, then is possible put another class that inherit ACSClient and add
	 * to this list to add another status information.
	 */
	private LinkedList<ACSClient> onLineClients;
	
	/**
	 * @param opserver This parameter make possible get connection with the opserver and is used as the only class
	 * that have online information, then is passed as parameter to another class that display the specific information 
	 */
	private OpServerConnection opserver;
	
	/**
	 * @param OnlLineActivated This parameter indicate if the information that the iteration will use is online or offline
	 * based on the result of isOpServerOnline from OpServerConnection
	 */
	private boolean OnLineActivated;
	
	/**
	 * @param OpServerOnline This parameter indicate if there is connection with the opserver, if not, the program will
	 * use offline information
	 */
	private boolean OpServerOnline;
	
	private String[] AntennasName;
	private String[] Devices;
	private String brokerURI;
	private String clientName;
	
	/**
	 * This is the constructor of Publisher class, here will try to connect with manager, because we try to create an
	 * Acs Client, if the connection was not possible, then will throw and exception saying that its impossible to 
	 * get conection, then WebOMCLauncher will put a message on a file, and will try again.
	 * 
	 * @param _managerLoc Indicate the location of manager calculated by WebOMCLauncher.
	 * @param _clientName Indicate the name with will register on the ACSLogger.
	 * @param _files Indicate the hashmap of files where will be written the status information.
	 * @param _refresh Indicate the tame between updates
	 * @throws Exception Indicate that was unnable to create a client, contain the message to be written on the
	 * corresponding file.
	 */
	public Publisher(String _managerLoc, String _clientName,HashMap<String,Archivo> _files, Long _refresh,String[] antennas, String[] devices,String _brokerURI) throws Exception{
		
		this.clientName = _clientName;
		this.AntennasName = antennas;
		this.Devices = devices;
		this.brokerURI = _brokerURI;
		
		this.files = _files;
		this.timeToRefresh = _refresh;
		this.ExecutedRefresh = 0;
		this.Mutliplier = 1;
		try {
			this.client = new AdvancedComponentClient(ClientLogManager.getAcsLogManager().getLoggerForApplication(this.clientName, false), _managerLoc, _clientName);
			this.opserver = new OpServerConnection(this.client,this.clientName,"IDL:alma/exec/Executive:1.0");
			// its necessary to have both kind of clients, because if one falls, the other can replace
			createOffLineClients();
			createOnLineClients();
			this.OpServerOnline = this.opserver.isOpserverOnline();
		} catch (Exception e) {
			this.OpServerOnline = false;
			throw new Exception("Failed connection with manager, we must to try again");
		}
	}
	
	/**
	 * Here initialize the list of OnLine clients that will display all online information from opserver
	 * This will display the following information :
	 * 		- ACSStatus
	 * 		- AlmaStatus
	 * 		- AntennaStatus
	 * 		- ContainerStatus
	 * 		- SubsystemsStatus
	 * 
	 * @throws alma.maciErrType.wrappers.AcsJCannotGetComponentEx
	 */
	private void createOnLineClients()throws alma.maciErrType.wrappers.AcsJCannotGetComponentEx{ 
		this.onLineClients = new LinkedList<ACSClient>();
		this.onLineClients.add(new ACSStatus(this.client, this.opserver));
		this.onLineClients.add(new Clients.OnLineTool.AlmaStatus(this.client, this.opserver));
		this.onLineClients.add(new Clients.OnLineTool.AntennaStatus(this.client, this.opserver,this.brokerURI,getDevices()));
		this.onLineClients.add(new ContainerStatus(this.client, this.opserver));
		this.onLineClients.add(new SubsystemsStatus(this.client, this.opserver));
		
		//this.onLineClients.add(new CorbaStatus(this.client, this.firestarter));
		//this.onLineClients.add(this.opserver);
	}
	
	/**
	 * Here initialize the list of OffLine clients that will display all online information from  making
	 * corba calls. The offline tools will display the following information
	 * 		- ACSStatus
	 * 		- CorbaStatus
	 * 		- SubSystemsStatus
	 * 		- AntennaStatus
	 * 		- AlmaStatus
	 * 
	 * @throws alma.maciErrType.wrappers.AcsJCannotGetComponentEx
	 */
	private void createOffLineClients() throws alma.maciErrType.wrappers.AcsJCannotGetComponentEx{
		this.offLineClients = new LinkedList<ACSClient>();
		offLineClients.addFirst(new Clients.OffLineTool.ACSStatus(this.client));
		offLineClients.add(new CorbaStatus(this.client));
		SubsystemStatus sss = new SubsystemStatus(this.client);
		offLineClients.add(sss);
		offLineClients.add(new AntennaStatus(this.client,this.brokerURI,getAntennas(),getDevices()));
		offLineClients.addLast(new AlmaStatus(this.client, sss));
	}
	
	private String[] getDevices(){
		return this.Devices;
	}
	
	private String[] getAntennas(){
		return this.AntennasName;
	}
	
	/*
	// This method is called on run(), because this is a thread that run until stop =D
	public LinkedList<String> getStatus(){
		LinkedList<ACSClient> Status = getProviders();
		// getting information, its doesn't matter if the information is Online or Offline
		LinkedList<String> report = new LinkedList<String>();
		report.add("{\"Type\":\"ReportInfo\",\"OnlineInfo\":"+this.OnLineActivated+",\"Date\":\""+getDate()+"\",\"Iteration\":\""+ this.Mutliplier + " * " + this.ExecutedRefresh + "\",\"Status\":[");
		// this variable was actualized when we call to getProviders()
		if (!this.OpServerOnline) {
			report.add("\"Type:\"OpServerError\",\"Description\":\"Unable to connect to opserver, so we will use offline information\"}");
			for (ACSClient acs : Status) {
				if(!(acs instanceof AntennaStatus)){
					report.addAll(acs.getStatus());
				}
			}
		} else {
			for (ACSClient acs : Status) {
				report.addAll(acs.getStatus());
			}
		}
		report.add("]}");
		return report;
	}
	*/
	
	
	/**
	 * This method recover, create and write the offline status
	 * 
	 * @param date Indicate the date when the report was created
	 * @param Status Indicate the offline clients that will be used to create the status
	 */
	private void OffLineStatus(String date, LinkedList<ACSClient> Status){
		LinkedList<String> report = new LinkedList<String>();
		report.add("{\"Type\":\"ReportInfo\",\"OnlineInfo\":"+this.OnLineActivated+",\"Date\":\""+date+"\",\"Iteration\":\""+ this.Mutliplier + " * " + this.ExecutedRefresh + "\",\"Status\":[");
		report.add("{\"Type:\"OpServerStatus\",\"Name\":\"OpServerError\",\"Status\":\"Unable to connect to opserver, so we will use offline information\"},");
		for (ACSClient acs : Status) {
			if(acs instanceof AntennaStatus){
				AntennaStatus a = (AntennaStatus) acs;
				Collection<Entry<String, String>> list = a.getAntennaInfo().entrySet();
				if (list.size() == 0) {
					String antennas[] = a.getAntennaNames();
					for (String name : antennas) {
						WebOMCLauncher.resetFile(name);
					}
				} else {
					for (Entry<String, String> e : list) {
						WriteFile(e.getKey(), e.getValue(), date);
					}
				}
			}else{
				report.addAll(acs.getStatus());
			}
		}
		report.add("]}");
		WriteFile(report);
	}
	
	/**
	 * This method create the online status based on the information from opserver
	 * 
	 * @param date Indicate the date when the report was created
	 * @param Status Indicate the online clients that will be used to create the status
	 */
	private synchronized void OnLineStatus(String date, LinkedList<ACSClient> Status){
		LinkedList<String> report = new LinkedList<String>();
		report.add("{\"Type\":\"ReportInfo\",\"OnlineInfo\":"+this.OnLineActivated+",\"Date\":\""+date+"\",\"Iteration\":\""+ this.Mutliplier + " * " + this.ExecutedRefresh + "\",\"Status\":[");
		report.add("{\"Type\":\"OpServerStatus\",\"Name\":\"OpServerStatus\",\"Status\":\"Ok\"},");
		for (ACSClient acs : Status) {
			if(acs instanceof Clients.OnLineTool.AntennaStatus){
				Collection<Entry<String,String>> list = ((Clients.OnLineTool.AntennaStatus) acs).getAntennaInfo().entrySet();
				for(Entry<String,String> e: list){
					if(e.getValue() != null)
						WriteFile(e.getKey(), e.getValue(), date);
				}
				
				Collection<Entry<String,String>> hashreport = ((Clients.OnLineTool.AntennaStatus) acs).getActiveMQInfo().entrySet();
				for(Entry<String,String> e: hashreport){
					System.out.println("Iterando sobre el hash report de antenna para activemq");
					if(e.getValue() != null)
						WriteFile(e.getKey(), e.getValue(), date);
				}
			}else{
				report.addAll(acs.getStatus());
			}
		}
		// Fixing the last comma
		String last = report.removeLast();
		report.addLast(last.substring(0, last.length()-1));
		// Last comma fixed
		Executable.WebOMCLauncher.resetFile("cacheFile");
		report.add("]}");
		WriteFile(report);
	}
	
	/**
	 * This method write the generic report generate by OnLineStatus or OffLineStatus
	 * 
	 * @param report The status to write on the cacheFile 
	 */
	private void WriteFile(LinkedList<String> report){
		Executable.WebOMCLauncher.resetFile("cacheFile");
		try {
			this.files.get("cacheFile").Escribir(report);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will write the information on a specific file
	 * 
	 * @param nameFile The name where the information will be written
	 * @param line The line that will be written on the file
	 * @param date Indicate the date when the report was created
	 */
	private void WriteFile(String nameFile, String line, String date){
		//Executable.WebOMCLauncher.resetFile(nameFile);
		try {
			this.files.get(nameFile).Escribir("{\"Type\":\"ReportInfo\",\"OnlineInfo\":"+this.OnLineActivated+",\"Date\":\""+date+"\",\"Iteration\":\""+ this.Mutliplier + " * " + this.ExecutedRefresh + "\",\"Status\":[",true);
			this.files.get(nameFile).Escribir(line,false);
			this.files.get(nameFile).Escribir("]}",false);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * This method decide which kind of status display, depending of OpServer status
	 */
	public void PrintStatus(){
		String date = getDate();
		LinkedList<ACSClient> Status = getProviders();
		LinkedList<String> report = new LinkedList<String>();
		report.add("{\"Type\":\"ReportInfo\",\"OnlineInfo\":"+this.OnLineActivated+",\"Date\":\""+date+"\",\"Iteration\":\""+ this.Mutliplier + " * " + this.ExecutedRefresh + "\",\"Status\":[");
		// this variable was actualized when we call to getProviders()
		if (!this.OpServerOnline) {
			OffLineStatus(date, Status);			
		} else {
			OnLineStatus(date, Status);
		}
	}
	
	/**
	 * This method decide which providers (Online or offline) return to make the status report
	 * @return The list of ACSClient that correspond to the status of the ACSSystem. Online if opserver is online
	 * and offline if opserver is down
	 */
	private LinkedList<ACSClient> getProviders(){
		
		LinkedList<ACSClient> Status = new LinkedList<ACSClient>();
		if(this.opserver == null){
			this.opserver = new OpServerConnection(this.client,this.clientName,"IDL:alma/exec/Executive:1.0");
			this.OnLineActivated = false;
			Status.addAll(this.offLineClients);
		}else if(isOpServerOnline()){
			Status.addAll(this.onLineClients);
			this.OnLineActivated = true;
		}else{
			Status.addAll(this.offLineClients);
			this.OnLineActivated = false;
		}
		
		return Status;
	}
	
	/**
	 * This method write a Warning message on the logger using the client's logger
	 * 
	 * @param msg The message to display on the Logger
	 */
	protected void writeWarningMessage(String msg){
		this.client.getContainerServices().getLogger().warning(msg);
	}
	
	/**
	 * This method write a Fine message on the logger using the client's logger
	 * 
	 * @param msg The message to display on the Logger
	 */
	protected void writeFineMessage(String msg){
		this.client.getContainerServices().getLogger().info(msg);
	}
	
	/**
	 * This method write a Info message on the logger using the client's logger
	 * 
	 * @param msg The message to display on the Logger
	 */
	protected void writeInfoMessage(String msg){
		this.client.getContainerServices().getLogger().fine(msg);
	}

	/**
	 * This method print the status each time according to the timeToRefresh parameter
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(this.ExecutedRefresh == Double.MAX_VALUE){
				this.Mutliplier++;
				this.ExecutedRefresh = 0;
			}
			this.ExecutedRefresh++;
			if(isAcsFalling()){
				break;
			}
			PrintStatus();
			writeInfoMessage("Status was written on file " + this.files.get("cacheFile").getPath() + " " + this.Mutliplier + " * " + this.ExecutedRefresh + " times");
			try {
				Thread.sleep(this.timeToRefresh);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method create the date to add to the report
	 * 
	 * @return A string with the actual date on the following format "yyyy'/'MM'/'dd 'at' hh:mm:ss z", example = 2011/02/04 at 09:35:39 UTC
	 */
	private String getDate() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy'/'MM'/'dd 'at' hh:mm:ss z");
	    return sdf.format(cal.getTime());
	}
	
	/**
	 * This method ask to opserver if can retrieve ACS information
	 * 
	 * @return <li>true  : If its possible to connect with the opserver </li>
	 * 		   <li>false : It its not possible to connect with the opserver </li>
	 */
	private boolean isOpServerOnline(){
		this.OpServerOnline = this.opserver.isOpserverOnline();
		return this.OpServerOnline;
	}
	
	/**
	 * @param before This parameter indicate the past state of OpServerOnline to can decide when ACS is falling down
	 */
	private boolean before = false;
	
	/**
	 * This method indicate when acs is falling
	 * @return	<li>true  : if acs is falling, this is determinated when the past state of ACS was true and the actual state is false </li>
	 * 			<li>false : any another combination of variables.
	 */
	private boolean isAcsFalling(){
		System.out.println("\n\t ####  before " + this.before + " now " + this.OpServerOnline);
		boolean acs = this.client.getAcsManagerProxy().pingManager(100);
		if(before && !acs){
			System.out.println("\n\t #####  ACS is falling!  #####");
			this.before = acs;
			return true;
		}else{
			this.before = acs;
			return false;
		}
	}
	
}
