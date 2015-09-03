package com.alexnedelcu.aadl2asp.translator.datatype;

import org.osate.aadl2.DataType;

import com.alexnedelcu.aadl2asp.translator.Translator;

public class DataTypeTranslator extends Translator {
	protected DataType dataType;
	protected String dataTypeName;
	
	public DataTypeTranslator(DataType dataType) {
		this.dataType=dataType;
		this.dataTypeName =  formatToLegalASPName(dataType.getQualifiedName());
	}
	
	public String translate() {
		String asp = "";
		
		asp += addComment("Describing an AADL data type");
		asp += "dataType("+this.dataTypeName+").\n";
		
		return asp;
	}
}
