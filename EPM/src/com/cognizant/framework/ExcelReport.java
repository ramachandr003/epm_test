package com.cognizant.framework;

import com.cognizant.framework.ExcelCellFormatting;
import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.Util;

/**
 * Class to encapsulate the Excel report generation functions of the framework
 * @author Cognizant
 * @version 1.0
 * @since May 2012
 */
public class ExcelReport implements ReportType
{
	private ExcelDataAccess testLogAccess, resultSummaryAccess;
	
	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	private ExcelCellFormatting cellFormatting = new ExcelCellFormatting();
	
	private int currentSectionRowNum = 0;
	private int currentSubSectionRowNum = 0;
	
	
	/**
	 * Constructor to initialize the Excel report path and name
	 * @param reportSettings The {@link ReportSettings} object
	 * @param reportTheme The {@link ReportTheme} object
	 */
	public ExcelReport(ReportSettings reportSettings, ReportTheme reportTheme)
	{
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
		
		testLogAccess =
				new ExcelDataAccess(reportSettings.getReportPath() +
										Util.getFileSeparator() + "Excel Results",
										reportSettings.getReportName()+(reportSettings.getBrowserVersion() != null ? ("_" + reportSettings.getBrowserVersion()) : "-"));
		
		resultSummaryAccess =
				new ExcelDataAccess(reportSettings.getReportPath() +
										Util.getFileSeparator() + "Excel Results",
										"Summary");
	}
	
	
	/* TEST LOG FUNCTIONS */
	
	public void initializeTestLog()
	{
		testLogAccess.createWorkbook();
		testLogAccess.addSheet("Cover_Page");
		testLogAccess.addSheet("Test_Log");
		
		initializeTestLogColorPalette();
		
		testLogAccess.setRowSumsBelow(false);
	}
	
	private void initializeTestLogColorPalette()
	{
		testLogAccess.setCustomPaletteColor((short) 0x8, reportTheme.getHeadingBackColor());
		testLogAccess.setCustomPaletteColor((short) 0x9, reportTheme.getHeadingForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xA, reportTheme.getSectionBackColor());
		testLogAccess.setCustomPaletteColor((short) 0xB, reportTheme.getSectionForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xC, reportTheme.getContentBackColor());
		testLogAccess.setCustomPaletteColor((short) 0xD, reportTheme.getContentForeColor());
		testLogAccess.setCustomPaletteColor((short) 0xE, "#008000");	//Green (Pass)
		testLogAccess.setCustomPaletteColor((short) 0xF, "#FF0000");	//Red (Fail)
		testLogAccess.setCustomPaletteColor((short) 0x10, "#FF8000");	//Orange (Warning)
		testLogAccess.setCustomPaletteColor((short) 0x11, "#000000");	//Black (Done)
		testLogAccess.setCustomPaletteColor((short) 0x12, "#00FF80");	//Blue (Screenshot)
	}
	
	public void addTestLogHeading(String heading)
	{
		testLogAccess.setDatasheetName("Cover_Page");
		int rowNum = testLogAccess.getLastRowNum();
		if (rowNum != 0) {
			rowNum = testLogAccess.addRow();
		}
		
		cellFormatting.setFontName("Copperplate Gothic Bold");
		cellFormatting.setFontSize((short) 12);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		testLogAccess.setValue(rowNum, 0, heading, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	

	public void addTestLogSubHeading(String subHeading1, String subHeading2,
										String subHeading3, String subHeading4)
	{
		testLogAccess.setDatasheetName("Cover_Page");
		int rowNum = testLogAccess.addRow();
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);
		
		testLogAccess.setValue(rowNum, 0, subHeading1, cellFormatting);
		testLogAccess.setValue(rowNum, 1, subHeading2, cellFormatting);
		testLogAccess.setValue(rowNum, 2, "", cellFormatting);
		testLogAccess.setValue(rowNum, 3, subHeading3, cellFormatting);
		testLogAccess.setValue(rowNum, 4, subHeading4, cellFormatting);
	}
	
	
	public void addTestLogTableHeadings()
	{
		testLogAccess.setDatasheetName("Test_Log");
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		testLogAccess.addColumn("Step_No", cellFormatting);
		testLogAccess.addColumn("Step_Name", cellFormatting);
		testLogAccess.addColumn("Description", cellFormatting);
		testLogAccess.addColumn("Status", cellFormatting);
		testLogAccess.addColumn("Step_Time", cellFormatting);
	}

	public void addTestLogSection(String section)
	{
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();
		
		if (currentSubSectionRowNum != 0) {
			// Group (outline) previous sub-section rows
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);
		}
		
		if (currentSectionRowNum != 0) {
			// Group (outline) the previous section rows
			testLogAccess.groupRows(currentSectionRowNum, rowNum - 1);
		}
		
		currentSectionRowNum = rowNum + 1;
		currentSubSectionRowNum = 0;
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0xA);
		cellFormatting.setForeColorIndex((short) 0xB);
		
