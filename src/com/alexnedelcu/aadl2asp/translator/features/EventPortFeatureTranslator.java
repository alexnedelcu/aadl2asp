package com.alexnedelcu.aadl2asp.translator.features;

import org.osate.aadl2.impl.EventPortImpl;

import com.alexnedelcu.aadl2asp.translator.Translator;

public class EventPortFeatureTranslator extends FeatureTranslator {
	EventPortImpl feature;
	
	public EventPortFeatureTranslator (EventPortImpl eventPort,  String owningComponentName) {
		super(eventPort, owningComponentName);
		this.feature = eventPort;
	}
	
	public String translate() {
		String asp="";
		
		asp += "featureDirection("+this.featureName+", "+this.feature.getDirection().getLiteral()+").\n";
		asp += "eventPortFeature("+this.featureName+").\n";
		asp += "% assign input event at step S with: occurs(eventPortValue(" + this.featureName +", 1), S).\n";
		return asp;
	}
	
}
