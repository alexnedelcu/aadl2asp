package com.alexnedelcu.aadl2asp.translator;

import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;

public class ComponentImplTranslator extends ComponentTypeTranslator {
	private ComponentImplementation compImpl;
	private ComponentType compType;
	private String compImplQualifiedName, compQualifiedName;
	
	
	public ComponentImplTranslator(ComponentImplementation compImpl) {
		this.compImpl = compImpl;
		this.compType = compImpl.getType();
		
		this.compImplQualifiedName=formatToLegalASPName(compImpl.getQualifiedName());
		this.compQualifiedName=formatToLegalASPName(compImpl.getType().getQualifiedName());
	}
	
	/**
	 * Translate a component implementation to ASP
	 */
	public String translate() {

		/* 
		 * Translate the component implementation
		 * 
		 */
		addComment(compImpl.getClass().getSimpleName() +": " + compImplQualifiedName);
		String asp="componentImplementation(" + compImplQualifiedName + ").\n";
		asp += "implementationOf("+compImplQualifiedName+", "+compQualifiedName+").\n";
		
		/*
		 * Translate the modes
		 *
		 */
		asp += translateModeListOfComponent(compType.getAllModes(), compQualifiedName);
		
		/*
		 * Translates the properties
		 *
		 */
		asp += translatePropertyList(compType.getAllPropertyAssociations(), compImpl.getAllModes());
		return asp;
	}
}
