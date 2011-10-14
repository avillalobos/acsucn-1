
/**
 * @author ssanchez
 */

package acsucn.eclipse.plugin.graphics.wizards;
import java.util.Hashtable;
import acsucn.eclipse.plugin.graphics.projectpreferences.Properties;
import java.util.ArrayList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo; 
import org.eclipse.swt.widgets.List; 
import org.eclipse.swt.widgets.DirectoryDialog;


public class PrototypesMethodsWizardPage extends WizardPage {

	private Button addBtn,addMethodBtn,deleteMethod,deleteParam;
	private Text nameText,paramText;
	private List lista,finalList,listParam;
	private Combo DropListType,DropListReturn,DropVisibility;
    private Label aviso;
	public static ArrayList methodList = new ArrayList();
	public static ArrayList auxList = new ArrayList();
	
/**
* Listener for browse button
*/
	
private SelectionListener ButtonListener = new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			String methodFinal="" ;
			// TODO Auto-generated method stub
			if(e.widget == addBtn){
				if(DropListType.getText().compareTo("")==0){
					aviso.setText("First choose a type parameter");
				}else{
					if(paramText.getText().compareTo("")==0){
						aviso.setText("First choose a name parameter");
					}else{
						if(finalList.getItemCount() == 0){
							aviso.setText("First insert a method");
						}else{
							if(validateReservateType(paramText.getText())==false){
								aviso.setText("Name invalid,keyword Java");
							}else{
								//code addBtn
								auxList.clear();
								if(finalList.isSelected(finalList.getSelectionIndex())==true){
									String Parameters = DropListType.getText() + " " + validateString(paramText.getText());
									if (Parameters.compareTo(" ")!=0){
										auxList.add(Parameters);
										String resetMethod ="";
										String auxResetMethod="";
										String temp="";
										int j;
										if (auxList.size() != 0){
											resetMethod = finalList.getItem(finalList.getSelectionIndex());
											for(j = 0 ; j < resetMethod.length();j++ ){
												auxResetMethod = auxResetMethod + resetMethod.charAt(j);
												if (resetMethod.charAt(j)=='('){
													break;
												}	
											}
											
											for(int x = j+1 ; x < resetMethod.length();x++ ){
													if (resetMethod.charAt(x)==')'){
														break;
													}	
													temp = temp + resetMethod.charAt(x);
											}
											
											//cambia posicion de parametro
											String paramAux = (String)auxList.get(0);
											auxList.clear();
											//reguarda elementos en list
											String makeWord="";
											String makePal="";
											int p;
											if (temp.compareTo(" ")!=0){
												for (p=0 ; p < temp.length();p++){
													if(temp.charAt(p)!=','){
														makeWord = makeWord + temp.charAt(p);
													}else{
														auxList.add(makeWord);
														makeWord="";
													}
												}
											}
											
											if(makeWord.compareTo("")!=0){
												auxList.add(makeWord);
												makeWord="";
											}
											auxList.add(paramAux);
											paramAux="";
											
											for (int i = 0 ; i < auxList.size() ;i++ ){
												if (auxList.size() > 1){
													//no es el ultimo elemento de la lista
													if (i!=auxList.size()-1){
															auxResetMethod = auxResetMethod +(String)auxList.get(i) + ",";
													}else{
															auxResetMethod = auxResetMethod + (String)auxList.get(i) + ");";
													}
												}else {
													auxResetMethod = auxResetMethod + (String)auxList.get(i)+ ");";
												}						
											}
										finalList.setItem(finalList.getSelectionIndex(),auxResetMethod);
										paramText.setText("");
									}
									}
								}else{
									aviso.setText("First choose a method");
								}
								cargarParamList();
							}
						}
					}
				}
				cargarMethodList();			
			}else if(e.widget == addMethodBtn){
			//code addMethodBtn
				if(DropListReturn.getText().compareTo("")==0){
					aviso.setText("First choose a type return");
				}else{
					if(nameText.getText().compareTo("")==0){
						aviso.setText("First choose a name method");
					}else{
						if (DropVisibility.getText().compareTo("")==0){
							aviso.setText("First choose type visibility");
						}else{
							
							if(validateReservateType(nameText.getText())==false){
								aviso.setText("Name Invalid,keyword Java");
							}else{
								methodFinal = DropVisibility.getText() + " " +DropListReturn.getText() + " " + validateString(nameText.getText()) + " ( );";
								if (methodFinal.compareTo("    (")!=0){
									finalList.add(methodFinal);
								}
										
								methodFinal="";
								DropListReturn.setText("");
								DropListType.setText("");
								nameText.setText("");
								aviso.setText("must fill in all fields");
							}
							
						}
					}
				}	
				cargarMethodList();
			}else if(e.widget == deleteMethod){
				finalList.remove(finalList.getSelectionIndex());
				cargarMethodList();	
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
	public PrototypesMethodsWizardPage(String pageName) {
		super(pageName);
		setTitle(ACSWizardMessages.ProtoypePageTitle);
		setDescription(ACSWizardMessages.PrototypeDescription);
		// TODO Auto-generated constructor stub
		
	}

	public PrototypesMethodsWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(4,false);
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 5;
		
		createTextMethod(container);
		setControl(container);
	}
	
