package com.alexnedelcu.aadl2asp.translator;

import java.util.ArrayList;

import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.DataType;

import com.alexnedelcu.aadl2asp.ComponentManager;
import com.alexnedelcu.aadl2asp.translator.datatype.DataTypeTranslator;

public class AADLModelTranslator extends Translator {

	public AADLModelTranslator() {
		// TODO Auto-generated constructor stub
	}


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
		 * Adding the AADL declared data types to the ASP program
		 * 
		 */
		ArrayList<DataType> dataTypes = cm.getDataTypes();
		for (int i=dataTypes.size()-1; i>=0; i--) {
			DataType c = dataTypes.get(i);
//			asp += addComment(c.getClass().getSimpleName() + ": " + c.getName());
			asp += new DataTypeTranslator(c).translate() + "\n";
		}

		/*
		 * Define a componentObj - either a type or an implementation
		 */
		asp += addComment("Define a componentObj");
		asp += "componentObj(X) :- componentType(X).\n";
		asp += "componentObj(X) :- componentImplementation(X).\n";


//		/*
//		 * Any feature (AADL port) of a type applies to its implementations
//		 */
//		asp += addComment("Any feature (AADL port) of a type applies to its implementations");
//		asp += "feature(FEATURE, COMPONENTIMPL) :- \n\t"
//				+"feature(FEATURE, COMPONENTTYPE), \n\t"
//				+"componentType(COMPONENTTYPE), \n\t"
//				+"componentImplementation(COMPONENTIMPL), \n\t"
//				+"implementationOf(COMPONENTIMPL, COMPONENTTYPE). \n";
//
//		/*
//		 * Any mode of a type applies to its implementations
//		 */
//		asp += addComment("Any mode of a type applies to its implementations");
//		asp += "mode(COMPIMPL, MODE) :- mode(COMPTYPE, MODE), \n\t"
//				+"implementationOf(COMPIMPL, COMPTYPE), \n\t"
//				+"componentType(COMPTYPE), \n\t"
//				+"componentImplementation(COMPIMPL), \n\t"
//				+"step(S).\n";
//		// TODO
//		asp += "holds(currentMode(COMPIMPL, MODE) :- mode(COMPTYPE, MODE), \n\t"
//				+"implementationOf(COMPIMPL, COMPTYPE), \n\t"
//				+"componentType(COMPTYPE), \n\t"
//				+"componentImplementation(COMPIMPL), \n\t"
//				+"step(S).\n";
//
//		/*
//		 * Any mode transition of a type applies to its implementation
//		 */
//		asp += addComment("Any mode transition of a type applies to its implementations");
//		asp += "holds(currentMode(COMPIMPL, MODE1), S+1) :-  \n\t"
//				+"holds(currentMode(COMPIMPL, MODE2), S), \n\t"
//				+"holds(eventPortValue(EVENTCOMPIMPLPORT, 1), S), \n\t"
//				+"feature(), \n\t"
//				+"mode(), \n\t"
//				+"implementationOf(COMPIMPL, COMPTYPE), \n\t"
//				+"componentType(COMPTYPE), \n\t"
//				+"componentImplementation(COMPIMPL), \n\t"
//				+"step(S).\n";

//holds(currentMode(pcfan___fan, powered), S+1) :- 
//	holds(currentMode(pcfan___fan, not_powered), S),
//	holds(eventPortValue(pcfan___fan__turnonevent, 1), S),
//	step(S).


	/*
	 * Add the event as a defined fluent?
	 */
		
		/*
		 * Any mode of a type applies to its implementations
		 */
		asp += addComment("Any mode of a type applies to its implementations");
		asp += "holds(property(COMPIMPL, PROPNAME, PROPVALUE), S) :- holds(property(COMPTYPE, PROPNAME, PROPVALUE), S), \n\t"
				+"implementationOf(COMPIMPL, COMPTYPE), \n\t"
				+"componentType(COMPTYPE), \n\t"
				+"componentImplementation(COMPIMPL), \n\t"
				+"step(S).\n";


		/*
		 * Adding fluent definitions
		 */
		asp += addComment("Modes - inertial fruents (a current mode stays current of no action that changes the mode occurs)");
		asp += "fluent(inertial, currentMode(COMPONENT, MODE)) :- mode(COMPONENT, MODE),  componentObj(COMPONENT).\n";
		asp += "holds(currentMode(COMPONENT, MODE), S+1) :- \n\t"
				+"fluent(inertial, currentMode(COMPONENT, MODE)),  \n\t"
				+"holds(currentMode(COMPONENT, MODE), S), \n\t"
				+"not -holds(currentMode(COMPONENT, MODE), S+1), \n\t"
				+"step(S), \n\t"
				+"componentObj(COMPONENT).\n";
		

		/*
		 * Closed world assumption: properties (a property can only have one value at a time)
		 */
		asp += addComment("Closed world assumption:a currentModes can only have one value at a time");
		asp += "-holds(currentMode(COMPONENT, MODE1), S) :- \n\t"
				+"holds(currentMode(COMPONENT, MODE2), S), \n\t"
				+"MODE1 != MODE2, \n\t"
				+"mode(COMPONENT, MODE1), \n\t"
				+"mode(COMPONENT, MODE2), \n\t"
				+"step(S), \n\t"
				+"componentObj(COMPONENT).\n";

		// TODO: instead of defining the domain manually, integrate it in ASP
		asp += "-holds(property(COMPONENT, PROP, VALUE1), S) :- \n\t"
				+"holds(property(COMPONENT, PROP, VALUE2), S), \n\t"
				+"dom(VALUE1),\n\t"
				+"VALUE1!=VALUE2, \n\t"
				+"step(S), \n\t"
				+"componentObj(COMPONENT).\n";
		asp += "dom(1..150).\n";

		/*
		 * % Observations about the initial state are taken as-is
		 */
		asp += addComment("Observations about the initial state are taken as-is");
		asp += "holds(F,0) :- obs(F,t,0).\n";
		asp += "-holds(F,0) :- obs(F,f,0).\n";

		/*
		 * If an action is observed to occur, it is assumed to have indeed occurred
		 */
		asp += addComment("% If an action is observed to occur, it is assumed to have indeed occurred");
		asp += "occurs(A,S) :- hpd(A,S).\n";


		/*
		 * Reality check axiom
		 */
		asp += addComment("% Reality check axiom");
		asp += ":- obs(F,f,S), not -holds(F,S).\n";
		asp += ":- obs(F,t,S), not holds(F,S).\n";

		

		/*
		 * Closed world assumption - (if the current mode is on, then state that the current mode is not off)
		 */
//		asp += addComment("Closed world assumption - (if the current mode is on, then state that the current mode is not off)");
//		asp += "-holds(currentMode(COMPONENT, MODE1), S) :- \n\t"
//				+"holds(currentMode(COMPONENT, MODE2), S), \n\t"
//				+"MODE1 != MODE2, \n\t"
//				+"mode(COMPONENT, MODE1), \n\t"
//				+"mode(COMPONENT, MODE2), \n\t"
//				+"step(S), \n\t"
//				+"componentObj(COMPONENT).\n";
		
		
		return removeDuplicateStatementsDuplicate(asp);
	}
}
