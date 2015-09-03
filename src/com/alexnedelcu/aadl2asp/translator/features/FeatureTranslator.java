package com.alexnedelcu.aadl2asp.translator.features;

import org.osate.aadl2.Feature;
import org.osate.aadl2.impl.DataPortImpl;
import org.osate.aadl2.impl.EventPortImpl;

import com.alexnedelcu.aadl2asp.translator.Translator;

public class FeatureTranslator extends Translator {
	
	protected Feature feature;
	protected String featureName;
	protected String owningComponentName;
	
	public FeatureTranslator(Feature f, String owningComponentName) {
		this.feature = f;
		this.featureName = formatToLegalASPName(this.feature.getQualifiedName());
		this.owningComponentName = owningComponentName;
	}
	
	public String translate () {
		String asp = "";
				
		asp += "feature("+owningComponentName+", "+featureName+").\n";
		
		/*
		 * TODO: add other types of ports
		 */
		String className = this.feature.getClass().getSimpleName();
		switch (className) {
			case "DataPortImpl":
				asp += new DataImplFeatureTranslator((DataPortImpl) this.feature,  this.owningComponentName).translate();
				break;
			case "EventPortImpl":
				asp += new EventPortFeatureTranslator((EventPortImpl) this.feature,  this.owningComponentName).translate();
				break;
			default:
				System.out.println("The feature type "+className+" was not translated");
		}
		
		return asp;
	}
}
