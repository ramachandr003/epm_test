package supportlibraries;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import com.cognizant.framework.*;
import com.cognizant.framework.ReportThemeFactory.Theme;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Driver script class which encapsulates the core logic of the CRAFT framework
 * 
 * @author Cognizant
 * @version 3.0
 * @since October 2011
 */
public abstract class TestScript extends BaseTestScript
{
	private int currentIteration;
	private Date startTime, endTime;
	private String timeStamp;
	private String reportPath;

	protected CraftliteDataTable dataTable;
	protected ReportSettings reportSettings;
	protected SeleniumReport report;
	protected RemoteWebDriver driver;
	protected ScriptHelper scriptHelper;
		
	private Properties properties;
	private String gridMode;
	private FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	
	/**
	 * The {@link TestParameters} object
	 */
	protected TestParameters testParameters = new TestParameters();

	/**
	 * Constructor to initialize the DriverScript
	 */
	public TestScript()
	{
		setRelativePath();
		properties = Settings.getInstance();
		setDefaultTestParameters();
	}

	private void setRelativePath()
	{
		String relativePath = new File(System.getProperty("user.dir")).getAbsolutePath();
		if (relativePath.contains("allocator"))
		{
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		frameworkParameters.setRelativePath(relativePath);
	}

	/**
	 * Function which implements the test case to be automated
	 */
	protected abstract void executeTestcase();

	private void setDefaultTestParameters()
	{
		testParameters.setCurrentTestcase(this.getClass().getSimpleName());
		testParameters.setCurrentScenario(this.getClass().getPackage().getName().substring(12));
		testParameters.setIterationMode(IterationOptions.RunAllIterations);
		testParameters.setBrowser(Browser.fromString(properties.getProperty("DefaultBrowser").toLowerCase()));
		testParameters.setBrowserVersion(properties.getProperty("DefaultBrowserVersion"));
		testParameters.setPlatform(PlatformFactory.getPlatform(properties.getProperty("DefaultPlatform")));
	}

	/**
	 * Function to execute the given test case
	 */
	public void driveTestExecution()
	{
		initializeWebDriver();
		initializeTestReport();
		initializeDatatable();
		initializeTestScript();
		initializeTestIterations();
		//setUp();
		executeTestIterations();
		tearDown();
		wrapUp();
	}

	/*
	private String convertToBrowserString(String browser)
	{
		String browserString = "";
		if (browser.equalsIgnoreCase("Firefox"))
			browserString = "firefox";
		if (browser.equalsIgnoreCase("InternetExplorer"))
			browserString = "iexplore";
		if (browser.equalsIgnoreCase("Chrome"))
			browserString = "chrome";
		if (browser.equalsIgnoreCase("Opera"))
			browserString = "Opera";
		if (browser.equalsIgnoreCase("Safari"))
			browserString = "safari";

		return browserString;
	}
*/
	
	private void initializeWebDriver()
	{
		startTime = Util.getCurrentTime();
		gridMode = properties.getProperty("GridMode");

		if (gridMode.equalsIgnoreCase("on"))
		{
			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setBrowserName(testParameters.getBrowser().toString());
			desiredCapabilities.setVersion(testParameters.getBrowserVersion());
			desiredCapabilities.setPlatform(testParameters.getPlatform());

			URL gridHubUrl;
			try
			{
				gridHubUrl = new URL(properties.getProperty("GridHubUrl"));
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
				throw new FrameworkException("The specified Selenium Grid Hub URL is malformed");
			}

			driver = new RemoteWebDriver(gridHubUrl, desiredCapabilities);
		} else
		{
			driver = WebDriverFactory.getDriver(testParameters.getBrowser());
		}
	}

	private void initializeReportSettings()
	{
		reportPath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Results" + Util.getFileSeparator() + timeStamp;
		reportSettings = new ReportSettings(reportPath, testParameters.getCurrentTestcase());
		reportSettings.setDateFormatString(properties.getProperty("DateFormatString"));
		reportSettings.setLogLevel(Integer.parseInt(properties.getProperty("LogLevel")));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.generateExcelReports = Boolean.parseBoolean(properties.getProperty("ExcelReport"));
		reportSettings.generateHtmlReports = Boolean.parseBoolean(properties.getProperty("HtmlReport"));
		reportSettings.includeTestDataInReport = Boolean.parseBoolean(properties.getProperty("IncludeTestDataInReport"));
		reportSettings.takeScreenshotFailedStep = Boolean.parseBoolean(properties.getProperty("TakeScreenshotFailedStep"));
		reportSettings.takeScreenshotPassedStep = Boolean.parseBoolean(properties.getProperty("TakeScreenshotPassedStep"));
		reportSettings.setBrowserVersion(testParameters.getBrowserVersion());
	}

	private void initializeDatatable()
	{
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";

		String runTimeDatatablePath;
		if (reportSettings.includeTestDataInReport)
		{
			runTimeDatatablePath = reportPath + Util.getFileSeparator() + "Datatables";

			File runTimeDatatable = new File(runTimeDatatablePath + Util.getFileSeparator() + testParameters.getCurrentScenario() + ".xls");
			if (!runTimeDatatable.exists())
			{
				File datatable = new File(datatablePath + Util.getFileSeparator() + testParameters.getCurrentScenario() + ".xls");

				try
				{
					FileUtils.copyFile(datatable, runTimeDatatable);
				} catch (IOException e)
				{
					e.printStackTrace();
					throw new FrameworkException("Error in creating run-time datatable: Copying the datatable failed...");
				}
			}

			File runTimeCommonDatatable = new File(runTimeDatatablePath + Util.getFileSeparator() + "Common Testdata.xls");
			if (!runTimeCommonDatatable.exists())
			{
				File commonDatatable = new File(datatablePath + Util.getFileSeparator() + "Common Testdata.xls");

				try
				{
					FileUtils.copyFile(commonDatatable, runTimeCommonDatatable);
				} catch (IOException e)
				{
					e.printStackTrace();
					throw new FrameworkException("Error in creating run-time datatable: Copying the common datatable failed...");
				}
			}
		} else
		{
			runTimeDatatablePath = datatablePath;
		}

		dataTable = new CraftliteDataTable(runTimeDatatablePath, testParameters.getCurrentScenario());
	}

	private void initializeTestScript()
	{
		scriptHelper = new ScriptHelper(dataTable, report, driver);
	}

	private synchronized void initializeTestIterations()
	{
		switch (testParameters.getIterationMode())
		{
		case RunAllIterations:
			String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "Datatables";
			ExcelDataAccess testDataAccess = new ExcelDataAccess(datatablePath, testParameters.getCurrentScenario());
			testDataAccess.setDatasheetName(properties.getProperty("DefaultDataSheet"));

			int startRowNum = testDataAccess.getRowNum(testParameters.getCurrentTestcase(), 0);
			int nTestcaseRows = testDataAccess.getRowCount(testParameters.getCurrentTestcase(), 0, startRowNum);
			int nSubIterations = testDataAccess.getRowCount("1", 1, startRowNum); // Assumption: Every test case will have at least one iteration 
			int nIterations = nTestcaseRows / nSubIterations;
			testParameters.setEndIteration(nIterations);
			currentIteration = 1;
			break;

		case RunOneIterationOnly:
			currentIteration = 1;
			break;

		case RunRangeOfIterations:
			if (testParameters.getStartIteration() > testParameters.getEndIteration())
			{
				throw new FrameworkException("Error", "StartIteration cannot be greater than EndIteration!");
			}
			currentIteration = testParameters.getStartIteration();
			break;
		}
	}

	/**
	 * Function to do required setup activities before starting the test
	 * execution
	 */
	protected void setUp()
	{
		// driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		// driver.get(properties.getProperty("ApplicationUrl"));
		String applicationUrl = dataTable.getData("General_Data", "URL"); // properties.getProperty("ApplicationUrl");
		driver.get(applicationUrl);
	}

	private void executeTestIterations()
	{

		while (currentIteration <= testParameters.getEndIteration())
		{
			dataTable.setCurrentRow(testParameters.getCurrentTestcase(), currentIteration);
			report.addTestLogSection("Iteration: " + Integer.toString(currentIteration));
			try
			{
				this.setDriver(driver, dataTable);
				setUp();
				//load the Object Repository properties file once for complete execution
				new LoadObjRep();
				executeTestcase();
			} catch (FrameworkException fx)
			{
				exceptionHandler(fx, fx.errorName);
			} catch (Exception ex)
			{
				exceptionHandler(ex, "Error");
			}

			currentIteration++;
		}
	}

	private void exceptionHandler(Exception ex, String exceptionName)
	{
		// Error reporting
		String exceptionDescription = ex.getMessage();
		if (exceptionDescription == null)
		{
			exceptionDescription = ex.toString();
		}

		if (ex.getCause() != null)
		{
			report.updateTestLog(exceptionName, exceptionDescription + " <b>Caused by: </b>" + ex.getCause(), Status.FAIL);
		} else
		{
			report.updateTestLog(exceptionName, exceptionDescription, Status.FAIL);
		}
		ex.printStackTrace();

		// Error response
		if (frameworkParameters.getStopExecution())
		{
			report.updateTestLog("CRAFTLite Info", "Test execution terminated by user! All subsequent tests aborted...", Status.DONE);
		} else
		{
			OnError onError = OnError.valueOf(properties.getProperty("OnError"));
			switch (onError)
			{
			case NextIteration:
				report.updateTestLog("CRAFTLite Info", "Test case iteration terminated by user! Proceeding to next iteration (if applicable)...", Status.DONE);
				currentIteration++;
				executeTestIterations();
				break;

			case NextTestCase:
				report.updateTestLog("CRAFTLite Info", "Test case terminated by user! Proceeding to next test case (if applicable)...", Status.DONE);
				break;

			case Stop:
				frameworkParameters.setStopExecution(true);
				break;
			}
		}
		// Wrap up execution
		tearDown();
		wrapUp();
	}

	/**
	 * Function to do required teardown activities at the end of the test
	 * execution
	 */
	protected void tearDown()
	{
		try
		{
			Thread.sleep(3000);
			driver.quit();
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
	}

	private void wrapUp()
	{
		endTime = Util.getCurrentTime();
		closeTestReport();

		if (report.getTestStatus().equalsIgnoreCase("Failed"))
		{
			try
			{
				//if(!System.getProperty("browser").equalsIgnoreCase("chrome"))
				//	Assert.fail(report.getFailureDescription());
			}
			catch(Exception e)
			{
				System.out.print(e.getMessage());
			}
		}
	}

	private void closeTestReport()
	{
		String executionTime = Util.getTimeDifference(startTime, endTime);
		report.addTestLogFooter(executionTime);
	}

	private void initializeTestReport()
	{
		frameworkParameters.setRunConfiguration(properties.getProperty("RunConfiguration"));
		timeStamp = TimeStamp.getInstance();

		initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory.getReportsTheme(Theme.valueOf(properties.getProperty("ReportsTheme")));

		report = new SeleniumReport(reportSettings, reportTheme);
		report.initializeReportTypes();

		report.setDriver(driver);
		report.initializeTestLog();
		report.addTestLogHeading(reportSettings.getProjectName() + " - " + reportSettings.getReportName() + " Automation Execution Results");
		report.addTestLogSubHeading("Date & Time", ": " + Util.getCurrentFormattedTime(properties.getProperty("DateFormatString")), "Iteration Mode", ": " + testParameters
				.getIterationMode());
		report.addTestLogSubHeading("Start Iteration", ": " + testParameters.getStartIteration(), "End Iteration", ": " + testParameters.getEndIteration());

		if (gridMode.equalsIgnoreCase("on"))
		{
			report.addTestLogSubHeading("Browser", ": " + testParameters.getBrowser(), "Version", ": " + testParameters.getBrowserVersion());
			report.addTestLogSubHeading("Platform", ": " + testParameters.getPlatform().toString(), "Application Name", ": " + properties.getProperty("ApplicationUrl"));
		} else
		{
			report.addTestLogSubHeading("Browser", ": " + testParameters.getBrowser(), "Application Name", ": " + properties.getProperty("ApplicationUrl"));
		}

		report.addTestLogTableHeadings();
	}

}