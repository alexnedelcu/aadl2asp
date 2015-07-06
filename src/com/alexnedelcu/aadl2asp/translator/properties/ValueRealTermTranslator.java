package com.alexnedelcu.aadl2asp.translator.properties;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Mode;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.impl.RealLiteralImpl;

import com.alexnedelcu.aadl2asp.translator.PropertyTranslator;

public class ValueRealTermTranslator extends PropertyTranslator {

	public ValueRealTermTranslator(PropertyAssociation propAssc, EList<Mode> modes) {
		super(propAssc, modes);
	}

	/**
	 * Translates a real term property whether it is mode specific or not.
	 */
	public String translate() {		
		
		// TODO: add units
		//String propUnit = formatToLegalASPName(((RealLiteralImpl) propExpr).getUnit().getName());
		
		ModeHandler modeHandler = new ModeHandler(propAssc, modes) {

			@Override
			protected String ifModeSpecificValue(PropertyExpression propExpr,
					Mode mode) {
				
				// TODO : convert to string, and then have python do the computations, instead of keeping only the integer value
				Integer propValue = (int) ((RealLiteralImpl) propExpr).getValue();
				
				return "holds(property(" + aadlOwningComponentName+ ", "+ propertyQualifiedName + ", " + propValue +"), S) "
						+ ":- holds(currentMode("+aadlOwningComponentName+", "+formatToLegalASPName(mode.getFullName())+"), S), step(S).\n";
			}

			@Override
			protected String ifNonModeSpecificValue(PropertyExpression propExpr) {
				
				// TODO : convert to string, and then have python do the computations, instead of keeping only the integer value
				Integer propValue = (int) ((RealLiteralImpl) propExpr).getValue();
				return "holds(property(" + aadlOwningComponentName+ ", "+ propertyQualifiedName + ", " + propValue +"), S) "
						+ ":- step(S).\n"; 
				
			}
			
		};
		
		return modeHandler.translate();
	}

}
