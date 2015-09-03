package com.alexnedelcu.aadl2asp.translator.modes;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Mode;
import org.osate.aadl2.ModeTransition;
import org.osate.aadl2.ModeTransitionTrigger;

import com.alexnedelcu.aadl2asp.translator.Translator;

public class ModeTransitionTranslator extends Translator {
	ModeTransition modeTransition;
	String ownerName;
	
	public ModeTransitionTranslator(ModeTransition modeTransition,  String ownerName) {
		this.modeTransition = modeTransition;
		this.ownerName = ownerName;
	}
	
	public String translate() {
		String asp="";

		Mode source = this.modeTransition.getSource();
		Mode destination = this.modeTransition.getDestination();

		// the modes are referred by their name 
		String sourceModeName = formatToLegalASPName(source.getName());
		String destinationModeName = formatToLegalASPName(destination.getName());
		
		EList<ModeTransitionTrigger> triggers = this.modeTransition.getOwnedTriggers();
		 
		for (int i=0; i<triggers.size(); i++) {
			ModeTransitionTrigger trigger = triggers.get(i);
			String triggerName = formatToLegalASPName(trigger.getTriggerPort().getQualifiedName());

			asp += "holds(currentMode("+ownerName+", "+destinationModeName+"), S+1) :- \n\t"
					+"holds(currentMode("+ownerName+", "+sourceModeName+"), S),\n\t"
					+"occurs(eventPortValue("+triggerName+", 1), S),\n\t"
					+"step(S).\n";
		}
		
		return asp;
	}
}