	private void createTextMethod(Composite container){
			
		// DataGrid
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		//data.grabExcessHorizontalSpace = true;
		//data.horizontalIndent = -1;
		//data.verticalIndent = 5;
		
		GridData data2 = new GridData();
		data2.horizontalAlignment = GridData.FILL;
		data2.grabExcessHorizontalSpace = true;
		//data2.grabExcessVerticalSpace= true;
		
		GridData data3 = new GridData();
		data3.horizontalAlignment = GridData.FILL;
		data3.grabExcessHorizontalSpace = true;
		data3.grabExcessVerticalSpace= true;
		
		// Define Method name
		Label nameMethod = new Label(container, SWT.NONE);
		nameText = new Text(container, SWT.BORDER);
		nameText.setEditable(true);
		nameText.setToolTipText("put name");
		nameMethod.setText("Method Name");
		nameText.setLayoutData(data);
		
		// Define Return type
		Label returnType = new Label(container, SWT.NONE);
		returnType.setText("Return Type");
		DropListReturn= new Combo(container, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		DropListReturn.setLayoutData(data);
		DropListReturn.setToolTipText("choose type return");
		DropListReturn.add("void");
		DropListReturn.add("boolean");
		DropListReturn.add("char");
		DropListReturn.add("wchar");
		DropListReturn.add("double");
		DropListReturn.add("float");
		DropListReturn.add("octet");
		DropListReturn.add("Long long");
		DropListReturn.add("short");
		DropListReturn.add("string");
		DropListReturn.add("wstring");
		
		// Visibility
		// Define Return type
		Label visibility = new Label(container, SWT.NONE);
		visibility.setText("visibility");
		DropVisibility= new Combo(container, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		DropVisibility.setLayoutData(data);
		DropVisibility.setToolTipText("choose type visibility");
		DropVisibility.add("public");
		DropVisibility.add("protected");
		DropVisibility.add("private");
		
		//Button add Method
		addMethodBtn = new Button(container,SWT.PUSH);
		addMethodBtn.setText("Add Method");
		addMethodBtn.addSelectionListener(ButtonListener);	
		
		//List Methods
		finalList= new List(container,SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		finalList.setLayoutData(data2);
		
		finalList.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) { 
				cargarParamList();
			}});
		
		// Define Parameters
		Label param = new Label(container, SWT.NONE);
		paramText = new Text(container, SWT.BORDER);
		paramText.setEditable(true);
		param.setText("Name parameter");
		paramText.setLayoutData(data);
		paramText.setToolTipText("put one parameter");
		
		// Define Type		
		Label type = new Label(container, SWT.NONE);
		type.setText("Type");
		DropListType= new Combo(container, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		DropListType.setLayoutData(data);
		DropListType.setToolTipText("choose type parameter");
		DropListType.add("boolean");
		DropListType.add("char");
		DropListType.add("wchar");
		DropListType.add("double");
		DropListType.add("float");
		DropListType.add("octet");
		DropListType.add("Long long");
		DropListType.add("short");
		DropListType.add("string");
		DropListType.add("wstring");
		
		//Button add param
		addBtn = new Button(container,SWT.PUSH);
		addBtn.setText("Add Param");
		addBtn.addSelectionListener(ButtonListener);
		
		//List param
		listParam= new List(container,SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		listParam.setLayoutData(data3);
		
		listParam.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) { 
				removeParam();
				cargarMethodList();	
			}});
				
		// Button deleteMethod
		deleteMethod = new Button(container,SWT.PUSH);
		deleteMethod.setText("Delete method");
		deleteMethod.addSelectionListener(ButtonListener);
		

