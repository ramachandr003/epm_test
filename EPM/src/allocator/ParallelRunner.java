package allocator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import com.cognizant.framework.*;
import supportlibraries.*;

/**
 * Class to facilitate parallel execution of test scripts
 * 
 * @author Cognizant
 * @version 1.0
 * @since October 2012
 */
public class ParallelRunner implements Runnable
{
	private FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	private TestParameters testParameters;
	private Date startTime, endTime;
	private String testStatus;

	private Report report;

	/**
	 * Constructor to initialize the details of the test case to be executed
	 * 
	 * @param testParameters
	 *            The {@link TestParameters} object (passed from the
	 *            {@link Allocator})
	 * @param report
	 *            The {@link Report} object (passed from the {@link Allocator})
	 */
	public ParallelRunner(TestParameters testParameters, Report report)
	{
		super();
		this.testParameters = testParameters;
		this.report = report;
	}

	
	public void run()
	{
		startTime = Util.getCurrentTime();
		testStatus = invokeTestScript();
		endTime = Util.getCurrentTime();
		String executionTime = Util.getTimeDifference(startTime, endTime);
		report.updateResultSummary(testParameters.getCurrentScenario(),
				testParameters.getCurrentTestcase(),
				testParameters.getCurrentTestDescription(),
				executionTime, testStatus, testParameters.getBrowserVersion());
	}

	private String invokeTestScript()
	{
		if (frameworkParameters.getStopExecution())
		{
			testStatus = "Aborted by user";
		} else
		{
			try
			{
				Class<?> testScriptClass = Class.forName("testscripts." + testParameters.getCurrentScenario() + "." + testParameters.getCurrentTestcase());
				Object testScript = testScriptClass.newInstance();

				Field testParameters = testScriptClass.getSuperclass().getDeclaredField("testParameters");
				testParameters.setAccessible(true);
				testParameters.set(testScript, this.testParameters);

				Method driveTestExecution = testScriptClass.getMethod("driveTestExecution", (Class<?>[]) null);
				driveTestExecution.invoke(testScript, (Object[]) null);

				Field testReport = testScriptClass.getSuperclass().getDeclaredField("report");
				testReport.setAccessible(true);
				Report report = (Report) testReport.get(testScript);
				testStatus = report.testStatus;
			} catch (ClassNotFoundException e)
			{
				testStatus = "Reflection Error - ClassNotFoundException";
				e.printStackTrace();
			} catch (IllegalArgumentException e)
			{
				testStatus = "Reflection Error - IllegalArgumentException";
				e.printStackTrace();
			} catch (InstantiationException e)
			{
				testStatus = "Reflection Error - InstantiationException";
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				testStatus = "Reflection Error - IllegalAccessException";
				e.printStackTrace();
			} catch (SecurityException e)
			{
				testStatus = "Reflection Error - SecurityException";
				e.printStackTrace();
			} catch (NoSuchFieldException e)
			{
				testStatus = "Reflection Error - NoSuchFieldException";
				e.printStackTrace();
			} catch (NoSuchMethodException e)
			{
				testStatus = "Reflection Error - NoSuchMethodException";
				e.printStackTrace();
			} catch (InvocationTargetException e)
			{
				testStatus = "Reflection Error - InvocationTargetException";
				e.printStackTrace();
			}
		}

		return testStatus;
	}
}