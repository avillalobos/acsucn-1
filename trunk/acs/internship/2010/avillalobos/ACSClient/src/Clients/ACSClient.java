package Clients;

import java.util.LinkedList;

import alma.acs.component.client.AdvancedComponentClient;

/**
 * This class have the common method to write something on ACS Logger, also, is used to generalize 
 * the kind of report, thats why, all that you need to integrate another new status is extends this class
 * and later add a new instance of the extended class to the corresponding list, Online or Offline info.
 * 
 * @author Andres Villalobos, 2011 Summerjob, Ingenieria de ejecucion en Computacion e Informatica, Universidad Catolica del norte
 * @see	DeveloperInfo
 *		The project  <a href="http://almasw.hq.eso.org/almasw/bin/view/JAO/OfflineToolsSummerJob">Twiki page</a> and
 * 		my <a href="http://almasw.hq.eso.org/almasw/bin/view/Main/AndresVillalobosDailyLogOfflineTools">Dailylog</a> please
 * 		Contact to a.e.v.r.007@gmail.com
 */
public abstract class ACSClient{

	/**
	 * @param client This client allow to the extended class get container services, get the orb reference
	 * to the corba status, make ping to the manager to know if ACS is Up or down and also allow to 
	 * write message on the Logger
	 */
	protected AdvancedComponentClient client;
	
	/**
	 * Constructor of ACSClient, just initialize the AdvancedComponentClient
	 * 
	 * @param _client
	 */
	public ACSClient(AdvancedComponentClient _client){
		this.client = _client;
	}
	
	/**
	 * This abstract method allow to generalize the way of get the status information
	 * 
	 * @return A linked list with Strings that contains the status to write on the file
	 */
	public abstract LinkedList<String> getStatus();
	
	/**
	 * This method write a info message on ACS Logger
	 * 
	 * @param msg The message to write on logger
	 */
	public void writeInfoMessage(String msg){
		this.client.getContainerServices().getLogger().info(msg);
	}
	
	/**
	 * This method write a warning message on ACS Logger
	 * 
	 * @param msg The message to write on logger
	 */
	public void writeWarningMessage(String msg){
		this.client.getContainerServices().getLogger().warning(msg);
	}

	/**
	 * This method write a Fine message on ACS Logger
	 * 
	 * @param msg The message to write on logger
	 */
	public void writeFineMessage(String msg){
		this.client.getContainerServices().getLogger().info(msg);
	}
	/**
	 * This method make the ping to the Manager
	 * @return <li> true  : If manager is up   </li>
	 * 		   <li> false : If manager is down </li>
	 */
	protected boolean isAcsOnline(){
		return this.client.getAcsManagerProxy().pingManager(100);
	}
}
