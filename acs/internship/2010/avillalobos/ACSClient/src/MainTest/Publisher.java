package MainTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import alma.acs.commandcenter.meta.Firestarter;
import alma.acs.component.client.AdvancedComponentClient;
import alma.acs.logging.ClientLogManager;

import Clients.ACSClient;
import Clients.OffLineTool.AlmaStatus;
import Clients.OffLineTool.AntennaStatus;
import Clients.OffLineTool.CorbaStatus;
import Clients.OffLineTool.SubsystemStatus;
import Clients.OnLineTool.OpServerConnection;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class Publisher implements Runnable{
	
	private AdvancedComponentClient client;
	private Firestarter firestarter;
	private Archivo archivo;
	private Long timeToRefresh;
	private double Mutliplier;
	private double ExecutedRefresh;
	private LinkedList<ACSClient> offLineClients;
	private LinkedList<ACSClient> onLineClients;
	private OpServerConnection opserver;
	private boolean OnLineActivated;
	private boolean OpServerOnline;
	
	public Publisher(String managerLoc, String _clientName, Archivo _archivo, Long refresh) throws Exception{
		this.archivo = _archivo;
		this.timeToRefresh = refresh;
		this.ExecutedRefresh = 0;
		this.Mutliplier = 1;
		try {
			this.client = new AdvancedComponentClient(ClientLogManager.getAcsLogManager().getLoggerForApplication(_clientName, false), managerLoc, _clientName);
			this.opserver = new OpServerConnection(this.client,_clientName,"IDL:alma/exec/Executive:1.0");
			this.firestarter = new Firestarter(_clientName, null, managerLoc);
			// its necessary to have both kind of clients, because if one falls, the other can replace
			createOffLineClients();
			createOnLineClients();
			this.OpServerOnline = true;
		} catch (Exception e) {
			this.OpServerOnline = false;
			throw new Exception("Failed connection with manager, we must to try again");
		}
	}
	
	private void createOnLineClients()throws alma.maciErrType.wrappers.AcsJCannotGetComponentEx{ 
		this.onLineClients = new LinkedList<ACSClient>();
		this.onLineClients.add(new CorbaStatus(this.client, this.firestarter));
		this.onLineClients.add(this.opserver);
	}
	
	private void createOffLineClients() throws alma.maciErrType.wrappers.AcsJCannotGetComponentEx{
		this.offLineClients = new LinkedList<ACSClient>();
		SubsystemStatus sss = new SubsystemStatus(this.client);
		offLineClients.add(sss);
		offLineClients.add(new AntennaStatus(this.client,"tcp://tmc-services.aiv.alma.cl:61616"));
		offLineClients.add(new CorbaStatus(this.client, this.firestarter));
		offLineClients.addLast(new AlmaStatus(this.client, sss));
	}
	
	// This method is called on run(), because this is a thread that run until stop =D
	public LinkedList<String> getStatus(){
		LinkedList<ACSClient> Status = getProviders();
		// getting information, its doesn't matter if the information is Online or Offline
		LinkedList<String> report = new LinkedList<String>();
		report.add("{\"Type\":\"ReportInfo\",\"OnlineInfo\":"+this.OnLineActivated+",\"Date\":\""+getDate()+"\",\"Iteration\":\""+ this.Mutliplier + " * " + this.ExecutedRefresh + "\",\"Status\":[");
		// this variable was actualized when we call to getProviders()
		if (!this.OpServerOnline) {
			report.add("\"Type:\"OpServerError\",\"Description\":\"Unable to connect to opserver, so we will use offline information\"}");
			if(isAcsOnline()){
				report.add("\"Type:\"ACSStatus\",\"Status\":\"Ok\"}");
			}else{
				report.add("\"Type:\"ACSStatus\",\"Status\":\"Down\"}");
			}
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
	
	// The provider could be online or offline clients
	private LinkedList<ACSClient> getProviders(){
		
		LinkedList<ACSClient> Status = new LinkedList<ACSClient>();
		if(this.opserver == null){
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
	
	protected void writeWarningMessage(String msg){
		this.client.getContainerServices().getLogger().warning(msg);
	}
	
	protected void writeFineMessage(String msg){
		this.client.getContainerServices().getLogger().info(msg);
	}
	
	protected void writeInfoMessage(String msg){
		this.client.getContainerServices().getLogger().fine(msg);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if(this.ExecutedRefresh == Double.MAX_VALUE){
				this.Mutliplier++;
				this.ExecutedRefresh = 0;
			}
			this.ExecutedRefresh++;
			try {
				LinkedList<String> report = getStatus();
				System.out.println("######## Size report = " + report.size());
				resetFile();
				Thread.sleep(100);
				archivo.Escribir(report);
				writeInfoMessage("Status was written on file " + this.archivo.getPath() + " " + this.Mutliplier + " * " + this.ExecutedRefresh + " times");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block	
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				createCacheFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				createCacheFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				createCacheFile();
			}
			try {
				Thread.sleep(this.timeToRefresh);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void resetFile(){
		String cmd[] = new String[]{"bash","-c","cat /dev/null > $AXIS2_HOME/cacheFile"};
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("El programa no puede seguir porque no se pudo refrescar el archivo");
			System.exit(1);
		}
	}
	
	private void createCacheFile(){
		String cmd[] = new String[]{"bash","-c","touch $AXIS2_HOME/cacheFile"};
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("El programa no puede seguir porque no se pudo crear el archivo");
			System.exit(1);
		}
	}
	
	private String getDate() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy'/'MM'/'dd 'at' hh:mm:ss z");
	    return sdf.format(cal.getTime());
	}
	
	private boolean isOpServerOnline(){
		this.OpServerOnline = this.opserver.isOpserverOnline();
		return this.OpServerOnline;
	}
	
	private boolean isAcsOnline(){
		return false;
	}
	
}
