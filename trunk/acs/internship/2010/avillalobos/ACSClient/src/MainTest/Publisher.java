package MainTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import alma.acs.commandcenter.meta.Firestarter;
import alma.acs.component.client.AdvancedComponentClient;

import Clients.ACSClient;
import Clients.AlmaStatus;
import Clients.AntennaStatus;
import Clients.CorbaStatus;
import Clients.SubsystemStatus;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class Publisher implements Runnable{
	
	private AdvancedComponentClient client;
	private Firestarter firestarter;
	private Archivo archivo;
	private Long timeToRefresh;
	private double Mutliplier;
	private double ExecutedRefresh;
	private LinkedList<ACSClient> clientes;
	
	public Publisher(String managerLoc, String clientName, Archivo _archivo, Long refresh){
		this.archivo = _archivo;
		this.timeToRefresh = refresh;
		this.ExecutedRefresh = 0;
		this.Mutliplier = 1;
		try {
			this.client = new AdvancedComponentClient(null, managerLoc, clientName);
			this.firestarter = new Firestarter(clientName, null, managerLoc);
			createClients();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createClients(){
		this.clientes= new LinkedList<ACSClient>();
		SubsystemStatus sss =new SubsystemStatus(this.client); 
		clientes.add(sss);
		clientes.add(new AntennaStatus(this.client));
		clientes.add(new CorbaStatus(this.client,this.firestarter));
		clientes.addLast(new AlmaStatus(this.client, sss));
	}
	
	public LinkedList<String> getStatus(){
		LinkedList<String> report = new LinkedList<String>();
		report.add("{Type:'ReportInfo', Date:'"+getDate()+"',Iteration:'"+ this.Mutliplier + " * " + this.ExecutedRefresh + "',");
		for(ACSClient acs : clientes){
			report.addAll(acs.getStatus());
		}
		report.add("}");
		return report;
	}
	
	public void writeWarningMessage(String msg){
		this.client.getContainerServices().getLogger().warning(msg);
	}
	
	public void writeFineMessage(String msg){
		this.client.getContainerServices().getLogger().info(msg);
	}
	
	public void writeInfoMessage(String msg){
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
				System.out.println("Status was written on file " + this.archivo.getPath() + " " + this.Mutliplier + " * " + this.ExecutedRefresh + " times");
				resetFile();
				archivo.Escribir(getStatus());
				writeInfoMessage("Status was written on file " + this.archivo.getPath() + " " + this.Mutliplier + " * " + this.ExecutedRefresh + " times");
				Thread.sleep(this.timeToRefresh);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block	
				//e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				createCacheFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				createCacheFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				createCacheFile();
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
	
}
