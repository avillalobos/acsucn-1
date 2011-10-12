package acsucn.eclipse.plugin.core.projectpreferences;

/**
 * Singleton class for acs path utilities
 * @author avillalobos
 */
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;


public class AcsPathUtilities {
	
	public static AcsPathUtilities PathUtilities;
	
	public static AcsPathUtilities getInstance(){
		if(PathUtilities == null){
			PathUtilities = new AcsPathUtilities();
		}
		return PathUtilities;
	}
	
	public static String ACSROOTpath(){
		if(PathUtilities == null){
			getInstance();
		}
		return getGenericPath("ACSROOT");
	}
	
	public static String INTROOTpath(){
		if(PathUtilities == null){
			getInstance();
		}
		return getGenericPath("INTROOT");
	}
	
	public static String JACORBpath(){
		if(PathUtilities == null){
			getInstance();
		}
		return getGenericPath("JACORB_HOME");
	}
	
	public static String getDefaultPath(String var){
		if(PathUtilities == null){
			getInstance();
		}
		return PathUtilities.getPathOf(var);
	}
	
	private static String getGenericPath(String name){
		IProject project = acsucn.eclipse.plugin.graphics.projectpreferences.ViewSelectionListener.getCurrentProject(); 
		if(project != null && project.getPathVariableManager().getURIValue(name) != null){
			return project.getPathVariableManager().getURIValue(name).toString();
		}else{
			return PathUtilities.getPathOf(name);
		}
	}
	
	// variable, path
	private HashMap<String,String> Variables;
	
	private ACSClasspathContainer ClassPathContainer;

	public AcsPathUtilities(){
		Variables = new HashMap<String,String>();
		ArrayList<String> extensions = new ArrayList<String>();
		// TODO extensions to import, because by default will always accept jars files
		extensions.add("jar");
		this.ClassPathContainer = new ACSClasspathContainer(extensions);
		setDefault();
	}
	
	private void setDefault(){
		for(AcsCommonVariables var : AcsCommonVariables.values()){
			Variables.put(var.getTypeOfEnviromentVariable(), var.getPath(var));
		}
	}
	
	public HashMap<String,String> getAcsVariables(){
		return this.Variables;
	}
	
	public String getPathOf(String name){
		if(this.Variables.containsKey(name)){
			return this.Variables.get(name);
		}else{
			return "path for " + name +" not found";
		}
	}
	
	public boolean setPathOf(String name, String value){
		if(this.Variables.containsKey(name)){
			if(this.Variables.put(name, value) != null)
				return true;
			else
				return false;
		}else{
			return false;
		}
	}
	
	public String[] getAcsPaths(){
		if(PathUtilities == null){
			getInstance();
		}
		String[] values = new String[this.Variables.size()];
		this.Variables.values().toArray(values);
		return values;
	}
	
	public HashMap<String,String>[] jarFinder(String name){
		// The java classes in each jar file
		Vector<String> javaClasses = new Vector<String>();
		LinkedList<HashMap<String,String>> jars = new LinkedList<HashMap<String,String>>();
		
		String[] paths = getAcsPaths();
		
		for (String path:  paths) {
			
			JarFolder folder = new JarFolder(new File(path+"/lib"));
			File[] jarFiles = folder.getJarFiles();
			for (File jar: jarFiles) {
				JarFileHelper jarFH=null;
				try {
					jarFH = new JarFileHelper(jar);
				} catch (Throwable t) {
					System.out.println("Error: "+t.getMessage());
					t.printStackTrace();
					continue;
				}
				javaClasses.clear();
				try {
					jarFH.getClasses(javaClasses);
					Vector<String> asdf = (Vector<String>) jarFH.getMatchingClasses(name);
					if(asdf != null){
						for(String str: asdf){
							HashMap<String,String> hm = new HashMap<String,String>();
							hm.put("classname", str);
							hm.put("jarfile", jarFH.getName());
							jars.add(hm);
						}
					}
				} catch (Throwable t) {
					System.out.println("Error: "+t.getMessage());
					t.printStackTrace();
					continue;
				}
			}
			
		}
		if(jars.isEmpty()){
			return null;
		}else{
			@SuppressWarnings("unchecked")
			HashMap<String,String>[] values = new HashMap[jars.size()];
			jars.toArray(values);
			return values;
		}
	}
	
	public boolean validatePath(Path path){
		File f = new File(path.toOSString());
		return f.exists();
	}
	
	public void addNewVariable(String name, String path){
		this.Variables.put(name, path);
	}
	
	public void removeVariable(String name){
		this.Variables.remove(name);
	}

	public ACSClasspathContainer getClassPathContainer() {
		return ClassPathContainer;
	}
}
