//TODO Make the correct documentation

package acsucn.eclipse.plugin.graphics.projectpreferences;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

import acsucn.eclipse.plugin.core.projectpreferences.AcsPathUtilities;
import acsucn.eclipse.plugin.core.projectpreferences.Messages;

/**
 * This class help us to make a property page for a java project when we 
 * can set the ACS paths and the extensions for the library to import
 * 
 * @author avillalobos
 *
 */
public class Properties extends PropertyPage implements SelectionListener {

	/**
	 * @param PathsInformation This variable store the common paths for ACS
	 */
	private AcsPathUtilities PathsInformation;
	
	/**
	 * @param DirectoryDialog This variable is used to look for the directory on the 
	 * system to set a new folder for a specific ACS Path
	 */
	private DirectoryDialog DirectoryDialog ;
	
	/**
	 * @param ActualProject This variable indicate the project selected using the
	 * ViewSelectionListener class, because where a selection is made this is stored on a 
	 * static variable and shared with the plugin
	 */
	private IProject ActualProject;

	// Inherit method called when the propertie is executed
	@Override
	protected Control createContents(Composite parent) {
		
		System.out.println("ejecutando createContents");
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);
		PathsInformation = new AcsPathUtilities();
		DirectoryDialog = new DirectoryDialog(composite.getShell());
	
		/*
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		*/
		
		// TODO Get the name of the actual project, i mean, the project that call this preferences page
		this.ActualProject = acsucn.eclipse.plugin.graphics.projectpreferences.ViewSelectionListener.getCurrentProject();
		

		if(this.ActualProject != null){
			System.out.println(Messages.DirLabel +" = " + this.ActualProject.getName().toString());
		}else{
			System.out.println("Fail!!!!");
			return null;
		}
		
		DefaultPaths(composite);
		// TODO Auto-generated method stub
		return composite;
	}
	
	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);
		
		return composite;
	}

	private Text acsrootloc,introotloc,jacorbloc;
	private Button acsrootBtn,introotBtn,jacorbBtn;
	
	private void DefaultPaths(Composite parent){
		Composite composite = createDefaultComposite(parent);
		
		// DataGrid
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.horizontalIndent = 20;
		
		// ACSROOT Properties
		Label acsroot = new Label(composite, SWT.NONE);
		acsrootloc = new Text(composite, SWT.BORDER);
		acsrootloc.setEditable(true);
		acsroot.setText("ACSROOT");
		acsrootloc.setLayoutData(data);
		acsrootloc.setText(AcsPathUtilities.ACSROOTpath());
		
		acsrootBtn = new Button(composite,SWT.PUSH);
		acsrootBtn.setText("Select...");
		acsrootBtn.addSelectionListener(this);
		
		// INTROOT Properties
		
		Label introot = new Label(composite, SWT.NONE);
		introotloc = new Text(composite, SWT.BORDER);
		introot.setText("INTROOT");
		introotloc.setLayoutData(data);
		introotloc.setText(AcsPathUtilities.INTROOTpath());
		
		introotBtn = new Button(composite,SWT.PUSH);
		introotBtn.setText("Select...");
		introotBtn.addSelectionListener(this);
		
		// JACORB_HOME Properties
		
		Label jacorb = new Label(composite, SWT.NONE);
		jacorbloc = new Text(composite, SWT.BORDER);
		jacorb.setText("JACORB_HOME");
		jacorbloc.setLayoutData(data);
		jacorbloc.setText(AcsPathUtilities.JACORBpath());
		
		jacorbBtn = new Button(composite,SWT.PUSH);
		jacorbBtn.setText("Select...");
		jacorbBtn.addSelectionListener(this);
	}
	
	// Method called when Apply button is pressed
	public void performApply(){
		System.out.println("accepting changes");
		// This classpath is for all enviroment, not for the project
		try {
			this.ActualProject.getPathVariableManager().setURIValue("ACSROOT",new URI(this.PathsInformation.getPathOf("ACSROOT")));
			this.ActualProject.getPathVariableManager().setURIValue("INTROOT",new URI(this.PathsInformation.getPathOf("INTROOT")));
			this.ActualProject.getPathVariableManager().setURIValue("JACORB_HOME", new URI(this.PathsInformation.getPathOf("JACORB_HOME")));
			
			//UpdateProjectClassPath(new Path[] {new Path("/alma/ACS-9.0/ACSSW/lib/jACSUtil.jar"),new Path("/alma/ACS-9.0/ACSSW/lib/xdb.jar")});

			/*
			JavaCore.setClasspathVariable("ACSROOT", new Path(ACSROOTpath()),null);
			JavaCore.setClasspathVariable("INTROOT", new Path(INTROOTpath()),null);
			JavaCore.setClasspathVariable("JACORB_HOME", new Path(JACORBpath()),null);
			*/
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		
		// TODO Auto-generated method stub
		if(e.widget == this.acsrootBtn){
			DirectoryDialog.setText("Select ACSROOT classpath folder");
			DirectoryDialog.setFilterPath("ACSROOT");
			DirectoryDialog.setMessage("Set the path of a ACS classpath");
			String path =DirectoryDialog.open(); 
			if(path != null){
				this.acsrootloc.setText(path);
				this.PathsInformation.setPathOf("ACSROOT", path);
			}else{
				return;
			}
		}else if(e.widget == this.introotBtn){
			DirectoryDialog.setText("Select INTROOT classpath folder");
			DirectoryDialog.setFilterPath("INTROOT");
			DirectoryDialog.setMessage("Set the path of a ACS classpath");
			String path =DirectoryDialog.open(); 
			if(path != null){
				this.introotloc.setText(path);
				this.PathsInformation.setPathOf("INTROOT", path);
			}else{
				return;
			}
		}else if(e.widget == this.jacorbBtn){
			DirectoryDialog.setText("Select JACORB_HOME classpath folder");
			DirectoryDialog.setFilterPath("JACORB_HOME");
			DirectoryDialog.setMessage("Set the path of a ACS classpath");
			String path =DirectoryDialog.open(); 
			if(path != null){
				this.jacorbloc.setText(path);
				this.PathsInformation.setPathOf("JACORB_HOME", path);
			}else{
				return;
			}
		}else{
			System.out.println("Is fucking nothing!!");
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
