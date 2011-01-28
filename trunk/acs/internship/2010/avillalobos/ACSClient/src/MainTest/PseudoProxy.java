package MainTest;

import java.io.IOException;
import java.util.LinkedList;

import Clients.ACSClient;

public class PseudoProxy{

	public static void LaunchPrograms() throws IOException{
		Runtime.getRuntime().exec("konqueror");
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args){
		// TODO Auto-generated method stub
		//String managerLoc = "corbaloc::10.195.17.114:3000/Manager";
		//ACSClient list[] = new ACSClient[2];
		//list[0] = new SubsystemClient(null, managerLoc, "ACS Status Client");
		//list[1] = new AntennaClient(null, managerLoc, "ACS Status Client");
		//run(list);
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
