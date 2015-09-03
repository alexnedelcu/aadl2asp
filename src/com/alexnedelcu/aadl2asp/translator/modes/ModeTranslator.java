package com.alexnedelcu.aadl2asp.translator.modes;

import org.osate.aadl2.Mode;

import com.alexnedelcu.aadl2asp.translator.Translator;

public class ModeTranslator extends Translator {
	
	Mode mode;
	String modeName;
	String ownerName;
	
	public ModeTranslator(Mode mode,  String ownerName) {
		this.mode=mode;
		this.modeName = mode.getName();
		this.ownerName = ownerName;
	}
	
	public String translate() {
		String asp = "";
		
		asp += "mode("+this.ownerName+", "+this.modeName+").\n";
		
		if(this.mode.isInitial())
			asp += "holds(currentMode("+ownerName+", "+modeName+"), 0).\n";
		
		return asp;
	}
}
