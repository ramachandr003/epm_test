package com.cognizant.framework;


/**
 * Class to encapsulate the reporting settings in the framework
 * @author Cognizant
 * @version 4.0
 * @since June 2012
 */
public class ReportSettings
{
	private String reportPath;
	private String browserVersion;
	
	/**
	 * @return the browserVersion
	 */
	public String getBrowserVersion()
	{
		return browserVersion;
	}
	/**
	 * @param browserVersion the browserVersion to set
	 */
	public void setBrowserVersion(String browserVersion)
	{
		this.browserVersion = browserVersion;
	}
	/**
	 * Function to get the absolute path where the report is to be stored
	 * @return The report path
	 */
	public String getReportPath()
	{
		return reportPath;
	}
	
	private String reportName;
	/**
	 * Function to get the name of the report
	 * @return The report name
	 */
	public String getReportName()
	{
		return reportName;
	}
	
	private String projectName = "";
	/**
	 * Function to get the name of the project being automated
	 * @return The project name
	 */
	public String getProjectName()
	{
		return projectName;
	}
	/**
	 * Function to set the name of the project being automated
	 * @param projectName The project name
	 */
	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}
	
	private int logLevel = 4;
	/**
	 * Function to get the logging level of the reports.
	 * Log levels range between 0 to 5,
	 * with 0 being minimal reporting and 5 being highly detailed reporting
	 * @return The log level
	 */
	public int getLogLevel()
	{
		return logLevel;
	}
	/**
	 * Function to set the logging level of the reports.
	 * Log levels range between 0 to 5,
	 * with 0 being minimal reporting and 5 being highly detailed reporting
	 * @param logLevel The log level
	 */
	public void setLogLevel(int logLevel)
	{
		if (logLevel < 0)
			logLevel = 0;
		
		if (logLevel > 5)
			logLevel = 5;
		
		this.logLevel = logLevel;
	}
	
	/**
	 * Boolean variable indicating whether Excel reports should be generated
	 * @see #generateHtmlReports
	 */
	public Boolean generateExcelReports = false;
	/**
	 * Boolean variable indicating whether HTML reports should be generated
	 * @see #generateExcelReports
	 */
	public Boolean generateHtmlReports = true;
	
	/**
	 * Boolean variable indicating if a copy of the test data
	 * should be included within the report
	 */
	public Boolean includeTestDataInReport = true;
	
	/**
	 * Boolean variable indicating whether a screenshot should be captured for failed/warning steps
	 * @see #takeScreenshotPassedStep
	 */
	public Boolean takeScreenshotFailedStep = true;
	/**
	 * Boolean variable indicating whether a screenshot should be captured for passed steps
	 * @see #takeScreenshotFailedStep
	 */
	public Boolean takeScreenshotPassedStep = false;
	
	private String dateFormatString = "dd-MMM-yyyy hh:mm:ss a";
	/**
	 * Function to get a string indicating the format for the date/time to be used within the report
	 * @return The date/time formatting string
	 * @see java.text.SimpleDateFormat
	 */
	public String getDateFormatString()
	{
		return dateFormatString;
	}
	/**
	 * Function to set a string indicating the format for the date/time to be used within the report
	 * @param dateFormatString The date/time formatting string
	 * @see java.text.SimpleDateFormat
	 */
	public void setDateFormatString(String dateFormatString)
	{
		this.dateFormatString = dateFormatString;
	}
	
	
	/**
	 * Constructor to initialize the report settings
	 * @param reportPath The report path
	 * @param reportName The report name
	 */
	public ReportSettings(String reportPath, String reportName)
	{
		this.reportPath = reportPath;
		this.reportName = reportName;
	}
}