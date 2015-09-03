package com.alexnedelcu.aadl2asp.translator.properties;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Mode;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.impl.RealLiteralImpl;
import org.osate.aadl2.impl.StringLiteralImpl;


public class ValueStringTermTranslator extends PropertyTranslator {

	ArrayList<EncodedVariable> variables = new ArrayList<EncodedVariable>();
	
	
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
						
						String tmp = relation;
						
						while (!tmp.equals("")) {
							tmp = loadFirstVariableBlock(tmp);
						}
						
						String asp = "";
						String equation = variables.get(0).getLeftHandSide() + variables.get(0).getName();
						for (int i=0; i<variables.size(); i++){
							asp += variables.get(i).getAspValueGrabberStatement()+", \n\t";
							if (i>0)
								equation += variables.get(i).getLeftHandSide() + variables.get(i).getName();
							if (i==variables.size()-1)
								equation += variables.get(i).getRightHandSide();
						}
						
						
//						
						asp = "computed_value("+ propertyQualifiedName.replaceAll("___",  "_").replaceAll("__",  "_") + ", "+equation+", S) :- \n\t" + asp;
						asp += "step(S).";
						
						return asp;
						
					}
				}
				return "";
			}
			
		};
		
		return modeHandler.translate();
		
		
	}
	
	// TODO: allow the user specify the same variable twice.
	private String loadFirstVariableBlock (String input) {
		

		// identify the variable (or the relating property)
		int varStart=0, varEnd=0, blockStart=0, blockEnd = input.length();
		varStart = input.indexOf('%', 0);
		varEnd = input.indexOf('%',  varStart+1);
		
		
		int nextVarPos = input.indexOf('%', varEnd+1);
		if (nextVarPos != -1)
			blockEnd = nextVarPos;
		
		if (varStart != -1 && varEnd !=-1) {
			EncodedVariable var = new EncodedVariable(); 
			var.setReference (input.substring(varStart, varEnd+1));
			var.setLeftHandSide (input.substring(0, varStart));
			var.setRightHandSide (input.substring(varEnd+1,  blockEnd));
			variables.add(var);
		} else return "";
		
		return input.substring(varEnd+1);
	}
	
	
	class EncodedVariable {
		String leftHandSide, rightHandSide, varReference;
		String aspValueGrabberStatement;
		String nameVar;

		void setLeftHandSide(String input) {
			leftHandSide=input;
		}

		void setRightHandSide(String input) {
			rightHandSide=input;
		}

		public String getLeftHandSide () {
			return leftHandSide;
		}
		public String getRightHandSide () {
			return rightHandSide;
		}

		public String getName () {
			return nameVar;
		}
		public String getAspValueGrabberStatement () {
			return aspValueGrabberStatement;
		}
		
		void setReference(String reference) {
			
			nameVar = "X"+variables.size();
			
			String[] hierarchy = reference.substring(1, reference.length()-1).split("__");
			
			/*
			 * A computed property can take as an input a feature (input) or another property
			 * A section will specify if the variable is found under the features or the properties
			 */
			String section = hierarchy[2].split("::")[0];
			
			// based on the convention, we could identify the package, component and property name.
			// They are hierarchically separated by double underscore __ 
			String relatingPck = formatToLegalASPName(hierarchy[0]);
			String relatingComp = formatToLegalASPName(hierarchy[1]);
			String relatingReferredInputName = formatToLegalASPName(hierarchy[2].split("::",2)[1]);
			
			// TODO: only consider the functions having a specific prefix
			switch (section) {
				case "properties":
					aspValueGrabberStatement = "holds(property(" + relatingPck+"___"+relatingComp+ ", "+ relatingReferredInputName + ", "+nameVar+"), S)";
					break;
				case "features":
					aspValueGrabberStatement = "occurs(dataPortValue(" + relatingPck+"___"+relatingComp+ "__"+relatingReferredInputName+", "+nameVar+"), S)";
					break;
			}
			varReference=reference;
		}
		
		
		
		
	}
	
}
