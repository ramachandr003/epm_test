/**
 * 
 */
package com.cognizant.framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.cognizant.framework.FrameworkException;


/**
 * Singleton class that encapsulates the user settings specified in the properties file of the framework
 * @author Cognizant
 * @version 3.0
 * @since March 2011
 */
public class LoadObjRep
{
	private static Properties properties;
	
	//private LoadObjRep() {}
	
	/**
	 * Function to return the singleton instance of the {@link Properties} object
	 * @return Instance of the {@link Properties} object
	 */
	
	public LoadObjRep()
	{
		loadFromObjRepFile();
	}
	
	/*public static synchronized Properties getObject()
	{
		if(properties == null) {
			loadFromObjRepFile();
		}
		
		return properties;
	}*/
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	private static void loadFromObjRepFile()
	{
	
		properties = new Properties(System.getProperties());
		
		try {
			properties.load(new FileInputStream("ObjRep.properties"));
			System.setProperties(properties);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("FileNotFoundException while loading the Obj Rep Settings file");
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("IOException while loading the Obj Rep Settings file");
		}
	}
}
