package com.alexnedelcu.aadl2asp.translator;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.Element;
import org.osate.aadl2.ModalPropertyValue;
import org.osate.aadl2.Mode;
import org.osate.aadl2.Property;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.impl.AadlStringImpl;
import org.osate.aadl2.impl.DeviceTypeImpl;
import org.osate.aadl2.impl.RangeValueImpl;
import org.osate.aadl2.impl.RealLiteralImpl;
import org.osate.aadl2.impl.ComputedValueImpl;
import org.osate.aadl2.impl.StringLiteralImpl;
import org.osate.aadl2.properties.EvaluationContext;

import com.alexnedelcu.aadl2asp.ComponentManager;

public class Translator {

	private static int timeDivisions=0;
	
	
	/**
	 * Translates an AADL Model
	 * @return
	 */
	public String translate() {
		String asp="";
		ComponentManager cm = ComponentManager.getInstance();

		ArrayList<ComponentType> translatedComponentTypes = new ArrayList<ComponentType>(); 

		/*
		 * Adding the component implementations to the ASP program
		 * The inherited AADL component types will also be added
		 * 
		 */
		ArrayList<ComponentImplementation> componentImpl = cm.getComponentImplementations();
		for (int i=0; i<componentImpl.size();i++) {
			ComponentImplementation c = componentImpl.get(i);
			ComponentType cType = c.getType();

			if (!translatedComponentTypes.contains(cType)) {
				asp += addComment(cType.getClass().getSimpleName() + ": " + cType.getName());
				asp += new ComponentTypeTranslator(cType).translate();
				translatedComponentTypes.add(cType);
			}
			

			asp += addComment(c.getClass().getSimpleName() + ": " + c.getName());
			asp += new ComponentImplTranslator(c).translate();
		}

		
		/*
		 * Adding the AADL component types to the ASP program,
		 * if they were not already added within the types
		 * 
		 */
		ArrayList<ComponentType> componentTypes = cm.getComponentTypes();
		for (int i=componentTypes.size()-1; i>=0; i--) {
			ComponentType c = componentTypes.get(i);
			if (!translatedComponentTypes.contains(c)) {
				asp = new ComponentTypeTranslator(c).translate() + "\n" + asp;
				asp = addComment(c.getClass().getSimpleName() + ": " + c.getName()) + asp;
				translatedComponentTypes.add(c);
			}
		}

		/*
		 * Define a componentObj - either a type or an implementation
		 */
		asp += addComment("Define a componentObj");
		asp += "componentObj(X) :- componentType(X).\n";
		asp += "componentObj(X) :- componentImplementation(X).\n";

		/*
		 * Adding fluent definitions
		 */
		asp += addComment("Property - inertial fruents");
		asp += "fluent(inertial, property(X,Y,Z)) :- property(X,Y,Z).\n";
		asp += "holds(property(COMPONENT, PROPERTYNAME, PROPERTYVALUE), S+1) :- \n\t"
				+"fluent(inertial, property(COMPONENT, PROPERTYNAME, PROPERTYVALUE)),  \n\t"
				+"holds(property(COMPONENT, PROPERTYNAME, PROPERTYVALUE), S), \n\t"
				+"not holds(property(COMPONENT, PROPERTYNAME, PROPERTYVALUE), S+1), \n\t"
				+"step(S), \n\t"
				+"componentObj(COMPONENT).\n";

		/*
		 * Adding the reality axioms for properties (a property can only have one value at a time)
		 */
		asp += addComment("Adding the reality axioms for properties (a property can only have one value at a time)");
//		asp += "holds(property(COMPONENT, PROPERTYNAME, PROPERTYVALUE), S+1) :- \n\t"
//				+"holds(property(COMPONENT, PROPERTYNAME, PROPERTYVALUE), S), \n\t"
//				+"not holds(property(COMPONENT, PROPERTYNAME, PROPERTYVALUE), S+1), \n\t"
//				+"step(S), \n\t"
//				+"componentObj(COMPONENT).\n";
		
		
		return removeDuplicateStatementsDuplicate(asp);
	}
	
	protected String formatToLegalASPName (String input) {
		String text = input;
		text = text.replace("::", "___");
		text = text.replace(".", "__");
		text = text.toLowerCase();
		return text;
	}

	protected String addComment(String comment) {
		return "\n\n%    "+comment+"\n% ------------------------------------------------------\n";
	}
	
	private void setTimeDivisions (int t) {
		this.timeDivisions=t;
	}
	
	/**
	 * This function will be used to eliminate statements that were already added to the ASP translation.
	 * @param resultedASP
	 * @return
	 */
	protected String removeDuplicateStatementsDuplicate(String resultedASP) {
		String[] statements = resultedASP.trim().split("\n");
		ArrayList<String> uniqueStatements = new ArrayList<String>();
		
		/*
		 * Eliminate the duplicate statements, but keep the comments and the empty lines
		 */
		for (int i=0; i<statements.length; i++) {
			if (statements[i].trim().startsWith("%") || statements[i].trim().equals(""))
				uniqueStatements.add(statements[i]);
			else if (!uniqueStatements.contains(statements[i].trim()))
				uniqueStatements.add(statements[i]);
		}

		
		/*
		 * merging the array elements in a string
		 */
		String asp="";
		
		for  (int i=0; i<uniqueStatements.size(); i++)
			asp += uniqueStatements.get(i)+"\n";
		
		return asp;
	}
	
}
