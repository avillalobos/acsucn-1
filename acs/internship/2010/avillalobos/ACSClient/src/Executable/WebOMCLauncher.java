package Executable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Main class responsible to try to connect with something on ACS (client or opserver) and put this
 * information on a cache file to leate be displayed by a webservice.
 *
 * @author Andres Villalobos, 2011 Summerjob, Ingenieria de ejecucion en Computacion e Informatica, Universidad Catolica del norte
 * @see DeveloperInfo
 *              The project  <a href="http://almasw.hq.eso.org/almasw/bin/view/JAO/OfflineToolsSummerJob">Twiki page</a> and
 *              my <a href="http://almasw.hq.eso.org/almasw/bin/view/Main/AndresVillalobosDailyLogOfflineTools">Dailylog</a> please
 *              Contact to a.e.v.r.007@gmail.com
 */
public class WebOMCLauncher{

	/**
	 * This method execute a bash command line (find $AXIS2_HOME/WebOMCFiles/cacheFile -name cacheFile) that retrieve the 
	 * enviroment variable $AXIS2_HOME adding the name of the file that this program need, cacheFile, but my application 
	 * can create this file if ths $AXIS2_HOME variable exist on the enviroment. Its important to have this file on this 
	 * place because the client read from the same place and the information is on the same place that axis2server keeping 
	 * on order.
	 * 
	 *  @return The path to the cache file used to record the information (Could be antenna's file or generic's file) using
	 *  as base the $AXIS2_HOME enviroment variable.
	 */
	public static String recoverPathFile(String nameFile){
		// the command line to execute on bash that search the file
		String cmd[] = new String[]{"bash","-c","find $AXIS2_HOME/WebOMCFiles/ -name "+nameFile};
		String path = "";
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s = null;
			
			// creating the path
			while ((s = stdInput.readLine()) != null) {
                path = path + s;
			}
			stdInput.close();
			
			// if path is null or white, so the file was not found
			if(path == null || path.length() == 0){
				return "Path not found";
			}else{
				return path;
			}
			// If couldn't to execute the command line
		} catch (IOException e) {
			return "Unable to recover path";
		}
	}
	
	/**
	 * This method make the cacheFile on null, on this way, its possible to overwrite the file and always 
	 * keep actualiced information. This method execute a bash command line (cat /dev/null > $AXIS2_HOME/WebOMCFiles/cacheFile)
	 * that simplily clean the file, or put this file on null. Also this method will wait a 0.1 second to wait that the operation
	 * has been executed
	 */
	public static void resetFile(String fileName){
		// the command line to execute on bash
		String cmd[] = new String[]{"bash","-c","echo '' > $AXIS2_HOME/WebOMCFiles/"+fileName};
		try {
			Runtime.getRuntime().exec(cmd);
			Thread.sleep(100);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("El programa no puede seguir porque no se pudo refrescar el archivo");
			System.exit(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method create the file on case of this files was not found, like another methods, here i'm executing a bash
	 * command line (touch $AXIS2_HOME/WebOMCFiles/cacheFile) to create the file and later be able to write on it. 
	 */
	public static void createFile(String fileName){
		// the command line to execute on bash
		String cmd[] = new String[]{"bash","-c","touch $AXIS2_HOME/WebOMCFiles/"+fileName};
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("El programa no puede seguir porque no se pudo crear el archivo");
			System.exit(1);
		}
	}
	
	/**
	 * This method calculate the manager port using as base the AcsInstance (0 - 9), on base to the information from
	 * twiki i found that the AcsPort are defined with this algorithm, so i will use the same to work according to
	 * ACS project. 
	 * 
	 * @param AcsInstance The Acs instance to use with the client, you can choose between 0 and 9, it AcsInstance is bigger
	 * than 9, then this method will use the default AcsInstance (0).
	 * @return The manager location where we can find and connect with the manager that we need to monitor 
	 * @see http://almasw.hq.eso.org/almasw/bin/view/ACS/AcsPortsAllocation
	 */
	public static String getManagerLoc(int AcsInstance, String IP){
		if(AcsInstance > 9){
			System.out.println("###########  Acs Instance is bigger than 9, so we will use the 0 instance, i this is wrong, please stop the application  ###########");
			// choosing the default AcsInstance
			AcsInstance = 0;
		}
		int Manageroffset = 0;
		int port = 3000 + AcsInstance*100 + Manageroffset;
		return "corbaloc::"+IP+":"+port+"/Manager";
	}
	
	/**
	 * Prints the need parameter to run this application and also tell you about $AXIS2_HOME, this enviroment variable must to 
	 * be seted before to run this application and inside of this directory, must to exist a folder named WebOMCFiles where will be
	 * created all the files to use by the webservice.  
	 */
	private static void PrintUsage(){
		System.out.println("To can use this application, you must to pass the following parameters and the enviroment Variable $AXIS2_HOME must\n" +
				           "to exist, on the Axis2 home must to be a folder named WebOMCFiles and on this place all files will be saved");
		System.out.println("\t 1) AcsInstance");
		System.out.println("\t 2) IP to Manager");
		System.out.println("\t 3) Time to refresh on seconds");
		System.out.println("\t 4) Broker URI");
		System.out.println("\t x) -h to show this help");
	}
	
	/**
	 * This method validate a number, this method is only used to validate ACSIntance an IP numbers, thats why if the number is bigger
	 * than 255 and smallest than 0 return false.
	 * 
	 * @param arg1 the string to ve parse to a int number
	 * @return <li>true : If the number is between 0 and 255<\li>
	 * 		   <li>false : If the number is bigger than 255 and smallest than 0<\li>
	 */
	private static boolean isNumber(String arg1) {
		try {
			int x = Integer.parseInt(arg1);
			if (x > 255 || x < 0) {
				return false;
			} else {
				return true;
			}
		} catch (NumberFormatException e) {
			return false;
		}

	}
	
	/**
	 * This method validate an IP on IPV4, to do that, i made a split of the parameter, this return an array of String
	 * separated by '.', so the first validation is that the array length have exactly 4 blocks, later, we must to 
	 * validate each number, to do that, we iterate over the array an validate each number, if one of the blocks
	 * doesn't have a number, return false, if all number could be validates, then return true.  
	 * 
	 * @param arg2 An string passed by parameter that represent the IP
	 * @return <li>true : If is a valid IP </li>
	 * 		   <li>false : Is is not a valid IP </li>
	 */
	private static boolean isIp(String arg2){
		if(arg2.equalsIgnoreCase("localhost")){
			return true;
		}else{
			String[] ip = arg2.split("\\.");
			if (ip.length != 4) {
				return false;
			} else {
				for (int i = 0; i < 4; i++) {
					if (!isNumber(ip[i])) {
						return false;
					}
				}
				return true;
			}
		}
	}
	
	/**
	 * This method check the files, if file exist, will return the complete file path, else will create it
	 * 
	 * @param fileName The name of the file to check
	 * @param write Indicate if the file will be to write or read, if write is false, then assume that the file will 
	 * be used to read
	 * @return An Archivo file to write on it. 
	 */
	private static Archivo checkFile(String fileName, boolean write){
		String filePath = recoverPathFile(fileName);
		if (filePath.equalsIgnoreCase("Path not found")	|| filePath.equalsIgnoreCase("Unable to recover path")) {
			createFile(fileName);
			filePath = recoverPathFile(fileName);
		}else{
			if(write)
				resetFile(fileName);
		}
		Archivo archivo = new Archivo(filePath);
		try {
			if(write)
				archivo.Escritura();
			else
				archivo.Lectura();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return archivo;
	}
	
	/**
	 * This method create all files to record the information on a hashmap, the key of this hashmap is the name
	 * of the file and the value is a Archivo class that you can use to Write and Read information.
	 * 
	 * @return A HashMap that contain an Archivo to write and read files. The key of the hashmap is the name of
	 * the file.
	 */
	private static HashMap<String,Archivo> InitializeAllFiles(String antennas[], String devices[]){
		HashMap<String,Archivo> Files = new HashMap<String,Archivo>();
		// Creating cacheFile, where all logs except Antenna will be writed
		Files.put("cacheFile", checkFile("cacheFile",true));
		System.out.println("Cache file is ready");
		
		// Creating AntennaFiles
		for(String antennaName : antennas){
			Files.put(antennaName, checkFile(antennaName,true));
			System.out.println("File to antenna " + antennaName + " already");
		}
		
		// Creating Devices files
		for(String dev : devices){
			Files.put(dev, checkFile(dev,true));
			System.out.println("File to device " + dev + " already");
		}
		return Files;
	}
	
	/**
	 * This method return the name of the available antennas
	 * 
	 * @return An array with the name of each antenna
	 */
	private static String[] getAntennaNames(){
		Archivo a = checkFile("Antennas",false);
		try {
			LinkedList<String> antennas = a.LeerCompleto();
			String[] returned = new String[antennas.size()];
			int i = 0;
			System.out.println("Antennas to load =>");
			for(String name : antennas){
				System.out.println("\t '- Antenna "+name);
				returned[i] = name;
				i++;
			}
			System.out.println();
			return returned;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error, unable to get antenna name on file");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error, unable to get antenna name on file");
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error, unable to get antenna name on file");
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}
	
	/**
	 * This method return the name of the available devices
	 * 
	 * @return An array with the name of each device
	 */
	private static String[] getDevices(){
		Archivo a = checkFile("Devices",false);
		try {
			LinkedList<String> devices = a.LeerCompleto();
			String[] returned = new String[devices.size()];
			int i = 0;
			System.out.println("Devices to load =>");
			for(String name : devices){
				System.out.println("\t '- Device "+name);
				returned[i] = name;
				i++;
			}
			System.out.println();
			return returned;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error, unable to get devices name on file");
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error, unable to get devices name on file");
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error, unable to get devices name on file");
			System.exit(0);
		}
		return null;
	}
	
	/**
	 * This Method validate the broker URI, at this moment, just validate that
	 * exist a type of connection, by example "TCP", then an valid ip and then
	 * a port, so, if you want to make sure that my client will accept your
	 * broker URI, the you must to pass somethink like this : tcp://10.195.17.114:61616 or tcp://localhost:61616
	 * 
	 * @param uri The address to connect with ActiveMQ server
	 * @return <li> true :  </li>
	 */
	private static boolean isAValidBrokerUri(String uri){
		String[] x = uri.split(":");
		if(x.length < 3){
			return false;
		}else{
			String ip = x[1];
			return isIp(ip.substring(2));
		}
	}
	
	/**
	 * This is the main method that initialize all files and try to connect first to the Manager using an
	 * AdvancedComponentClient
	 * 
	 * @param args {[1]=ACSInstance , [2]=ManagerIP , [3]= Time to refresh the information} or {[1]=-h to see some help}
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// If there is not arguments or the arguments are '-h'
		if (args.length < 3 || args[0].equalsIgnoreCase("-h")) {
			PrintUsage();
			System.exit(0);
			// If the first argument is not a number (ACSInstance)
		} else if (!isNumber(args[0])) {
			System.out.println("You must to put the AcsInstance (a valid number) on first argument, please don't put strings or another thing");
			System.exit(1);
			// If the second argument is no a valid IP
		} else if (!isIp(args[1])) {
			System.out.println("Please put an correct IPV4 as second argument");
			System.exit(1);
		} else if (!isNumber(args[2])){
			System.out.println("Please put a correct number that represent time to refresh the information on seconds");
			System.exit(1);
		} else if(!isAValidBrokerUri(args[3])){
			System.out.println("Please put a correct broker uri");
			System.exit(1);
		} else {
			// getting Antenna and device names
			String[] antennas = getAntennaNames();
			String[] devices = getDevices();
			// Creating the files to write information
			HashMap<String,Archivo> Files = InitializeAllFiles(antennas, devices);
			String ip = args[1];
			Publisher publisher = null;
			do {
				try {
					String brokerURI = args[3];
					// Trying to connect to manager on some way
					publisher = new Publisher(getManagerLoc(0,ip),"ACS Client Status", Files, Long.parseLong(args[2])*1000,antennas,devices,brokerURI);
					resetFile("cacheFile");
					Files.get("cacheFile").Escribir("{\"Type\":\"ACSStatus\",\"Status\":\"Up\"}",true);
				} catch (Exception e) {
					resetFile("cacheFile");
					System.out.println("Path "+Files.get("cacheFile").getPath());
					Files.get("cacheFile").Escribir("{\"Type\":\"ReportInfo\",\"OnlineInfo\":"+false+",\"Date\":\""+getDate()+"\",\"Iteration\":\""+ 0 + "\",\"Status\":[",true);
					Files.get("cacheFile").Escribir("{\"Type\":\"ManagerError\",\"Name\":\"Manager\",\"Status\":\"" + e.getMessage() + "\"},",false);
					Files.get("cacheFile").Escribir("{\"Type\":\"ACSStatus\",\"Name\":\"ACS Status\",\"Status\":\"Down\"},",false);
					Files.get("cacheFile").Escribir("{\"Type\":\"AlmaStatus\",\"Name\":\"Alma Status\",\"Status\":\"Down\"},",false);
					Files.get("cacheFile").Escribir("{\"Type\":\"OpServerStatus\",\"Name\":\"OpServer Status\",\"Status\":\"Down\"}",false);
					Files.get("cacheFile").Escribir("]}",false);
					Thread.sleep(10000);
				}
				Thread publisherThread = new Thread(publisher);
				publisherThread.run();
				publisher = null;
				System.out.println("\n\t #### Start again ####");
			} while (true);
		}
	}
	
	/**
	 * This method create the date to add to the report
	 * 
	 * @return A string with the actual date on the following format "yyyy'/'MM'/'dd 'at' hh:mm:ss z", example = 2011/02/04 at 09:35:39 UTC
	 */
	private static String getDate() {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy'/'MM'/'dd 'at' hh:mm:ss z");
	    return sdf.format(cal.getTime());
	}

}
