package com.alexnedelcu.aadl2asp.translator;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.Element;
import org.osate.aadl2.ModalPropertyValue;
import org.osate.aadl2.Mode;
import org.osate.aadl2.PropertyAssociation;
import org.osate.aadl2.PropertyExpression;
import org.osate.aadl2.impl.ComponentTypeImpl;
import org.osate.aadl2.impl.ComputedValueImpl;
import org.osate.aadl2.impl.DeviceTypeImpl;
import org.osate.aadl2.impl.RealLiteralImpl;
import org.osate.aadl2.impl.StringLiteralImpl;

import com.alexnedelcu.aadl2asp.translator.properties.ValueComputedTermTranslator;
import com.alexnedelcu.aadl2asp.translator.properties.ValueRangeTermTranslator;
import com.alexnedelcu.aadl2asp.translator.properties.ValueRealTermTranslator;
import com.alexnedelcu.aadl2asp.translator.properties.ValueStringTermTranslator;

public class PropertyTranslator extends Translator {
	
	protected PropertyAssociation propAssc;
	protected Classifier aadlOwningComponent;
	protected PropertyExpression propExpr;
	
	protected String aadlOwningComponentName;
	protected String propertyQualifiedName;
	protected EList<Mode> modes;
	
	public PropertyTranslator(PropertyAssociation propertyAssc, EList<Mode> modes) {
		this.propAssc = propertyAssc;
		this.modes = modes;
		
		aadlOwningComponent = propAssc.getContainingClassifier();
		aadlOwningComponentName = formatToLegalASPName(aadlOwningComponent.getQualifiedName());
		propertyQualifiedName = formatToLegalASPName(propAssc.getProperty().getQualifiedName());

	}
	
	/**
	 * Translate a property to ASP
	 * TODO: more property types to be added
	 */
	public String translate() {

		String asp="";
		
		
		EList<Mode> x = this.propAssc.getOwnedValues().get(0).getInModes();
		this.propAssc.getOwnedValues().get(0).getInModes();

		
		// retrieve the values
		// TODO: consider adding inherited property values ??
		EList<ModalPropertyValue> values = propAssc.getOwnedValues();
		
		// TODO: check if there are properties that return multiple values (maybe recods?)
		propExpr = values.get(0).getOwnedValue();
		
		// take different actions based on the value type
		String propertyClassName = propExpr.getClass().getSimpleName();
		switch(propertyClassName) {
		case "RealLiteralImpl":
			
			asp += new ValueRealTermTranslator(this.propAssc, this.modes).translate();

		break;
		case "StringLiteralImpl":
			
			asp += new ValueStringTermTranslator(this.propAssc, this.modes).translate();
			
			break;
		case "ComputedValueImpl":
			
			asp += new ValueComputedTermTranslator(this.propAssc, this.modes).translate();
			
			break;
		case "RangeValueImpl":
			
			asp += new ValueRangeTermTranslator(this.propAssc, this.modes).translate();
			
			break;
			
		default:
			System.out.println("Unhandled property type: "+propertyClassName);
		
		}
		return asp;
	}
	
}
