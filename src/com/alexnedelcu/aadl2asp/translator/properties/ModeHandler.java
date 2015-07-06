package com.alexnedelcu.aadl2asp.translator.properties;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.ModalPropertyValue;
import org.osate.aadl2.Mode;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;

public abstract class ModeHandler {
	private PropertyAssociation propAssc;
	private EList<Mode> modes;
	
	public ModeHandler (PropertyAssociation propAssc, EList<Mode> modes) {
		this.propAssc = propAssc;
		this.modes = modes;
	}
	
	protected abstract String ifModeSpecificValue(PropertyExpression propExpr, Mode mode);
	protected abstract String ifNonModeSpecificValue(PropertyExpression propExpr);
	
	/**
	 * translates each mode specific property by calling ifModeSpecificValue
	 * translates each mode specific property by calling ifNonModeSpecificValue
	 * @return
	 */
	public String translate() {

		String asp = "";		
		if (propAssc.isModal()) {
			for (int i=0; i<modes.size(); i++) {
				PropertyExpression propExpr = propAssc.valueInMode(modes.get(i));
				
				if (propExpr != null) 
					asp += ifModeSpecificValue(propExpr, modes.get(i));
			}
		} else {

			// retrieve the values
			// TODO: consider adding inherited property values ??
			EList<ModalPropertyValue> values = propAssc.getOwnedValues();
			
			// TODO: check if there are properties that return multiple values (maybe records?)
			PropertyExpression propExpr = values.get(0).getOwnedValue();
			
			asp += ifNonModeSpecificValue(propExpr);
		}
		
		
		return asp;
	}
	
	
}