		aviso = new Label(container, SWT.NONE);
		aviso.setText("Must fill in all fields");
		aviso.setLayoutData(data);
		
	}
	
	private String validateString(String palabra){
		String palabraAux ="";
		for (int j = 0 ; j<palabra.length() ; j++){
			if(palabra.charAt(j)!=' '){
				palabraAux = palabraAux + palabra.charAt(j);
			}
		}
		return palabraAux;
	}
	
	private boolean validateReservateType(String palabra){
		
		if (palabra.equals("abstract")||palabra.equals("double")||palabra.equals("int")||palabra.equals("strictfp")||palabra.equals("boolean")||
				palabra.equals("else")||palabra.equals("interface")||palabra.equals("super")||palabra.equals("break")||palabra.equals("extends")||
				palabra.equals("long")||palabra.equals("switch")||palabra.equals("byte")||palabra.equals("final")||palabra.equals("native")||
				palabra.equals("synchronized")||palabra.equals("case")||palabra.equals("finally")||palabra.equals("new")||palabra.equals("this")||palabra.equals("catch")||
				palabra.equals("float")||palabra.equals("package")||palabra.equals("throw")||palabra.equals("char")||palabra.equals("for")||palabra.equals("private")||
				palabra.equals("throws")||palabra.equals("class")||palabra.equals("goto")||palabra.equals("protected")||palabra.equals("transient")||palabra.equals("const")||
				palabra.equals("if")||palabra.equals("public")||palabra.equals("try")||palabra.equals("continue")||palabra.equals("implements")||palabra.equals("return")||
				palabra.equals("void")||palabra.equals("default")||palabra.equals("import")||palabra.equals("short")||palabra.equals("volatile")||palabra.equals("do")||
				palabra.equals("instanceof")||palabra.equals("static ")||palabra.equals("while")||palabra.equals("null")){
			 return false;
		}
		return true;
	}
	
	private void cargarMethodList(){
		methodList.clear();
		String method="";
		for ( int x = 0 ; x < finalList.getItemCount();x++ ){
			method = "       " + finalList.getItem(x);
			methodList.add(method);	
		}
	}	 
	
	private void cargarParamList(){
		listParam.removeAll();
		int j;
		String resetMethod="";
		String auxResetMethod="";
		String temp="";
			if (finalList.getItemCount() != 0){
				resetMethod = finalList.getItem(finalList.getSelectionIndex());
				for(j = 0 ; j < resetMethod.length();j++ ){
					auxResetMethod = auxResetMethod + resetMethod.charAt(j);
					if (resetMethod.charAt(j)=='('){
						break;
					}	
				}
				
				for(int x = j+1 ; x < resetMethod.length();x++ ){
						if (resetMethod.charAt(x)==')'){
							break;
						}	
						temp = temp + resetMethod.charAt(x);
				}	
				//reguarda elementos en list
				String makeWord="";
				String makePal="";
				int p;
				if (temp.compareTo(" ")!=0){
					for (p=0 ; p < temp.length();p++){
						if(temp.charAt(p)!=','){
							makeWord = makeWord + temp.charAt(p);
						}else{
							listParam.add(makeWord);
							makeWord="";
						}
					}
				}
				
				if(makeWord.compareTo("")!=0){
					listParam.add(makeWord);
					makeWord="";
				}
		}
	}
	
	private void removeParam(){
		String deleteParam = "";
		deleteParam = listParam.getItem(listParam.getSelectionIndex());
		
		auxList.clear();
		String resetMethod="";
		String auxResetMethod="";
		String temp="";
		int j;
		
		if (finalList.getItemCount() != 0){
			resetMethod = finalList.getItem(finalList.getSelectionIndex());
			for(j = 0 ; j < resetMethod.length();j++ ){
				auxResetMethod = auxResetMethod + resetMethod.charAt(j);
				if (resetMethod.charAt(j)=='('){
					break;
				}	
			}
			
			for(int x = j+1 ; x < resetMethod.length();x++ ){
					if (resetMethod.charAt(x)==')'){
						break;
					}	
					temp = temp + resetMethod.charAt(x);
			}
			
			//reguarda elementos en list
			String makeWord="";
			String makePal="";
			int p;
			if (temp.compareTo(" ")!=0){
				for (p=0 ; p < temp.length();p++){
					if(temp.charAt(p)!=','){
						makeWord = makeWord + temp.charAt(p);
					}else{
						if( makeWord.compareTo(deleteParam)!=0){
							auxList.add(makeWord);
						}
						makeWord="";
					}
				}
			}
			
			if(makeWord.compareTo("")!=0){
				if( makeWord.compareTo(deleteParam)!=0){
					auxList.add(makeWord);
				}
				makeWord="";
			}
		
			for (int i = 0 ; i < auxList.size() ;i++ ){
				if (auxList.size() > 1){
					//no es el ultimo elemento de la lista
					if (i!=auxList.size()-1){
							auxResetMethod = auxResetMethod +(String)auxList.get(i) + ",";
					}else{
							auxResetMethod = auxResetMethod + (String)auxList.get(i) + ");";
					}
				}else {
					auxResetMethod = auxResetMethod + (String)auxList.get(i)+ ");";
				}						
			}
			if(auxList.size()==0)
				auxResetMethod = auxResetMethod + ");";
			
		finalList.setItem(finalList.getSelectionIndex(),auxResetMethod);
		listParam.remove(listParam.getSelectionIndex());
	}
		
	}
		
}
