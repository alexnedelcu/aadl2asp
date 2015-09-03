package com.alexnedelcu.aadl2asp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ASPDialog extends org.eclipse.jface.dialogs.Dialog {
	String AADLModel = "";
	protected ASPDialog(Shell parentShell) {
		super(parentShell);
	}
	
	 @Override
	 protected Control createDialogArea(Composite parent) {
	    Composite container = (Composite) super.createDialogArea(parent);
	    
	    Label lblTimeDivisions = new Label(container, SWT.HORIZONTAL);
	    lblTimeDivisions.setText("ASP maximum time divisions:");
	    
	    final Text txtTimeDivisions = new Text(container, SWT.SINGLE);
	    txtTimeDivisions.setText("5");
	    
	    
	    
	    Label lblAadlModelRules = new Label(container, SWT.HORIZONTAL);
	    lblAadlModelRules.setText("ASP repreentation of AADL properties:");

	    GridData grid = new GridData(700, 210);
	    GridData shortGrid = new GridData(700, 75);
	    
	    final Text textAadlModelRules = new Text(container, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	    textAadlModelRules.setLayoutData(grid);
	    textAadlModelRules.setText(AADLModel);

	    Label lblEnvrRules = new Label(container, SWT.HORIZONTAL);
	    lblEnvrRules.setText("ASP environment representation:");
	    
	    final Text textEnvrRules = new Text(container, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	    textEnvrRules.setLayoutData(grid);
	    

	    Label lblQuery = new Label(container, SWT.HORIZONTAL);
	    lblQuery.setText("ASP Query:");
	    
	    final Text textQuery = new Text(container, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	    textQuery.setLayoutData(shortGrid);
	    
	    
	    Button button = new Button(container, SWT.PUSH);
	    button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
	    button.setText("Run clingo");
	    
	    
		button.addSelectionListener(new SelectionAdapter() {
		  @Override
		  public void widgetSelected(SelectionEvent e) {
			  
			  
			  try {

				  /*
				   *  create a temporary file with the ASP contents
				   */
				  PrintWriter writer = new PrintWriter("D:\\MSProject\\tmp\\asp.txt", "UTF-8");
				  writer.println("step(0.."+txtTimeDivisions.getText()+").");
				  writer.println(textAadlModelRules.getText());
				  writer.println(textEnvrRules.getText());
				  writer.println(textQuery.getText());
				  writer.close();

				  /*
				   *  execute the ASP code
				   */
				  System.out.println("------------ clingo result");
				  String clingoResult="";
				  String line;
			      Process p = Runtime.getRuntime().exec("C:\\OtherSoftware\\clingo-4.4.0-win32\\clingo.exe D:\\MSProject\\tmp\\asp.txt");
			      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				  writer = new PrintWriter("D:\\MSProject\\tmp\\clingoResult.txt", "UTF-8");
			      while ((line = input.readLine()) != null) {
			    	  clingoResult += line + "\n";
			    	  writer.println(line);
			          System.out.println(line);
			      }
			      writer.close();
			      input.close();

			      /*
			       *  run the clingo output through mkatoms
			       */
				  String mkAtomsResult="";
				  String[] cmd = {"cmd",
						  "/C",
						  "C:\\OtherSoftware\\mkatoms-2.14.exe < D:\\MSProject\\tmp\\clingoResult.txt & exit",
						  };
			      p = Runtime.getRuntime().exec(cmd);
			      input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			      while ((line = input.readLine()) != null) {
			    	  mkAtomsResult += line + "\n";
			          System.out.println(line);
			      }
			      input.close();
			      
			      final String result = mkAtomsResult;
			      
			      /*
			       *  remove the temporary file
			       */
			      File tmpAspFile =  new File("D:\\MSProject\\tmp\\asp.txt");
			      tmpAspFile.delete();
			      File tmpClingoResultFile =  new File("D:\\MSProject\\tmp\\clingoResult.txt");
			      tmpClingoResultFile.delete();
			      
			      
			      /*
			       *  display the results in a new dialog window
			       */
			      DialogAnswerSet resultDialog = new DialogAnswerSet(new Shell());
			      resultDialog.setResults(result);
			      resultDialog.create();
			      resultDialog.open();
			    }
			    catch (Exception err) {
			    	err.printStackTrace();
			    }
	      }
	    });
	    
	    return container;
	}

	public void setAADLTranslation(String aspFromAADL) {
		AADLModel = aspFromAADL;
	}

}
