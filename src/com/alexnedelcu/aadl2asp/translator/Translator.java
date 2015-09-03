package com.alexnedelcu.aadl2asp.translator;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.DataType;
import org.osate.aadl2.Element;
import org.osate.aadl2.ModalPropertyValue;
import org.osate.aadl2.Mode;
import org.osate.aadl2.Property;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.impl.AadlStringImpl;
import org.osate.aadl2.impl.DeviceTypeImpl;
import org.osate.aadl2.impl.RangeValueImpl;
import org.osate.aadl2.impl.RealLiteralImpl;
import org.osate.aadl2.impl.ComputedValueImpl;
import org.osate.aadl2.impl.StringLiteralImpl;
import org.osate.aadl2.properties.EvaluationContext;

import com.alexnedelcu.aadl2asp.ComponentManager;
import com.alexnedelcu.aadl2asp.translator.datatype.DataTypeTranslator;

public abstract class Translator {

	private static int timeDivisions=0;
	
	protected abstract String translate();
	
	protected String formatToLegalASPName (String input) {
		String text = input;
		text = text.replace("::", "___");
		text = text.replace(".", "__");
		text = text.toLowerCase();
		return text;
	}

	protected String addComment(String comment) {
		return "\n\n%    "+comment+"\n% ------------------------------------------------------\n";
	}
	
	private void setTimeDivisions (int t) {
		this.timeDivisions=t;
	}
	
	/**
	 * This function will be used to eliminate statements that were already added to the ASP translation.
	 * @param resultedASP
	 * @return
	 */
	protected String removeDuplicateStatementsDuplicate(String resultedASP) {
		String[] statements = resultedASP.trim().split("\n");
		ArrayList<String> uniqueStatements = new ArrayList<String>();
		
		/*
		 * Eliminate the duplicate statements, but keep the comments and the empty lines
		 */
		for (int i=0; i<statements.length; i++) {
			if (statements[i].trim().startsWith("%") || statements[i].trim().equals(""))
				uniqueStatements.add(statements[i]);
			else if (!uniqueStatements.contains(statements[i].trim()))
				uniqueStatements.add(statements[i]);
		}

		
		/*
		 * merging the array elements in a string
		 */
		String asp="";
		
		for  (int i=0; i<uniqueStatements.size(); i++)
			asp += uniqueStatements.get(i)+"\n";
		
		return asp;
	}
	
}
