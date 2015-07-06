package com.alexnedelcu.aadl2asp.translator.properties;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Mode;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.impl.ComputedValueImpl;
import org.osate.aadl2.impl.RealLiteralImpl;

import com.alexnedelcu.aadl2asp.translator.PropertyTranslator;

public class ValueComputedTermTranslator extends PropertyTranslator {

	public ValueComputedTermTranslator(PropertyAssociation propertyAssc, EList<Mode> modes) {
		super(propertyAssc, modes);
	}
	
	
	/**
	 * Translates a computed term property whether it is mode specific or not.
	 */
	public String translate() {
		
		
		// TODO: only consider the functions having a specific prefix
		ModeHandler modeHandler = new ModeHandler(propAssc, modes) {

			@Override
			protected String ifModeSpecificValue(PropertyExpression propExpr,
					Mode mode) {
				
				String function = formatToLegalASPName(((ComputedValueImpl) propExpr).getFunction());
				return "holds(property(" + aadlOwningComponentName+ ", "+ propertyQualifiedName + ", X), S) :- "
						+"computed_value("+formatToLegalASPName(function)+", X, S), "
						+"holds(currentMode("+formatToLegalASPName(mode.getFullName())+"), S), step(S).\n"; 
			}

			@Override
			protected String ifNonModeSpecificValue(PropertyExpression propExpr) {
				
				String function = formatToLegalASPName(((ComputedValueImpl) propExpr).getFunction());
				return "holds(property(" + aadlOwningComponentName+ ", "+ propertyQualifiedName + ", X), S) :- "
						+"computed_value("+formatToLegalASPName(function)+", X, S), step(S).\n"; 
				
			}
			
		};
		
		return modeHandler.translate();
	}

}