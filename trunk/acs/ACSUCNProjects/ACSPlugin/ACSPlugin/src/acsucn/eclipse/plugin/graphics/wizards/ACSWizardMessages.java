/**
 * @author avillalobos
 */

package acsucn.eclipse.plugin.graphics.wizards;

import org.eclipse.osgi.util.NLS;

public class ACSWizardMessages extends NLS {
	private static final String BUNDLE_NAME = "acsucn.eclipse.plugin.graphics.wizards.messages"; //$NON-NLS-1$
	public static String InitialPageTitle;
	public static String InitialDescription;
	public static String ProtoypePageTitle;
	public static String PrototypeDescription;
	public static String ConfigurationPageTitle;
	public static String ConfigurationDescription;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ACSWizardMessages.class);
	}

	private ACSWizardMessages() {
	}
}
