package com.alexnedelcu.aadl2asp.translator.properties;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Mode;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.UnitLiteral;
import org.osate.aadl2.impl.RangeValueImpl;

import com.alexnedelcu.aadl2asp.translator.PropertyTranslator;

public class ValueRangeTermTranslator extends PropertyTranslator {


	public ValueRangeTermTranslator(PropertyAssociation propAssc, EList<Mode> modes) {
		super(propAssc, modes);
	}

	/**
	 * Translates a range term property whether it is mode specific or not.
	 */
	public String translate() {

		ModeHandler modeHandler = new ModeHandler(propAssc, modes) {

			@Override
			protected String ifModeSpecificValue(PropertyExpression propExpr,
					Mode mode) {
				

				UnitLiteral unit = ((RangeValueImpl) propExpr).getMinimumValue().getUnit();

				// TODO: allow double values to be entered
				int lowerBound = (int) ((RangeValueImpl) propExpr).getMinimumValue().getScaledValue(unit);
				int upperBound = (int) ((RangeValueImpl) propExpr).getMaximumValue().getScaledValue(unit);
				
				
				return "holds(property(" + aadlOwningComponentName+ ", "+ propertyQualifiedName + ", range("+lowerBound+", "+upperBound+")), S) :- "
						+"holds(currentMode("+aadlOwningComponentName+", "+formatToLegalASPName(mode.getName())+"), S), step(S).\n"; 
				
			}

			@Override
			protected String ifNonModeSpecificValue(PropertyExpression propExpr) {


				UnitLiteral unit = ((RangeValueImpl) propExpr).getMinimumValue().getUnit();

				// TODO: allow double values to be entered
				int lowerBound = (int) ((RangeValueImpl) propExpr).getMinimumValue().getScaledValue(unit);
				int upperBound = (int) ((RangeValueImpl) propExpr).getMaximumValue().getScaledValue(unit);
				
				
				return "holds(property(" + aadlOwningComponentName+ ", "+ propertyQualifiedName + ", range("+lowerBound+", "+upperBound+")), S) :- step(S).\n"; 
				
			}
			
		};
		
		return modeHandler.translate();
	}
}
