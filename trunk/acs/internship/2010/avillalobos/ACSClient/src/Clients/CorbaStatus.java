package Clients;

import java.util.LinkedList;

import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;

import alma.acs.commandcenter.meta.Firestarter;
import alma.acs.component.client.AdvancedComponentClient;

public class CorbaStatus extends ACSClient {

	private Firestarter firestarter;
	public CorbaStatus(AdvancedComponentClient _client, Firestarter _firestarter) {
		super(_client);
		this.firestarter = _firestarter;
		// TODO Auto-generated constructor stub
	}

	@Override
	public LinkedList<String> getStatus() {
		LinkedList<String> report = new LinkedList<String>();
		try {
			ORB orb = this.firestarter.giveOrb();
			orb.list_initial_services();
			report.add("{Type:'CorbaSystem',Status:'Ok'}");
		} catch (SystemException exc) {
			// org.omg.CORBA.SystemException is what we expect in the "ordinary" error case
			report.add("{Type:'CorbaSystem',Status:'Error'}");
		} catch (Throwable t) {
			// any other error is not foreseen
			report.add("{Type:'CorbaSystem',Status:'Unknown'}");
		}
		return report;
	}

	

}
