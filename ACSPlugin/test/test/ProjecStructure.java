package test;

import acsucn.eclipse.plugin.core.projectstructure.*;

public class ProjecStructure {
	public static void main(String args[]){
		
		// This should get the workspace location
		//System.out.println(Platform.getLocation().toFile().getAbsolutePath());
		
		ACSProjectStructure cps = new ACSProjectStructure(OperativeSystem.Linux,"TestProject");
		cps.createProjectStructure();
	}
}
