package supportlibraries;

import java.io.File;
import java.util.Properties;

import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Util;


/**
 * Singleton class which manages the creation of timestamped result folders for every test batch execution
 * @author Cognizant
 * @version 3.0
 * @since October 2011
 */
public class TimeStamp
{
	private static String timeStamp;
	
	/**
	 * Function to return the timestamped result folder path
	 * @return The timestamped result folder path
	 */
	public static synchronized String getInstance()
	{
		if(timeStamp == null) {
			FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
			Properties properties = Settings.getInstance();
			
			timeStamp = frameworkParameters.getRunConfiguration() + Util.getFileSeparator() + "Run_" +
							Util.getCurrentFormattedTime(properties.getProperty("DateFormatString")).replace(" ", "_").replace(":", "-");
            
			String reportPathWithTimeStamp = frameworkParameters.getRelativePath() +
												Util.getFileSeparator() + "Results" +
												Util.getFileSeparator() + timeStamp;
            
            new File(reportPathWithTimeStamp).mkdirs();
    		new File(reportPathWithTimeStamp + Util.getFileSeparator() + "Screenshots").mkdir();
		}
		
		return timeStamp;
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}