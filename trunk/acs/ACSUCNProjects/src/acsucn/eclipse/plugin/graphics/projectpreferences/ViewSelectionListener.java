/**
 * @author avillalobos
 */

package acsucn.eclipse.plugin.graphics.projectpreferences;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import acsucn.eclipse.plugin.core.projectpreferences.AcsPathUtilities;

public class ViewSelectionListener extends ViewPart implements SelectionListener{

	private static IProject currentProject;
	private static IJavaProject currentJavaProject;
	
	public static IProject getCurrentProject(){
		return currentProject;
	}
	
	public static IJavaProject getCurrentProjectJavaProject(){
		return currentJavaProject;
	}
	
	// the listener we register with the selection service 
	private ISelectionListener listener = new ISelectionListener() {
		@Override
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			// TODO Auto-generated method stub
			showSelection(sourcepart, selection);
		}
	};
	
	/**
	 * Shows the given selection in this view.
	 */
	public void showSelection(IWorkbenchPart sourcepart, ISelection selection) {
		
		if (selection instanceof IStructuredSelection && !(sourcepart instanceof IEditorPart)) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			System.out.println("seleccionando proyecto");
			for(Object o : ss.toArray()){
				if(o instanceof IJavaProject){
					IJavaProject t = (IJavaProject)o;
					currentJavaProject = t;
					currentProject = t.getProject();
				}
			}
		}
	}
	
	private Text ClassName;
	private Button search;
	private Button accept;
	private Label Message;
	private TableViewer jarsTable;
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
		Composite composite = createDefaultComposite(parent);
		
		// DataGrid
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.horizontalIndent = 20;
		
		Label libLabel = new Label(composite, SWT.NONE);
		libLabel.setText("Write the class name :");
		
		ClassName = new Text(composite, SWT.BORDER);
		ClassName.setEditable(true);
		ClassName.setLayoutData(data);
		ClassName.setText("here...");
		
		search = new Button(composite,SWT.PUSH);
		search.setText("Search");
		search.addSelectionListener(this);
		
		accept = new Button(composite,SWT.PUSH);
		accept.setText("Add!");
		accept.addSelectionListener(this);

		GridData labelData = new GridData();
		labelData.horizontalAlignment = SWT.FILL;
		labelData.grabExcessHorizontalSpace = true;
		labelData.horizontalSpan = 2;
		Message = new Label(composite,SWT.FILL);
		Message.setLayoutData(labelData);
		
		// DataGrid
		GridData listViewData = new GridData();
		listViewData.horizontalAlignment = SWT.FILL;
		listViewData.verticalAlignment = SWT.FILL;
		listViewData.grabExcessHorizontalSpace = true;
		listViewData.grabExcessVerticalSpace = true;
		listViewData.horizontalSpan = 2;
		
		
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(1));
		tableLayout.addColumnData(new ColumnWeightData(1));
		
		
		Table table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayout(tableLayout);

		this.jarsTable = new TableViewer(table);
		this.jarsTable.getControl().setLayoutData(listViewData);

		TableViewerColumn labelColumn = new TableViewerColumn(this.jarsTable, SWT.NONE);
		labelColumn.getColumn().setText("ClassName");
		TableViewerColumn valueColumn = new TableViewerColumn(this.jarsTable, SWT.NONE);
		valueColumn.getColumn().setText("Jarfile");

		this.jarsTable.setContentProvider(new ArrayContentProvider());
		this.jarsTable.setLabelProvider(new JarsLabelProvider());

	}

	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);
		
		return composite;
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		System.out.println("algo!");
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		HashMap<String,String>[] jars = existLibrary();
		if(e.widget == this.search){
			this.jarsTable.getTable().removeAll();
			if(jars != null){
				this.Message.setText("The class "+ this.ClassName.getText() + " found!");
				this.jarsTable.setInput(jars);
			}else{
				this.Message.setText(ClassName.getText() + " was doesn't found");
			}
		}else if(e.widget == this.accept){
			if(jars != null && getCurrentProject() != null){
				// TODO Try to get that you can set 2 or more classpaths using UpdateProjectClassPath
				Path path = createFullPath();
				System.out.println("path accepted = " + path.toOSString());
				int index = this.jarsTable.getTable().getSelectionIndex();
				@SuppressWarnings("unchecked")
				HashMap<String,String> hm = (HashMap<String,String>)this.jarsTable.getElementAt(index);
				if(UpdateProjectClassPath(new Path[]{path})){
					this.Message.setText("The library " + hm.get("jarfile") +" for " + hm.get("classname")+" was succesfull added to the library project references");
				}else{
					this.Message.setText("The library " + hm.get("jarfile") +" for " + hm.get("classname")+" was unable to load the library on the project library references");
				}
			}else if(getCurrentProject() == null){
				this.Message.setText("You must to select a project to set this classpath");
			}else{
				this.Message.setText(ClassName.getText() + " was doesn't found \nthe library can't be added to the classpath");
			}
		}else{
			System.out.println("Freak error!");
		}
		
	}
	
	/**
	 * Indicate if the library exist or not
	 * @return true if the library was found and false if not
	 */
	private HashMap<String,String>[] existLibrary(){
		HashMap<String,String>[] jarFiles = AcsPathUtilities.getInstance().jarFinder(this.ClassName.getText());
		if(jarFiles == null || jarFiles.length == 0){
			return null;
		}else{
			return jarFiles;
		}
	}
	
	/**
	 * This method is used to create the complete path to the selected library
	 * 
	 * @return true if the path was found and false if was doesn't found
	 */
	@SuppressWarnings("unchecked")
	private Path createFullPath(){
		if(this.jarsTable.getTable().getSelectionIndex() == -1){
			return null;
		}
		int index = this.jarsTable.getTable().getSelectionIndex();
		HashMap<String,String> hm = (HashMap<String,String>)this.jarsTable.getElementAt(index);
		Path acsroot = new Path(AcsPathUtilities.ACSROOTpath() +"/lib/"+hm.get("jarfile"));
		Path introot = new Path(AcsPathUtilities.INTROOTpath() +"/lib/"+((HashMap<String,String>)this.jarsTable.getElementAt(this.jarsTable.getTable().getSelectionIndex())).get("jarfile"));
		Path jacorb = new Path(AcsPathUtilities.JACORBpath() +"/lib/"+((HashMap<String,String>)this.jarsTable.getElementAt(this.jarsTable.getTable().getSelectionIndex())).get("jarfile"));
		
		if(AcsPathUtilities.getInstance().validatePath(introot)){
			return introot;
		}else if(AcsPathUtilities.getInstance().validatePath(jacorb)){
			return jacorb;
		}else if(AcsPathUtilities.getInstance().validatePath(acsroot)){
			return acsroot;
		}else{
			return null;
		}
	}
	
	private boolean UpdateProjectClassPath(Path[] libs){
		ArrayList<IClasspathEntry> newList = new ArrayList<IClasspathEntry>();
		for(Path p : libs){
			newList.add(AcsPathUtilities.getInstance().getClassPathContainer().getClasspathEntry(p, null));
		}

		IClasspathEntry[] result = new IClasspathEntry[newList.size()];
		
		try {
			// getting the new class path
			IClasspathEntry[] newClasspath = AcsPathUtilities.getInstance().getClassPathContainer().updateClasspathEntries(getCurrentProjectJavaProject().getRawClasspath(), (IClasspathEntry[])newList.toArray(result));
			// setting the new class path
			getCurrentProjectJavaProject().setRawClasspath(newClasspath, null);
			return true;
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
