package acsucn.eclipse.plugin.core.projectstructure;

import java.io.*;
import java.util.ArrayList;

import org.eclipse.core.runtime.Platform;

import acsucn.eclipse.plugin.graphics.wizards.PrototypesMethodsWizardPage;

public class ACSTemplateFiles {
	
	private OperativeSystem OS;
	private String ProjectName;
	
	public ACSTemplateFiles(OperativeSystem _OS, String projectName)
	{
		this.OS = _OS;
		this.ProjectName = projectName;
	}
	
	/**
	 * Method that create basic file of ACS
	 * 
	 * @author Santiago Sanchez
	 */
		
	public boolean CreateFiles()
	{
		boolean result = false;
		String pathFileAcs="";
		String firstPath ="";
		String finalPath ="";
		String WorkSpace = Platform.getLocation().toFile().getAbsolutePath();
		String Separator = this.OS.getDirectorySeparator();
		String FullPathProjectDirectory = WorkSpace
		+ this.OS.getDirectorySeparator() + this.ProjectName;
		
		
		for (int i = 1 ; i < 6 ; i ++ )
		{
			result = this.createDirectory(FullPathProjectDirectory + this.getDirectoryName(i));
			if (result = true)
			{
				String path = FullPathProjectDirectory + this.getDirectoryName(i) + this.getFile(i);
				boolean respuesta = this.createDirectory(FullPathProjectDirectory+ this.getDirectoryName(i));
				if (respuesta == false) {
					firstPath = ACSTemplateFiles.class.getProtectionDomain().getCodeSource().getLocation().toString();
					finalPath = firstPath.substring(6,firstPath.length());
					switch (i){
						case 1:
							//.H
							pathFileAcs = finalPath + "Config/ImplH.config";
							WriteFile(pathFileAcs,path,this.ProjectName,null);
							break;
						case 2:
							//.IDL
							pathFileAcs = finalPath + "Config/Idl.config";
							WriteFile(pathFileAcs,path,this.ProjectName,PrototypesMethodsWizardPage.methodList);
							PrototypesMethodsWizardPage.auxList=null;
							PrototypesMethodsWizardPage.methodList=null;
							break;
						case 3:
							//.CPP
							pathFileAcs = finalPath + "Config/Cpp.config";
							WriteFile(pathFileAcs,path,this.ProjectName,null);
							break;
						case 4:
							//.XSD
							pathFileAcs = finalPath + "Config/Xsd.config";
							WriteFile(pathFileAcs,path,this.ProjectName,null);
							break;
						case 5:
							//.JAVA
							pathFileAcs = finalPath + "Config/Java.config";
							WriteFile(pathFileAcs,path,this.ProjectName,null);
							break;
					}
				}
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method read file of configuration for create basic file of acs and return 
	 * a ArrayList with lines read
	 * 
	 * @param  path of configuration file 
	 * @author Santiago Sanchez
	 */
	private ArrayList ReadFile (String path){
		      File archivo = null;
		      FileReader fr = null;
		      BufferedReader br = null;
		      ArrayList Lista = new ArrayList();
		      
		      try {
		    	  // Open the file and to creating BufferedReader
		    	  // Make a comfortable reading (having the method readLine ()).
		         archivo = new File (path);
		         fr = new FileReader (archivo);
		         br = new BufferedReader(fr);

		         //Reading File
		         String linea;
		         while((linea=br.readLine())!=null){
		            Lista.add(linea);
		         }
		         fr.close();
		         return Lista;
		      }
		      catch(Exception e){
		         e.printStackTrace();
		      }finally{
		    	  // The finally close the file, to ensure
		    	  // Is closed if all goes as well as whether jumping
		    	  // An exception.
		         try{                    
		            if( null != fr ){   
		               fr.close();     
		            }                  
		         }catch (Exception e2){ 
		            e2.printStackTrace();
		         }
		      }
		      return null;
		}
	
	/**
	 * Method confirm if directory has been created and return a 
	 * boolean type True:directory created False:missing directory
	 * 
	 * @param  path of directory
	 * @author Santiago Sanchez
	 */
	
	private boolean createDirectory(String path)
	{
		try {
			File Directorio = new File(path);

			// The root directory does not exist Check if the directory created
			// Exists
			if (Directorio.mkdirs()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception ex) {
			System.out.print("Se ha producido un error" + ex);
			return false;
		}
	}
	
	/**
	 * Method return directory name
	 * 
	 * @param  dir : index of directory
	 * @author Santiago Sanchez
	 */
	private String getDirectoryName(int dir)
	{
		String ReturnDirectory = "";
		switch (dir) {
		case 1:
			return "/" + "include";
		case 2:
			return "/" + "idl";
		case 3:
			return "/" + "lib";
		case 4:
			return "/" + "src";
		case 5:
			return "/" + "src";
		}
		return ReturnDirectory;
	}
	
	/**
	 * Method return file name
	 * 
	 * @param  dir : index of file
	 * @author Santiago Sanchez
	 */
	private String getFile(int dir)
	{
		String ReturnDirectory = "";
		switch (dir) {
		case 1:
			return "/" + this.ProjectName + "Impl.h";
		case 2:
			return "/" + this.ProjectName + ".idl";
		case 3:
			return "/" + this.ProjectName + ".xsd";
		case 4:
			return "/" + this.ProjectName + "Impl.cpp";
		case 5:
			return "/" + this.ProjectName + "Impl.java";
		}	
		return ReturnDirectory;
	}
	
	/**
	 * Method write file of configuration for create basic file of acs 
	 * 
	 * @param  pathFileAcs   : path for write basic file to acs
	 * @param  pathRead      : path for read file configuration
	 * @param  pathFileAcs   : project name
	 * @param  lista         : container IDL method
	 * @author Santiago Sanchez
	 */
	private void WriteFile (String pathRead,String pathFileAcs,String nameProject,ArrayList lista)
	{
		FileWriter fichero = null;
        PrintWriter pw = null;
        ArrayList newRead = new ArrayList();
        String linea = "";
        String lineFile="";
        String auxLine="";
        
        newRead = this.ReadFile(pathRead);
        
        try
        {
            fichero = new FileWriter(pathFileAcs,true);
            pw = new PrintWriter(fichero);
           	
            	//Bucle for line that reads
	            for ( int i = 0 ; i < newRead.size(); i++){
	            	linea = (String)newRead.get(i);
	            	int j;
	            	
	            	//Bucle for char that reads
	            	for ( j=0;j<linea.length();j++){
	            		if (linea.charAt(j) == '@'){
	            			for(int x = j ; x < j+ 13 ; x++){
		            			auxLine = auxLine + linea.charAt(x);
		            			if (auxLine.equals("@nameProject")){
		            				lineFile = lineFile  + nameProject;
		                    		j= j+12;
		                    		break;
		            			}
		            		}
	            		}
	            	        		
	            		lineFile = lineFile + linea.charAt(j);
	            		auxLine="";
	            	}
	            	pw.println(lineFile);
            		lineFile="";
		            if ( lista != null){
		            	// Code to write the functions in the IDL
		            	if(i == newRead.size()-4){
		            		for(int z = 0 ; z < lista.size();z++){
		           				pw.println((String)lista.get(z));
		           			}
		           		}
		           	}
	            } 
	            fichero.close();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try { 
        	// Make sure the file is closed.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	}
	
}

