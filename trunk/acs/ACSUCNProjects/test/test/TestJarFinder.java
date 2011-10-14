package test;
import java.util.HashMap;

import acsucn.eclipse.plugin.core.projectpreferences.AcsPathUtilities;

public class TestJarFinder {
	public static void main(String args[]){
		HashMap<String,String>[] val = AcsPathUtilities.getInstance().jarFinder("AdvancedComponentClient");
		for(HashMap<String,String> hm : val){
			System.out.println("jar = " + hm.get("jarfile") + " because contain the class " + hm.get("classname"));
		}
	}
}
