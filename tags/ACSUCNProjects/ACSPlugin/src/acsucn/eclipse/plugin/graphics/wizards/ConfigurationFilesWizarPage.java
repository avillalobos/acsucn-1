/**
 * @author avillalobos
 */

package acsucn.eclipse.plugin.graphics.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ConfigurationFilesWizarPage extends WizardPage {

	public ConfigurationFilesWizarPage(String pageName) {
		super(pageName);
		setTitle(ACSWizardMessages.ConfigurationPageTitle);
		setDescription(ACSWizardMessages.ConfigurationDescription);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		
		Label text = new Label(container,SWT.NULL);
		text.setText("This will be defined on another iteration");
		
		setControl(container);
	}

}
