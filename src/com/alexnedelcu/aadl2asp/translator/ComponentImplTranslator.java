package com.alexnedelcu.aadl2asp.translator;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.Feature;
import org.osate.aadl2.Mode;
import org.osate.aadl2.ModeTransition;

public class ComponentImplTranslator extends ComponentTypeTranslator {
	private ComponentImplementation compImpl;
	private ComponentType compType;
	private String compImplFormattedName, compImplQualifiedName, compQualifiedName,  compFormattedName;
	
	
	public ComponentImplTranslator(ComponentImplementation compImpl) {
		this.compImpl = compImpl;
		this.compType = compImpl.getType();
		
		this.compImplQualifiedName = compImpl.getQualifiedName();
		this.compImplFormattedName=formatToLegalASPName(this.compImplQualifiedName);
		this.compQualifiedName=compType.getQualifiedName();
		this.compFormattedName=formatToLegalASPName(compQualifiedName);
	}
	
	/**
	 * Translate a component implementation to ASP
	 * TODO: merge the translate with the one in ComponentTypeTranslator
	 */
	public String translate() {

		/* 
		 * Translate the component implementation
		 * 
		 */
		addComment(compImpl.getClass().getSimpleName() +": " + compImplQualifiedName);
		String asp="componentImplementation(" + compImplFormattedName + ").\n";
		asp += "implementationOf("+compImplFormattedName+", "+compFormattedName+").\n";

		/*
		 * Translating features
		 */

		// only translate the features of the implementation - not the type
		// the type's features will apply to its implementation, by writing an ASP rule
//		EList<Feature> implFeatures = new BasicEList<Feature>();
//		implFeatures.addAll(this.compImpl.getAllFeatures());
//		implFeatures.removeAll(this.compType.getAllFeatures());
//		
//		if (implFeatures.size() > 0) {
//			asp += addComment("Features of " + compImplQualifiedName);
//			asp += translateFeatures(implFeatures);
//		}
		
		for(int i=0; i<this.compImpl.getAllFeatures().size(); i++) {
			asp += addComment("Features of " + compImplQualifiedName);
			asp += translateFeatures(this.compImpl.getAllFeatures(),  compImplFormattedName);
		}
		
		/*
		 * Translate the modes
		 *
		 */
		// only translate the modes of the implementation - not the type
		// the type's modes will apply to its implementation, by writing an ASP rule
//		EList<Mode> implModes = new BasicEList<Mode>();
//		implModes.addAll(this.compImpl.getAllModes());
//		implModes.removeAll(this.compType.getAllModes());
//		
//		if (implModes.size() > 0) {
//			asp += addComment("Modes of " + compImplQualifiedName);
//			asp += translateModeListOfComponent(implModes, compImplFormattedName);
//		}

		for(int i=0; i<this.compImpl.getAllModes().size(); i++) {
			asp += addComment("Modes of " + compImplQualifiedName);
			asp += translateModeListOfComponent(this.compImpl.getAllModes(),  compImplFormattedName);
		}
		
		/*
		 * Translating its corresponding mode transitions
		 */
		// only translate the mode transitions of the implementation - not the type
		// the type's mode transitions will apply to its implementation, by writing an ASP rule
//		EList<ModeTransition> implModeTransitions = new BasicEList<ModeTransition>();
//		implModeTransitions.addAll(this.compImpl.getAllModeTransitions());
//		implModeTransitions.removeAll(this.compType.getAllModeTransitions());
//		
//		if (implModeTransitions.size() > 0) {
//			asp += addComment("Mode transitions of " + compImpl.getQualifiedName());
//			asp += translateModeTransitions(implModeTransitions, compImplFormattedName);
//		}

		for(int i=0; i<this.compImpl.getAllModeTransitions().size(); i++) {
			asp += addComment("Mode transitions of " + compImplQualifiedName);
			asp += translateModeTransitions(this.compImpl.getAllModeTransitions(),  compImplFormattedName);
		}
		
		/*
		 * Translates the properties
		 *
		 */
		asp += addComment("Properties of " + compImpl.getQualifiedName());
		asp += translatePropertyList(compType.getAllPropertyAssociations(), compImpl.getAllModes());
		return asp;
	}
}
