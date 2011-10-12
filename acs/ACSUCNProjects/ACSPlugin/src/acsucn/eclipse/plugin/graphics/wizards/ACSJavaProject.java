/**
 * @author avillalobos
 */

package acsucn.eclipse.plugin.graphics.wizards;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import acsucn.eclipse.plugin.core.projectstructure.ACSProjectStructure;
import acsucn.eclipse.plugin.core.projectstructure.OperativeSystem;

public class ACSJavaProject extends Wizard implements INewWizard {

	private InitialConfigurationsWizarPage InitialConfigurations;
	private PrototypesMethodsWizardPage PrototypesMethods;
	private ConfigurationFilesWizarPage ConfigurationFiles;
	private static final String INITIAL_CONFIGURATIONS_NAME = "ACS Project configuration"; //$NON-NLS-1$
	private static final String PROTOTYPES_METHODS_NAME = "Create idl functions";
	private static final String CONFIGURATION_FILES_NAME = "Set ACS configuration files";
	private static final String WIZARD_JAVA_PROJECT_NAME = "ACS Java Projects"; //$NON-NLS-1$
	private IWorkbench workbench;
	private IConfigurationElement config;
	private IProject project;
	
	/**
	 * Constructor
	 */
	public ACSJavaProject() {
		// TODO Auto-generated constructor stub
		setWindowTitle(WIZARD_JAVA_PROJECT_NAME);
	}
	
	/**
	 * this method is called after to init method
	 */
	@Override
	public void addPages() {
	    super.addPages();

	    InitialConfigurations = new InitialConfigurationsWizarPage(INITIAL_CONFIGURATIONS_NAME);
	    PrototypesMethods = new PrototypesMethodsWizardPage(PROTOTYPES_METHODS_NAME);
	    ConfigurationFiles = new ConfigurationFilesWizarPage(CONFIGURATION_FILES_NAME);

	    addPage(InitialConfigurations);
	    addPage(PrototypesMethods);
	    
	    //addPage(ConfigurationFiles);
	}

	/**
	 * Init method called after to constructor
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		System.out.println("init!!!");
		this.workbench = workbench;
	}

	/**
	 * Method used when finish button is clicked
	 */
	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		System.out.println("Creando el proyecto");
		if (project != null) {
			return true;
		}

		final IProject projectHandle = this.InitialConfigurations.getProjectHandle();
		
		URI projectURI = (!this.InitialConfigurations.useDefaults()) ? this.InitialConfigurations.getLocationURI() : null;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription desc = workspace.newProjectDescription(projectHandle.getName());
		desc.setLocationURI(projectURI);
		
		/*
		 * Just like the NewFileWizard, but this time with an operation object
		 * that modifies workspaces.
		 */
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor) throws CoreException {
				createProject(desc, projectHandle, monitor);
			}
		};

		/*
		 * This isn't as robust as the code in the BasicNewProjectResourceWizard
		 * class. Consider beefing this up to improve error handling.
		 */
		try {
			this.InitialConfigurations.getCustomContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		
		project = projectHandle;

		if (project == null) {
			return false;
		}

		IProjectDescription description = null;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] natures = description.getNatureIds();

		String[] newNatures = new String[natures.length + 1];

		System.arraycopy(natures, 0, newNatures, 0, natures.length);

		newNatures[natures.length] =JavaCore.NATURE_ID;

		description.setNatureIds(newNatures);
		
		IJavaProject javaProject = JavaCore.create(project);

		try {
			project.setDescription(description, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Set<IClasspathEntry> entries = new HashSet<IClasspathEntry>();
		
		entries.add(JavaCore.newSourceEntry((Path) this.project.getFullPath().append("/src")));
		entries.add(JavaRuntime.getDefaultJREContainerEntry());

		try {
			javaProject.setRawClasspath((IClasspathEntry[]) entries.toArray(new IClasspathEntry[entries.size()]), new NullProgressMonitor());
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			projectHandle.getPathVariableManager().setURIValue("ACSROOT", new URI(this.InitialConfigurations.getACSROOT()));
			projectHandle.getPathVariableManager().setURIValue("INTROOT", new URI(this.InitialConfigurations.getINTROOT()));
			projectHandle.getPathVariableManager().setURIValue("JACORB_HOME", new URI(this.InitialConfigurations.getJACORB_HOME()));
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BasicNewProjectResourceWizard.updatePerspective(config);
		BasicNewProjectResourceWizard.selectAndReveal(project, workbench.getActiveWorkbenchWindow());

		return true;
	}

	/**
	 * This creates the project in the workspace.
	 * 
	 * @param description
	 * @param projectHandle
	 * @param monitor
	 * @throws CoreException
	 * @throws OperationCanceledException
	 */
	void createProject(IProjectDescription description, IProject proj,IProgressMonitor monitor) throws CoreException,OperationCanceledException {
		try {

			monitor.beginTask("", 2000);

			proj.create(description, new SubProgressMonitor(monitor, 1000));

			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			proj.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1000));
			ACSProjectStructure newproject;
			if(isWindows())
				newproject = new ACSProjectStructure(OperativeSystem.Windows, proj.getName());
			else if (isUnix())
				newproject = new ACSProjectStructure(OperativeSystem.Linux, proj.getName());
			else if (isMac())
				newproject = new ACSProjectStructure(OperativeSystem.Mac, proj.getName());
			else
				return;
			newproject.createProjectStructure();
		} finally {
			monitor.done();
		}
	}

	public static boolean isWindows(){
		 
		String os = System.getProperty("os.name").toLowerCase();
		//windows
	    return (os.indexOf( "win" ) >= 0); 
 
	}
 
	public static boolean isMac(){
 
		String os = System.getProperty("os.name").toLowerCase();
		//Mac
	    return (os.indexOf( "mac" ) >= 0); 
 
	}
 
	public static boolean isUnix(){
 
		String os = System.getProperty("os.name").toLowerCase();
		//linux or unix
	    return (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0);
 
	}
}
