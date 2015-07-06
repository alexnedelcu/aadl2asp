package com.alexnedelcu.aadl2asp.translator;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.Mode;
import org.osate.aadl2.PropertyAssociation;

public class ComponentTypeTranslator extends Translator {
	private ComponentType comp;
	
	public ComponentTypeTranslator() {}
	
	public ComponentTypeTranslator(ComponentType component) {
		this.comp = component; 
	}
	
	/**
	 * Translate a component type to ASP
	 */
	public String translate() {
		/*
		 * Translating the component type
		 */
		String asp = "componentType("+formatToLegalASPName(comp.getQualifiedName())+").\n";
		
		/*
		 * Translating its corresponding modes
		 */
		asp += translateModes();
		
		/*
		 * Translating the properties belonging to a component type.
		 * 
		 */
		asp += translatePropertyList(comp.getAllPropertyAssociations(), comp.getAllModes());
	
		
		
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
			Mode mode = modes.get(i);
			String modeName = mode.getName();
			asp += "mode("+ownerName+", "+modeName+").\n";
			
			/*
			 * Describe the initial node to ASP
			 */
			if(mode.isInitial())
				asp=asp+"holds(currentMode("+ownerName+", "+modeName+"), 0).\n";
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
