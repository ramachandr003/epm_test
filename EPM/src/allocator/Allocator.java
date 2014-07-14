package allocator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import supportlibraries.*;

import com.cognizant.framework.*;
import com.cognizant.framework.ReportThemeFactory.Theme;

/**
 * Class to manage the batch execution of test scripts within the framework
 * Updated for Chrome and IE drivers in WrapUp
 * 
 * @author Cognizant
 * @version 1.0
 * @since October 2012,
 */
public class Allocator
{
	private static ArrayList<TestParameters> testInstancesToRun;
	private static SeleniumReport report;
	private static Properties properties;
	private static FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	private static Date startTime, endTime;
	private static ReportSettings reportSettings;
	private static String timeStamp;
	private static String reportPath;

	private static String managerSheet;
	
	

	public static void main(String[] args) throws FileNotFoundException
	{
		try
		{
			setRelativePath();
			initializeTestBatch(args);
			initializeSummaryReport();
			setupErrorLog();
			driveBatchExecution();
			wrapUp();
		}
		catch (Exception e)
		{
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
		System.out.print("Completed.");
	}

	private static void setRelativePath()
	{
		String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		if (relativePath.contains("allocator"))
		{
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		frameworkParameters.setRelativePath(relativePath);
	}

	private static void initializeTestBatch(String[] args) throws Exception
	{
		startTime = Util.getCurrentTime();
		properties = Settings.getInstance();

		managerSheet = properties.getProperty("ManagerSheet");
		try
		{
			if (managerSheet.toLowerCase().contains(".xls"))
				managerSheet = managerSheet.toLowerCase().replace(".xls", "");
		}
		catch (Exception e)
		{
			System.out.print(e.getMessage());
			e.printStackTrace();
		}

		switch (args.length)
		{
		case 0:
			testInstancesToRun = getRunInfo(properties.getProperty("RunConfiguration"));
			break;
		case 1:
			testInstancesToRun = createRunInfo(args[0].trim());
			break;
		case 2:
			testInstancesToRun = createRunInfo(args[0].trim(), args[1].trim());
			break;
		default:
			throw new Exception("Invalid arguments in command line options");
		}

		/*
		 * report = new Report(); //runConfiguration =
		 * properties.getProperty("RunConfiguration");
		 * //frameworkParameters.setRunConfiguration(runConfiguration);
		 * managerSheet = properties.getProperty("ManagerSheet"); try { if
		 * (managerSheet.toLowerCase().contains(".xls")) managerSheet =
		 * managerSheet.toLowerCase().replace(".xls", ""); } catch (Exception e)
		 * { System.out.print(e.getMessage()); e.printStackTrace(); } timeStamp
		 * = TimeStamp.getInstance(); reportPathWithTimeStamp =
		 * frameworkParameters.getRelativePath() +
		 * frameworkParameters.fileSeparator + "Results" +
		 * frameworkParameters.fileSeparator + timeStamp;
		 */
	}

	private static void setupErrorLog() throws FileNotFoundException
	{
		String errorLogFile = reportPath + Util.getFileSeparator() + "ErrorLog.txt";
		System.setErr(new PrintStream(new FileOutputStream(errorLogFile)));
	}

	private static void initializeSummaryReport()
	{
		frameworkParameters.setRunConfiguration(properties.getProperty("RunConfiguration"));
		timeStamp = TimeStamp.getInstance();

		reportSettings = initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory.getReportsTheme(Theme.valueOf(properties.getProperty("ReportsTheme")));
		report = new SeleniumReport(reportSettings, reportTheme);

		report.initializeReportTypes();

		report.initializeResultSummary();
		report.addResultSummaryHeading(reportSettings.getProjectName() + " - " + " Automation Execution Result Summary");
		report.addResultSummarySubHeading("Date & Time", ": " + Util.getCurrentFormattedTime(properties.getProperty("DateFormatString")), "OnError", ": " + properties
				.getProperty("OnError"));

		report.addResultSummaryTableHeadings();
	}

	private static ReportSettings initializeReportSettings()
	{
		reportPath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Results" + Util.getFileSeparator() + timeStamp;
		ReportSettings reportSettings = new ReportSettings(reportPath, "");

		reportSettings.setDateFormatString(properties.getProperty("DateFormatString"));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.generateExcelReports = Boolean.parseBoolean(properties.getProperty("ExcelReport"));
		reportSettings.generateHtmlReports = Boolean.parseBoolean(properties.getProperty("HtmlReport"));
		return reportSettings;
	}

	private static void driveBatchExecution()
	{
		int nThreads = Integer.parseInt(properties.getProperty("NumberOfThreads"));
		ExecutorService parallelExecutor = Executors.newFixedThreadPool(nThreads);
		//load the Object Repository properties file once for complete execution
		new LoadObjRep();
		for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun.size(); currentTestInstance++)
		{
			//report.setBrowserName(testInstancesToRun.get(currentTestInstance).getBrowserVersion());
			
		
			ParallelRunner testRunner = new ParallelRunner(testInstancesToRun.get(currentTestInstance), report);
			parallelExecutor.execute(testRunner);

			if (frameworkParameters.getStopExecution())
			{
				break;
			}
		}

		parallelExecutor.shutdown();
		while (!parallelExecutor.isTerminated())
		{
			try
			{
				Thread.sleep(3000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static String getCurrentScenario(String testCase) throws Exception
	{
		File f = null;
		if (System.getProperty("os.name").contains("Windows"))
			f = new File(frameworkParameters.getRelativePath() + "\\src\\testscripts");
		else
			f = new File(frameworkParameters.getRelativePath() + "/src/testscripts");

		String str = null;
		ArrayList<String> listofFiles = new ArrayList<String>();
		LinkedList<File> files = new LinkedList<File>();
		files.addAll(Arrays.asList(f.listFiles()));
		while (!files.isEmpty())
		{
			f = files.pop();
			if (f.isDirectory())
			{
				files.addAll(Arrays.asList(f.listFiles()));
				continue;
			}
			str = f.getName().toLowerCase();
			if (str.endsWith(".java") && str.replace(".java", "").equals(testCase.toLowerCase().trim()))
				listofFiles.add(f.getParent());
		}

		if (listofFiles.size() == 1)
		{
			if (System.getProperty("os.name").contains("Windows"))
				str = listofFiles.get(0).substring(listofFiles.get(0).lastIndexOf("\\") + 1);
			else
				str = listofFiles.get(0).substring(listofFiles.get(0).lastIndexOf("/") + 1);
		}
		else if (listofFiles.size() > 1)
			throw new Exception("More than one TestCase found in the project.");
		else
			throw new Exception("TestCase not found in the project.");

		return str;
	}

	private static ArrayList<TestParameters> createRunInfo(String testCase)
	{
		return createRunInfo(testCase, null);
	}

	private static ArrayList<TestParameters> createRunInfo(String testCase, String browser)
	{
		ArrayList<TestParameters> testInstancesToRun = new ArrayList<TestParameters>();
		try
		{
			TestParameters testParameters = new TestParameters();
			testParameters.setCurrentScenario(getCurrentScenario(testCase));
			testParameters.setCurrentTestcase(testCase);
			testParameters.setCurrentTestDescription(testCase);
			testParameters.setIterationMode(IterationOptions.RunAllIterations);
			if (browser != null && !browser.equals(""))
			{
				testParameters.setBrowser(Browser.fromString(browser));
			}
			else
			{
				testParameters.setBrowser(Browser.fromString(properties.getProperty("DefaultBrowser").toLowerCase()));
			}
			testInstancesToRun.add(testParameters);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return testInstancesToRun;
	}

	private static ArrayList<TestParameters> getRunInfo(String sheetName) throws Exception
	{
		ExcelDataAccess runManagerAccess = new ExcelDataAccess(frameworkParameters.getRelativePath(), managerSheet);
		runManagerAccess.setDatasheetName(sheetName);
		int nTestInstances = runManagerAccess.getLastRowNum();
		ArrayList<TestParameters> testInstancesToRun = new ArrayList<TestParameters>();

		for (int currentTestInstance = 1; currentTestInstance <= nTestInstances; currentTestInstance++)
		{
			String executeFlag = runManagerAccess.getValue(currentTestInstance, "Execute");

			if (executeFlag.equalsIgnoreCase("Yes"))
			{
				TestParameters testParameters = new TestParameters();
				String testCase = runManagerAccess.getValue(currentTestInstance, "Test_Case");
				testParameters.setCurrentTestcase(testCase);
				String scenario = runManagerAccess.getValue(currentTestInstance, "Test_Scenario");
				if (scenario.length() > 3)
					testParameters.setCurrentScenario(scenario.trim());
				else
					testParameters.setCurrentScenario(getCurrentScenario(testCase));
				testParameters.setCurrentTestDescription(runManagerAccess.getValue(currentTestInstance, "Description"));

				String iteration = runManagerAccess.getValue(currentTestInstance, "Iteration_Mode");
				if (!iteration.equals(""))
					testParameters.setIterationMode(IterationOptions.valueOf(iteration));
				else
					testParameters.setIterationMode(IterationOptions.RunAllIterations);

				String startIteration = runManagerAccess.getValue(currentTestInstance, "Start_Iteration");
				if (!startIteration.equals(""))
				{
					testParameters.setStartIteration(Integer.parseInt(startIteration));
				}
				String endIteration = runManagerAccess.getValue(currentTestInstance, "End_Iteration");
				if (!endIteration.equals(""))
				{
					testParameters.setEndIteration(Integer.parseInt(endIteration));
				}

				String browser = runManagerAccess.getValue(currentTestInstance, "Browser");				
				if (!browser.equals(""))
				{
					testParameters.setBrowser(Browser.fromString(browser));
				}
				else
				{
					testParameters.setBrowser(Browser.fromString(properties.getProperty("DefaultBrowser").toLowerCase()));
				}

				String browserVersion = runManagerAccess.getValue(currentTestInstance, "Browser_Version");
				if (!browserVersion.equals(""))
				{
					testParameters.setBrowserVersion(browserVersion);
				}
				String platform = runManagerAccess.getValue(currentTestInstance, "Platform");
				if (!platform.equals(""))
				{
					testParameters.setPlatform(PlatformFactory.getPlatform(platform));
				}

				testInstancesToRun.add(testParameters);
			}
		}

		return testInstancesToRun;
	}

	private static void wrapUp()
	{
		endTime = Util.getCurrentTime();
		closeSummaryReport();
		try
		{
			if (System.getProperty("os.name").contains("Windows"))
			{
				Runtime.getRuntime().exec("RunDLL32.EXE shell32.dll,ShellExec_RunDLL " + reportPath + "\\HTML Results\\Summary.Html");
				Runtime.getRuntime().exec("cmd /c taskkill /IM chromedriver.exe /F");
				Runtime.getRuntime().exec("cmd /c taskkill /IM IEDriverServer_32.exe /F");
				Runtime.getRuntime().exec("cmd /c taskkill /IM IEDriverServer_64.exe /F");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void closeSummaryReport()
	{
		String totalExecutionTime = Util.getTimeDifference(startTime, endTime);
		report.addResultSummaryFooter(totalExecutionTime);
	}
}