package com.alexnedelcu.aadl2asp;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.ClassifierFeature;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.DataPort;
import org.osate.aadl2.DataSubcomponentType;
import org.osate.aadl2.DataType;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.PortCategory;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.modelsupport.modeltraversal.AadlProcessingSwitch;
import org.osate.aadl2.util.Aadl2Switch;



public class Aadl2Parser extends AadlProcessingSwitch {
	ComponentManager cm = new ComponentManager();

	@Override
	protected void initSwitches() {
		/*
		 * We overwrite the case method for a class in the meta model specific
		 * switches.
		 */
		aadl2Switch = new Aadl2Switch<String>() {
			@Override
			public String casePropertyAssociation(PropertyAssociation object) {
				ComponentManager.getInstance().addProperty(object);
				return null;
			}
			@Override
			public String caseComponentType(ComponentType object) {
				ComponentManager.getInstance().addComponentType(object);
				return null;
			}
			@Override
			public String caseComponentImplementation (ComponentImplementation object) {
				ComponentManager.getInstance().addComponentImplementation(object);
				return null;
			}
			@Override
			public String caseDataType(DataType object) {
				ComponentManager.getInstance().addDataType(object);
				return null;
			}
		};

	}

}
