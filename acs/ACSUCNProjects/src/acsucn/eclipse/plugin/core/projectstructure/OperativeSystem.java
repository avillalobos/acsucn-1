package acsucn.eclipse.plugin.core.projectstructure;

/**
 * 
 * @author avillalobos
 *
 */
public enum OperativeSystem {
	Windows(1),Linux(2),Mac(3);
	
	private int type;
	OperativeSystem(int _type){
		this.type = _type;
	}
	
	public int getType() {
		return type;
	}
	
	public String getDirectorySeparator(){
		if(type == Windows.type)
			return "\\";
		else if (type == Linux.type)
			return "/";
		else if (type == Mac.type)
			return "/";
		else
			return null;
	}
}