		testLogAccess.setValue(rowNum, 0, section, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	
	
	public void addTestLogSubSection(String subSection)
	{
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();
		
		if (currentSubSectionRowNum != 0) {
			// Group (outline) previous sub-section rows
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);	
		}
		
		currentSubSectionRowNum = rowNum + 1;
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);
		
		testLogAccess.setValue(rowNum, 0, " " + subSection, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	
	
	public void updateTestLog(String stepNumber, String stepName,
								String stepDescription, Status stepStatus,
								String screenShotName)
	{
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBackColorIndex((short) 0xC);
		cellFormatting.setForeColorIndex((short) 0xD);
		
		cellFormatting.centred = false;
		cellFormatting.bold = false;
		testLogAccess.setValue(rowNum, "Step_Name", stepName, cellFormatting);
		testLogAccess.setValue(rowNum, "Description", stepDescription, cellFormatting);
		
		cellFormatting.centred = true;
		testLogAccess.setValue(rowNum, "Step_No", stepNumber, cellFormatting);
		testLogAccess.setValue(rowNum, "Step_Time", Util.getCurrentFormattedTime(reportSettings.getDateFormatString()), cellFormatting);
		
		cellFormatting.bold = true;
		int columnNum = testLogAccess.getColumnNum("Status", 0);
		
		if (stepStatus.equals(Status.PASS)) {
			cellFormatting.setForeColorIndex((short) 0xE);
			testLogAccess.setValue(rowNum, columnNum, stepStatus.toString(), cellFormatting);
			if (reportSettings.takeScreenshotPassedStep) {
				testLogAccess.setHyperlink(rowNum, columnNum, "..\\Screenshots\\" + screenShotName);
			}
		}
		
		if (stepStatus.equals(Status.FAIL)) {
			cellFormatting.setForeColorIndex((short) 0xF);
			testLogAccess.setValue(rowNum, columnNum, stepStatus.toString(), cellFormatting);
			if (reportSettings.takeScreenshotFailedStep) {
				testLogAccess.setHyperlink(rowNum, columnNum, "..\\Screenshots\\" + screenShotName);
			}
		}
		
		if (stepStatus.equals(Status.WARNING)) {
			cellFormatting.setForeColorIndex((short) 0x10);
			testLogAccess.setValue(rowNum, columnNum, stepStatus.toString(), cellFormatting);
			if (reportSettings.takeScreenshotFailedStep) {
				testLogAccess.setHyperlink(rowNum, columnNum, "..\\Screenshots\\" + screenShotName);
			}
		}
		
		if (stepStatus.equals(Status.DONE) || stepStatus.equals(Status.SCREENSHOT)) {
			cellFormatting.setForeColorIndex((short) 0x11);
			testLogAccess.setValue(rowNum, columnNum, stepStatus.toString(), cellFormatting);
		}
		
		if (stepStatus.equals(Status.SCREENSHOT)) {
			cellFormatting.setForeColorIndex((short) 0x11);
			testLogAccess.setValue(rowNum, columnNum, stepStatus.toString(), cellFormatting);
			testLogAccess.setHyperlink(rowNum, columnNum, "..\\Screenshots\\" + screenShotName);
		}
		
		if (stepStatus.equals(Status.DEBUG)) {
			cellFormatting.setForeColorIndex((short) 0x12);
			testLogAccess.setValue(rowNum, columnNum, stepStatus.toString(), cellFormatting);
		}
	}
	

	public void addTestLogFooter(String executionTime, int nStepsPassed, int nStepsFailed)
	{
		testLogAccess.setDatasheetName("Test_Log");
		int rowNum = testLogAccess.addRow();
		
		if (currentSubSectionRowNum != 0) {
			// Group (outline) the previous sub-section rows
			testLogAccess.groupRows(currentSubSectionRowNum, rowNum - 1);
		}
		
		if (currentSectionRowNum != 0) {
			// Group (outline) the previous section rows
			testLogAccess.groupRows(currentSectionRowNum, rowNum - 1);
		}
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		testLogAccess.setValue(rowNum, 0, "Execution Duration: " + executionTime, cellFormatting);
		testLogAccess.mergeCells(rowNum, rowNum, 0, 4);
		
		rowNum = testLogAccess.addRow();
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		
		cellFormatting.setForeColorIndex((short) 0xE);
		testLogAccess.setValue(rowNum, "Step_No", "Steps passed", cellFormatting);
		testLogAccess.setValue(rowNum, "Step_Name", ": " + nStepsPassed, cellFormatting);
		cellFormatting.setForeColorIndex((short) 0x8);
		testLogAccess.setValue(rowNum, "Description", "", cellFormatting);
		cellFormatting.setForeColorIndex((short) 0xF);
		testLogAccess.setValue(rowNum, "Status", "Steps failed", cellFormatting);
		testLogAccess.setValue(rowNum, "Step_Time", ": " + nStepsFailed, cellFormatting);
		
		wrapUpTestLog();
	}
	
	private void wrapUpTestLog()
	{
		testLogAccess.autoFitContents(0, 4);
		testLogAccess.addOuterBorder(0, 4);
		
		testLogAccess.setDatasheetName("Cover_Page");
		testLogAccess.autoFitContents(0, 4);
		testLogAccess.addOuterBorder(0, 4);
	}
	
	
	/* RESULT SUMMARY FUNCTIONS */
	

	public void initializeResultSummary()
	{
		resultSummaryAccess.createWorkbook();
		resultSummaryAccess.addSheet("Cover_Page");
		resultSummaryAccess.addSheet("Result_Summary");
		
		initializeResultSummaryColorPalette();
	}
	
	private void initializeResultSummaryColorPalette()
	{
		resultSummaryAccess.setCustomPaletteColor((short) 0x8, reportTheme.getHeadingBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0x9, reportTheme.getHeadingForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xA, reportTheme.getSectionBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xB, reportTheme.getSectionForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xC, reportTheme.getContentBackColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xD, reportTheme.getContentForeColor());
		resultSummaryAccess.setCustomPaletteColor((short) 0xE, "#008000");	//Green (Pass)
		resultSummaryAccess.setCustomPaletteColor((short) 0xF, "#FF0000");	//Red (Fail)
	}
	
	
	public void addResultSummaryHeading(String heading)
	{
		resultSummaryAccess.setDatasheetName("Cover_Page");
		int rowNum = resultSummaryAccess.getLastRowNum();
		if (rowNum != 0) {
			rowNum = resultSummaryAccess.addRow();
		}
		
		cellFormatting.setFontName("Copperplate Gothic Bold");
		cellFormatting.setFontSize((short) 12);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		resultSummaryAccess.setValue(rowNum, 0, heading, cellFormatting);
		resultSummaryAccess.mergeCells(rowNum, rowNum, 0, 4);
	}
	

	public void addResultSummarySubHeading(String subHeading1, String subHeading2,
											String subHeading3, String subHeading4)
	{
		resultSummaryAccess.setDatasheetName("Cover_Page");
		int rowNum = resultSummaryAccess.addRow();
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		cellFormatting.setForeColorIndex((short) 0x8);
		
		resultSummaryAccess.setValue(rowNum, 0, subHeading1, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 1, subHeading2, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 2, "", cellFormatting);
		resultSummaryAccess.setValue(rowNum, 3, subHeading3, cellFormatting);
		resultSummaryAccess.setValue(rowNum, 4, subHeading4, cellFormatting);
	}

	public void addResultSummaryTableHeadings()
	{
		resultSummaryAccess.setDatasheetName("Result_Summary");
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		resultSummaryAccess.addColumn("Test_Scenario", cellFormatting);
		resultSummaryAccess.addColumn("Test_Case", cellFormatting);
		resultSummaryAccess.addColumn("Test_Description", cellFormatting);
		resultSummaryAccess.addColumn("Execution_Time", cellFormatting);
		resultSummaryAccess.addColumn("Test_Status", cellFormatting);
	}
	
	public void updateResultSummary(String scenarioName, String testcaseName,
										String testcaseDescription, String executionTime,
										String testStatus, String browserVersion)
	{
		resultSummaryAccess.setDatasheetName("Result_Summary");
		int rowNum = resultSummaryAccess.addRow();
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.setBackColorIndex((short) 0xC);
		cellFormatting.setForeColorIndex((short) 0xD);
		
		cellFormatting.centred = false;
		cellFormatting.bold = false;
		resultSummaryAccess.setValue(rowNum, "Test_Scenario", scenarioName, cellFormatting);
		
		int columnNum = resultSummaryAccess.getColumnNum("Test_Case", 0);
		resultSummaryAccess.setValue(rowNum, columnNum, testcaseName+ "_" + browserVersion, cellFormatting);
		resultSummaryAccess.setHyperlink(rowNum, columnNum, scenarioName + "_" + testcaseName + ".xls");
		
		resultSummaryAccess.setValue(rowNum, "Test_Description", testcaseDescription, cellFormatting);
		
		cellFormatting.centred = true;
		resultSummaryAccess.setValue(rowNum, "Execution_Time", executionTime, cellFormatting);
		
		cellFormatting.bold = true;
		if (testStatus.equalsIgnoreCase("Passed")) {
			cellFormatting.setForeColorIndex((short) 0xE);
		}
		if (testStatus.equalsIgnoreCase("Failed")) {
			cellFormatting.setForeColorIndex((short) 0xF);
		}
		resultSummaryAccess.setValue(rowNum, "Test_Status", testStatus, cellFormatting);
	}
	

	public void addResultSummaryFooter(String totalExecutionTime, int nTestsPassed, int nTestsFailed)
	{	
		resultSummaryAccess.setDatasheetName("Result_Summary");
		int rowNum = resultSummaryAccess.addRow();
		
		cellFormatting.setFontName("Verdana");
		cellFormatting.setFontSize((short) 10);
		cellFormatting.bold = true;
		cellFormatting.centred = true;
		cellFormatting.setBackColorIndex((short) 0x8);
		cellFormatting.setForeColorIndex((short) 0x9);
		
		resultSummaryAccess.setValue(rowNum, 0, "Total Duration: " +
													totalExecutionTime, cellFormatting);
		resultSummaryAccess.mergeCells(rowNum, rowNum, 0, 4);
		
		rowNum = resultSummaryAccess.addRow();
		cellFormatting.centred = false;
		cellFormatting.setBackColorIndex((short) 0x9);
		
		cellFormatting.setForeColorIndex((short) 0xE);
		resultSummaryAccess.setValue(rowNum, "Test_Scenario", "Tests passed", cellFormatting);
		resultSummaryAccess.setValue(rowNum, "Test_Case", ": " + nTestsPassed, cellFormatting);
		cellFormatting.setForeColorIndex((short) 0x8);
		resultSummaryAccess.setValue(rowNum, "Test_Description", "", cellFormatting);
		cellFormatting.setForeColorIndex((short) 0xF);
		resultSummaryAccess.setValue(rowNum, "Execution_Time", "Tests failed", cellFormatting);
		resultSummaryAccess.setValue(rowNum, "Test_Status", ": " + nTestsFailed, cellFormatting);
		
		wrapUpResultSummary();
	}
	
	private void wrapUpResultSummary()
	{
		resultSummaryAccess.autoFitContents(0, 4);
		resultSummaryAccess.addOuterBorder(0, 4);
		
		resultSummaryAccess.setDatasheetName("Cover_Page");
		resultSummaryAccess.autoFitContents(0, 4);
		resultSummaryAccess.addOuterBorder(0, 4);
	}
}