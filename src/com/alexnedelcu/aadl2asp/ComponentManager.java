package com.alexnedelcu.aadl2asp;

import java.util.ArrayList;

import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.DataPort;
import org.osate.aadl2.DataType;
import org.osate.aadl2.PropertyAssociation;

public class ComponentManager {
	static ComponentManager instance = null;
	static ArrayList<PropertyAssociation> properties;
	static ArrayList<ComponentType> componentTypes;
	static ArrayList<ComponentImplementation> componentImplementations;
	static ArrayList<DataType> dataTypes;

	public static ComponentManager getInstance() {
		if (instance  ==  null) {
			instance  =  new ComponentManager();
			reset();
		}
		return instance;
	}
	
	public void addProperty(PropertyAssociation prop) {
		properties.add(prop);
	}
	
	public ArrayList<PropertyAssociation> getPropertyAssociations() {
		return properties;
	}


	public void addComponentType(ComponentType comp) {
		componentTypes.add(comp);
	}


	public void addComponentImplementation(ComponentImplementation compImpl) {
		componentImplementations.add(compImpl);
	}
	
	public static ArrayList<ComponentType> getComponentTypes() {
		return componentTypes;
	}

	public static ArrayList<ComponentImplementation> getComponentImplementations() {
		return componentImplementations;
	}

	public static void clearPreviousParsing(){
		reset();
	}

	public static void reset() {
		properties = new ArrayList<PropertyAssociation>();
		componentTypes = new ArrayList<ComponentType>();
		componentImplementations = new ArrayList<ComponentImplementation>();
		dataTypes = new ArrayList<DataType>();
	}

	public static ArrayList<DataType> getDataTypes() {
		return dataTypes;
	}

	public void addDataType(DataType object) {
		dataTypes.add(object);
	}
}
