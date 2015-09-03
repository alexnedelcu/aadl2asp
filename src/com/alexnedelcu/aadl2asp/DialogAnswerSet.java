package com.alexnedelcu.aadl2asp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DialogAnswerSet extends Dialog {

	String results;
	String filteredResults;
	Dialog dialog = this;
	
	protected DialogAnswerSet(Shell parentShell) {
		super(parentShell);
	}
	
	public void setResults(String r) {
		results=r;
	}

	 @Override
	 protected Control createDialogArea(Composite parent) {
	    Composite container = (Composite) super.createDialogArea(parent);

	    Label lblResultDesc = new Label(container, SWT.HORIZONTAL);
	    lblResultDesc.setText("ASP Answer Sets:");

	    GridData grid = new GridData(500, 500);
	    
	    final Text textResults = new Text(	container, SWT.MULTI | SWT.V_SCROLL);
	    textResults.setLayoutData(grid);
	    textResults.setText(results);

	    Label lblFilterByRegExpr = new Label(container, SWT.HORIZONTAL);
	    lblFilterByRegExpr.setText("Filtering regex:");
	    
	    final Text textFilterByRegExpr = new Text(container, SWT.SINGLE);
	    textFilterByRegExpr.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    
	    
	    Button button = new Button(container, SWT.PUSH);
	    
	    button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
	    button.setText("Filter");
		button.addSelectionListener(new SelectionAdapter() {
		  @Override
		  public void widgetSelected(SelectionEvent e) {
			  filteredResults = "";
			  String [] lines = results.split("\n");
			  for (int i=0; i<lines.length; i++) {
				  if (lines[i].matches(textFilterByRegExpr.getText()))
					  filteredResults += lines[i]+"\n";
			  }
			  if (filteredResults.equals(""))
				  filteredResults = "Nothing to show";
			  
			  textResults.setText(filteredResults);
	      }
	    });
	    
	    return container;
	}

	

}
