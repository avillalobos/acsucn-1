package MainTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class PseudoProxy{


	public static String recoverPathFile(){
		String cmd[] = new String[]{"bash","-c","echo $AXIS2_HOME/cacheFile"};
		String path = "";
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s = null;
			
			while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                path = path + s;
			}
			if(path == null || path.length() == 0){
				return "Path not found";
			}else{
				return path;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "Unable to recover path";
		}
	}
	
	public static void resetFile(){
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
	
	public static void createCacheFile(){
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
	
	public static String getManagerLoc(){
		return "corbaloc::10.195.17.114:3000/Manager";
	}
	
	public static void printStatus(LinkedList<String> listStatus){
		for(String status : listStatus){
			if(status != null){
				System.out.println(status);
			}else{
				System.out.println("No data found");
			}
		}
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception{
		Archivo archivo =null;
		String pathFile = recoverPathFile();
		if(pathFile.equalsIgnoreCase("Path not found") || pathFile.equalsIgnoreCase("Unable to recover path")){
			//publisher.writeWarningMessage(pathFile);
			//publisher.writeWarningMessage("Please ensure that cacheFile are on $AXIS2_HOME/repository/cacheFile or that enviroment variable AXIS2_HOME exist");
			System.exit(-1);
		}else{
			archivo = new Archivo(pathFile);
			archivo.Escritura();
			Publisher publisher = new Publisher(getManagerLoc(),"ACS Client Status",archivo,(long) 1000);
			Thread publisherThread = new Thread(publisher);
			publisher.writeFineMessage("The cache file was found on " + pathFile);
			publisherThread.run();
			//archivo.Escribir(publisher.getStatus());
			//printStatus(ws.getStatus());
		}
	}
}
