
import java.util.logging.Level;
import java.util.logging.Logger;

/*
import alma.maciErrType.ComponentNotAlreadyActivatedEx;
import alma.acs.util.IsoDateFormat;
import alma.ACSErrTypeCommon.wrappers.AcsJIllegalArgumentEx;
import org.exolab.castor.core.exceptions.CastorException;
import alma.cdbErrType.CDBXMLErrorEx;
import org.omg.DsLogAdmin.BasicLogOperations;
import alma.Logging.AcsLogServiceOperations;
import alma.maci.loggingconfig.UnnamedLogger;
import si.ijs.maci.ClientPOA;
import alma.acs.logging.AcsLogger;
import alma.acs.container.ContainerServicesBase;
*/
import alma.JavaContainerError.wrappers.AcsJContainerServicesEx;
import alma.acs.component.client.ComponentClient;
import alma.acs.container.corba.AcsCorba;

public class JavaClient extends ComponentClient {

	private  JavaClient jc;
	
	// Constructor of JavaClient using only info for ComponentClient
	protected JavaClient(Logger logger, String managerLoc, String clientName,
			AcsCorba externalAcsCorba) throws Exception {
		super(logger, managerLoc, clientName, externalAcsCorba);
		// TODO Auto-generated constructor stub
	}
	
	public void doSomeStuff() throws AcsJContainerServicesEx{
        this.jc = (JavaClient) getContainerServices().getComponent("Meteorologic");
        System.out.println("Obteniendo el componente Meteorologic");
	}


	public static void main(String args[]) throws Exception{
		System.out.println("Hello world");
		String managerLoc = System.getProperty("ACS.manager");
		System.out.println("managerloc = " + managerLoc);
		String clientName = "Meteorologic";
		JavaClient javaclient = new JavaClient(null,managerLoc,clientName, null);
		javaclient.doSomeStuff();
	}
	
	
}
