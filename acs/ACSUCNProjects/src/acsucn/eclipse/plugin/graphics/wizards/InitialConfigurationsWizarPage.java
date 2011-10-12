/**
 * @author avillalobos
 */

package acsucn.eclipse.plugin.graphics.wizards;

import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import acsucn.eclipse.plugin.core.projectpreferences.AcsPathUtilities;

public class InitialConfigurationsWizarPage extends WizardNewProjectCreationPage {

	private Text acsrootloc,introotloc,jacorbloc,filteredResources;
	private Button acsrootBtn,introotBtn,jacorbBtn;

	
	/**
	 * @param DirectoryDialog This variable is used to look for the directory on the 
	 * system to set a new folder for a specific ACS Path
	 */
	private DirectoryDialog DirectoryDialog ;
	
	/**
	 * Listener for browse button
	 */
	private SelectionListener ButtonListener = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			if(e.widget == acsrootBtn){
				DirectoryDialog.setText("Select ACSROOT classpath folder");
				DirectoryDialog.setFilterPath("ACSROOT");
				DirectoryDialog.setMessage("Set the path of a ACS classpath");
				String path =DirectoryDialog.open();
				if(path != null){
					acsrootloc.setText(path);
				}else{
					return;
				}
			}else if(e.widget == introotBtn){
				DirectoryDialog.setText("Select INTROOT classpath folder");
				DirectoryDialog.setFilterPath("INTROOT");
				DirectoryDialog.setMessage("Set the path of a ACS classpath");
				String path =DirectoryDialog.open();
				if(path != null){
					introotloc.setText(path);
				}else{
					return;
				}
			}else if(e.widget == jacorbBtn){
				DirectoryDialog.setText("Select INTROOT classpath folder");
				DirectoryDialog.setFilterPath("INTROOT");
				DirectoryDialog.setMessage("Set the path of a ACS classpath");
				String path =DirectoryDialog.open();
				if(path != null){
					jacorbloc.setText(path);
				}else{
					return;
				}
			}
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**
	 * Constructor
	 * 
	 * @param pageName The name of this wizard
	 */
	protected InitialConfigurationsWizarPage(String pageName) {
		super(pageName);
		setTitle(ACSWizardMessages.InitialPageTitle);
		setDescription(ACSWizardMessages.InitialDescription);
		setInitialProjectName("ACSJavaProject");
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		System.out.println("creando control");
		
		Composite container = new Composite(parent, SWT.FILL);
		container.setLayout(new GridLayout());
		
		Group project = new Group(container,SWT.FILL);
		project.setText("Define the project name and location");
		project.setLayout(new GridLayout(1,true));
		project.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		super.createControl(project);
		
		Group properties = new Group(container,SWT.FILL);
		properties.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		properties.setText("Define specific ACS Properties");
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		properties.setLayout(layout);
		
		DirectoryDialog = new DirectoryDialog(container.getShell());
		
		createClassPathsUI(properties);
		createFileteredResources(properties);
		setControl(container);
	}

	private void createClassPathsUI(Composite container){
		// DataGrid
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.horizontalIndent = 20;
		
		// ACSROOT Properties
		Label acsroot = new Label(container, SWT.NONE);
		acsrootloc = new Text(container, SWT.BORDER);
		acsrootloc.setEditable(true);
		acsroot.setText("ACSROOT");
		acsrootloc.setLayoutData(data);
		
		acsrootloc.setText(AcsPathUtilities.ACSROOTpath());
		
		acsrootBtn = new Button(container,SWT.PUSH);
		acsrootBtn.setText("Select...");
		acsrootBtn.addSelectionListener(ButtonListener);
		
		// INTROOT Properties
		
		Label introot = new Label(container, SWT.NONE);
		introotloc = new Text(container, SWT.BORDER);
		introot.setText("INTROOT");
		introotloc.setLayoutData(data);
		introotloc.setText(AcsPathUtilities.INTROOTpath());
		
		introotBtn = new Button(container,SWT.PUSH);
		introotBtn.setText("Select...");
		introotBtn.addSelectionListener(ButtonListener);
		
		// JACORB_HOME Properties
		
		Label jacorb = new Label(container, SWT.NONE);
		jacorbloc = new Text(container, SWT.BORDER);
		jacorb.setText("JACORB_HOME");
		jacorbloc.setLayoutData(data);
		jacorbloc.setText(AcsPathUtilities.JACORBpath());
		
		jacorbBtn = new Button(container,SWT.PUSH);
		jacorbBtn.setText("Select...");
		jacorbBtn.addSelectionListener(ButtonListener);
	}
	
	private void createFileteredResources(Composite container){
		// DataGrid
		GridData data = new GridData(200,20);
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.horizontalIndent = 20;
		data.horizontalSpan = 2;
		
		Label label = new Label(container, SWT.NULL);
		label.setText("Filtered Resources:");
		
		filteredResources = new Text(container,SWT.BORDER);
		filteredResources.setLayoutData(data);
		
	}
	
	public IWizardContainer getCustomContainer(){
		return getContainer();
	}
	
	public boolean verifyClassPaths(){
		return verifyAcsRoot() && verifyIntRoot() && verifyJacorb();
	}
	
	private boolean verifyAcsRoot(){
		if(this.acsrootloc.getText() != null && !this.acsrootloc.getText().equalsIgnoreCase(""))
			return true;
		else
			return false;		
	}
	
	private boolean verifyIntRoot(){
		if(this.introotloc.getText() != null && !this.introotloc.getText().equalsIgnoreCase(""))
			return true;
		else
			return false;		
	}
	
	private boolean verifyJacorb(){
		if(this.jacorbloc.getText() != null && !this.jacorbloc.getText().equalsIgnoreCase(""))
			return true;
		else
			return false;		
	}
	
	public String getACSROOT(){
		return this.acsrootloc.getText();
	}
	
	public String getINTROOT(){
		return this.introotloc.getText();
	}
	
	public String getJACORB_HOME(){
		return this.jacorbloc.getText();
	}
}
