package MainTest;

import java.util.LinkedList;

import Clients.ACSClient;
import Clients.AntennaClient;
import Clients.SubsystemClient;

public class PseudoProxy{

	private SubsystemClient sc;
	public PseudoProxy(){
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//String managerLoc = "corbaloc::10.195.17.114:3000/Manager";
		//ACSClient list[] = new ACSClient[2];
		//list[0] = new SubsystemClient(null, managerLoc, "ACS Status Client");
		//list[1] = new AntennaClient(null, managerLoc, "ACS Status Client");
		//run(list);
		//Runtime.getRuntime().exec("konqueror");
		WebServiceClient ws = new WebServiceClient();
		printStatus(ws.getStatus());
	}

	public static void run(ACSClient list[]) {
		
		for(int i = 0; i < list.length; i++){
			System.out.println("Status de " + list[i].getClass().getCanonicalName() +" es ");
			printStatus(list[i].getStatus());
		}
	}
	
	public static void printStatus(LinkedList<String> l){
		for(String s : l){
			if(s != null){
				System.out.println(s);
			}else{
				System.out.println("No data found");
			}
		}
	}

}
