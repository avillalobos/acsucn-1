package acsucn.eclipse.plugin.core.projectpreferences;

/**
 * 
 * @author avillalobos
 *
 */
public enum AcsCommonVariables {
	ACSROOT("ACSROOT"),
	INTROOT("INTROOT"),
	JACORB_HOME("JACORB_HOME");	
	
	private String enviromentName;
	
	AcsCommonVariables(String variable){
		this.enviromentName = variable;
	}
	
	String getTypeOfEnviromentVariable(){
		return this.enviromentName;
	}
	
	boolean existEnviromentVariable(AcsCommonVariables var){
		switch(var){
		case ACSROOT:
			return System.getenv("ACSROOT") != null;
		case INTROOT:
			return System.getenv("INTROOT") != null;
		case JACORB_HOME:
			return System.getenv("JACORB_HOME") != null;
		default:
			return false;
		}
	}
	
	String getPath(AcsCommonVariables var){
		
		if (!existEnviromentVariable(var))
			return "enviroment variable not found";
		
		switch(var){
		case ACSROOT:
			return System.getenv("ACSROOT"); 
		case INTROOT:
			return System.getenv("INTROOT");
		case JACORB_HOME:
			return System.getenv("JACORB_HOME");
		default:
			return "Error on enum type";
		}
	}
}

