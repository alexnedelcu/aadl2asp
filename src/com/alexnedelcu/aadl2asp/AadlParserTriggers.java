package com.alexnedelcu.aadl2asp;

import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.modelsupport.modeltraversal.AadlProcessingSwitch;
import org.osate.aadl2.util.Aadl2Switch;



public class AadlParserTriggers extends AadlProcessingSwitch {
	ComponentManager cm = new ComponentManager();

	@Override
	protected void initSwitches() {
		/*
		 * We overwrite the case method for a class in the meta model specific
		 * switches.
		 */
		aadl2Switch = new Aadl2Switch<String>() {
			
			public String casePropertyAssociation(PropertyAssociation object) {
				ComponentManager.getInstance().addProperty(object);
				return null;
			}
			@Override
			public String caseComponentType(ComponentType object) {
				ComponentManager.getInstance().addComponentType(object);
				return null;
			}
			
			public String caseComponentImplementation (ComponentImplementation object) {
				ComponentManager.getInstance().addComponentImplementation(object);
				return null;
			}
		};

	}

}
