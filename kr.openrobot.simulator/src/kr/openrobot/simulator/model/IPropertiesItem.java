package kr.openrobot.simulator.model;


import org.eclipse.core.runtime.IAdaptable;

public interface IPropertiesItem 
	extends IAdaptable {
	
	String getName();
	void setName(String newName);
	String getLocation();
	boolean isPropertiesFor(Object obj);
	PropertiesItemType getType();
	String getInfo();
	
	static IPropertiesItem[] NONE = new IPropertiesItem[] {};
	
}
