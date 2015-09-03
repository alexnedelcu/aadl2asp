package com.alexnedelcu.aadl2asp.translator;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.Feature;
import org.osate.aadl2.Mode;
import org.osate.aadl2.ModeTransition;
import org.osate.aadl2.PropertyAssociation;

import com.alexnedelcu.aadl2asp.translator.features.FeatureTranslator;
import com.alexnedelcu.aadl2asp.translator.modes.ModeTransitionTranslator;
import com.alexnedelcu.aadl2asp.translator.modes.ModeTranslator;
import com.alexnedelcu.aadl2asp.translator.properties.PropertyTranslator;

public class ComponentTypeTranslator extends Translator {
	private ComponentType comp;
	private String compFormattedName;
	
	public ComponentTypeTranslator() {}
	
	public ComponentTypeTranslator(ComponentType component) {
		this.comp = component; 
		this.compFormattedName = formatToLegalASPName(comp.getQualifiedName());
	}
	
	/**
	 * Translate a component type to ASP
	 */
	public String translate() {
		/*
		 * Translating the component type
		 */
		String asp = "componentType("+compFormattedName+").\n";
		
		/*
		 * Translating features
		 */
		if (this.comp.getAllFeatures().size() > 0) {
			asp += addComment("Features of " + comp.getQualifiedName());
			asp += translateFeatures(this.comp.getAllFeatures(), compFormattedName);
		}

		/*
		 * Translating its corresponding modes
		 */
		if (this.comp.getAllModes().size() > 0) {
			asp += addComment("Modes of " + comp.getQualifiedName());
			asp += translateModes();
		}

		/*
		 * Translating its corresponding mode transitions
		 */
		if (this.comp.getAllModeTransitions().size() > 0) {
			asp += addComment("Mode transitions of " + comp.getQualifiedName());
			asp += translateModeTransitions(comp.getAllModeTransitions(), compFormattedName);
		}
		
		/*
		 * Translating the properties belonging to a component type.
		 * 
		 */
		asp += addComment("Properties of " + comp.getQualifiedName());
		asp += translatePropertyList(comp.getAllPropertyAssociations(), comp.getAllModes());
		
		return asp;
	}
	
	public String translateFeatures(EList<Feature> eList, String owningName) {
		String asp = "";
		
		for (int i=0; i<eList.size(); i++) {
			asp += new FeatureTranslator(eList.get(i), owningName).translate();
		}
		return asp;
	}
	
	/**
	 * Translate the modes of a component type to ASP
	 * @return
	 */
	public String translateModes(){		
		return translateModeListOfComponent(comp.getAllModes(),  formatToLegalASPName(comp.getQualifiedName()));
	}

	protected String translateModeListOfComponent(EList<Mode> modes, String ownerName) {
		String asp="";

		/*
		 * Create the ASP translation for each node
		 */
		for (int i=0; i<modes.size(); i++) {
			asp += new ModeTranslator(modes.get(i), ownerName).translate();
		}
		
		return asp;
	}
	

	/**
	 * Translate a list of mode transitions
	 * @param modes
	 * @param ownerName
	 * @return
	 */
	protected String translateModeTransitions(EList<ModeTransition> modeTransitions, String ownerName) {
		String asp="";

		/*
		 * Create the ASP translation for each node
		 */
		for (int i=0; i<modeTransitions.size(); i++) {
			asp += new ModeTransitionTranslator(modeTransitions.get(i), ownerName).translate();
		}
		
		return asp;
	}
	
	/**
	 * Based on a list of property associations and a list of nodes,
	 * the corresponding properties of any component type or
	 * component implementation will be translated to ASP.
	 * 
	 * @param propAsscs
	 * @param modes
	 * @return
	 */
	protected String translatePropertyList(EList<PropertyAssociation> propAsscs, EList<Mode> modes) {
		String asp = "";
		
		for  (int i=0; i<propAsscs.size(); i++) {
			asp += new PropertyTranslator(propAsscs.get(i), modes).translate();
		}
		
		return asp;
	}
	
}
