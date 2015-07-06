package com.alexnedelcu.aadl2asp;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osate.aadl2.Element;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.ui.actions.AaxlReadOnlyActionAsJob;
import org.osate.ui.dialogs.Dialog;

import com.alexnedelcu.aadl2asp.translator.Translator;

public class DoAadl2AspConversion extends AaxlReadOnlyActionAsJob  {

	@Override
	protected String getActionName() {
		return "Counting components...";
	}

	@Override
	protected void doAaxlAction(IProgressMonitor monitor, Element root) {
		
		
	    // We only work on Declarative models...
	    if (root instanceof InstanceObject) {
	      Dialog.showError("Example", "Can only analyze declarative objects.");
	    } else {
	      
			/*
			 * Create a new model statistics analysis object. Run it over the
			 * instance model if it exists. Otherwise, run it over all the
			 * declarative models in the workspace.
			 */
			ComponentManager.getInstance().reset();
			final AadlParserTriggers stats = new AadlParserTriggers();
			stats.initSwitches();

			stats.defaultTraversalAllDeclarativeModels();
			
			root.getChildren();
			
			final String aspFromAADL = new Translator().translate();
			
			// open a dialog that
			// - shows the ASP resulting from AADL
			// - lets the user add environmental constraints
			final Shell shell = this.getWindow().getShell();
			Display.getDefault().syncExec(new Runnable() {
			    public void run() {
					ASPDialog dialog = new ASPDialog(shell);
					dialog.setAADLTranslation(aspFromAADL);
					dialog.create();
					dialog.open();
			    }
			});
			
	    }

	}
}