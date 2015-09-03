package com.alexnedelcu.aadl2asp.translator.features;

import org.osate.aadl2.Feature;
import org.osate.aadl2.impl.DataPortImpl;


public class DataImplFeatureTranslator extends FeatureTranslator {

	protected DataPortImpl feature;
	
	public DataImplFeatureTranslator(DataPortImpl f,  String owningComponentName) {
		super(f, owningComponentName);
		feature = f;
	}

	public String translate() {
		String asp="";
		
		asp += "featureDirection("+this.featureName+", "+this.feature.getDirection().getLiteral()+").\n";
		asp += "dataPortFeature("+this.featureName+").\n";
		asp += "dataPortDataType("+this.featureName+", "+formatToLegalASPName(this.owningComponentName)+").\n";
		asp += "% assign input data at step S with: occurs(dataPortValue(" + this.featureName +", value_term), S).\n";
		
		return asp;
	}
}
