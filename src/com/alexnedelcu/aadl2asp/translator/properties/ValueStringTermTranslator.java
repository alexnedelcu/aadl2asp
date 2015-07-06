package com.alexnedelcu.aadl2asp.translator.properties;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Mode;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.impl.RealLiteralImpl;
import org.osate.aadl2.impl.StringLiteralImpl;

import com.alexnedelcu.aadl2asp.translator.PropertyTranslator;


public class ValueStringTermTranslator extends PropertyTranslator {

	
	
	public ValueStringTermTranslator(PropertyAssociation propertyAssc, EList<Mode> modes) {
		super(propertyAssc, modes);
	}

	/**
	 * Translates a string term property whether it is mode specific or not.
	 */
	public String translate() {
		

		ModeHandler modeHandler = new ModeHandler(propAssc, modes) {

			@Override
			protected String ifModeSpecificValue(PropertyExpression propExpr,
					Mode mode) {
				// TODO: handle the mode specific section of the property
				return "";
			}

			@Override
			protected String ifNonModeSpecificValue(PropertyExpression propExpr) {
				// TODO: check for correctness and refactor

				String relationString = ((StringLiteralImpl) propExpr).getValue();
				
				String[] relations = relationString.split("\n");
				
				for (int k=0; k<relations.length; k++)
				{
					String relation = relations[k].trim(); // will contain either a domain or a formula
					
					if(!relation.equals("")) {
						// identify the variable (or the relating property)
						int start=0, end=0;
						start = relation.indexOf('%', 0);
						end = relation.indexOf('%',  start+1);
						
						System.out.println(relation + start +' '+ end);
						String relatingProperty = relation.substring(start+1, end);
						System.out.println(relatingProperty);
						String[] hierarchy = relatingProperty.split("__");
						
						// based on the convention, we could identify the package, component and property name.
						// They are hierarchically separated by double underscore __ 
						String relatingPck = formatToLegalASPName(hierarchy[0]);
						String relatingComp = formatToLegalASPName(hierarchy[1]);
						String relatingPropName = formatToLegalASPName(hierarchy[2]);
						
						// TODO: only consider the functions having a specific prefix
						return "computed_value("+ propertyQualifiedName.replaceAll("___",  "_").replaceAll("__",  "_") + ", Y, S) :- "
								+ "holds(property(" + relatingPck+"___"+relatingComp+ ", "+ relatingPropName + ", X), S), "
								+ "step(S), Y = "+relation.substring(0, start)+"X"+relation.substring(end+1)+".\n";
						
					}
				}
				return "";
			}
			
		};
		
		return modeHandler.translate();
		

		
	}
	
}
