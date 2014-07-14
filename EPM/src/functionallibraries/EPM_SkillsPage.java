package functionallibraries;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import supportlibraries.*;

import com.cognizant.framework.*;
import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;

/**
 * SignOnPage class
 * 
 * @author Cognizant
 */
public class EPM_SkillsPage extends MasterPage
{
	
	
	
	
	
		//Object Locator Values
		
		
		public String tblSkills  = "ctl00_ContentMain_ElsevierGrid1_RadGrid1_ctl00";
		public String selSkillStatus = "ctl00_ContentMain_ElsevierGrid1_ddlView";  
		
		
		//Create and Edit a Skill
		public String txtSkillName = "ctl00_ContentMain_TabContainer1_TabPanel1_tbTitle";	
		public String radActive = "ctl00_ContentMain_TabContainer1_TabPanel1_rblActive";
		public String radAlert = "ctl00_ContentMain_TabContainer1_TabPanel1_rblAlert";
		public String txtAlert = "ctl00_ContentMain_TabContainer1_TabPanel1_tbAlert";
		public String txtSummary = "ctl00_ContentMain_TabContainer1_TabPanel1_tbSummary";
		public String txtVerHistComments = "ctl00_ContentMain_TabContainer1_TabPanel1_tbComments";
		public String btnSave_Skill = "//*[@id='ctl00_ContentMain_TabContainer1_TabPanel1_btnSave']";
		public String btnCancel_Skill = "//input[@id='" + btnSave_Skill + "']/following-sibling::input";
		
		//Search Skill
		public String txtSkillSearchIndex = "ctl00_ContentMain_ElsevierGrid1_txtIndex";
		public String btnShowSearchResults = "ctl00_ContentMain_ElsevierGrid1_btnResultsSearch";
		
		//********************GRADE CHECKLISTS PAGE- secondary window******************************
		//*****filter Options*********
		public String selFilter1By= "ctl00_ContentMain_btnSearch_ddlFilterOne";
		public String selFilter1Option= "ctl00_ContentMain_btnSearch_ddlFilter1Options";
		public String btnShowResults1= "ctl00_ContentMain_btnSearch_btnSubmit1"; // Main Show Results
		public String btnShowResults2= "ctl00_ContentMain_btnSearch_btnSubmit2"; //Additional Show Results
		public String btnShowResults_LearnerSearch= "ctl00_ContentMain_btnSearch_btnSearch";  //learner Search Show Results

		public String tblGradeFilterResults  ="ctl00_ContentMain_ElsevierGrid1_RadGrid1_ctl00";
		
		
		public String headerText_tblGradeFilterResults  ="//div[@id='ctl00_ContentMain_ElsevierGrid1_Div1']/h2"; //Grade Learners By Skill  - To get Module and Skill Name
		public String btnSave_GradeLearnersBySkills  ="ctl00_ContentMain_ElsevierGrid1_btnSave"; //ctl00_ContentMain_ElsevierGrid1_btnSave2
		
		//*******************RECORD EVALUATION PAGE- secondary window*******************************
		//UniversalGrade Radio options
		public String radUnivGradeS= "ctl00_ContentMain_rbAllS"; //Satisfactory 
		public String radUnivGradeU= "ctl00_ContentMain_rbAllU"; //Unsatisfactory
		public String radUnivGradeNP= "ctl00_ContentMain_rbAllNP"; //Not Performed
		//
		public String tblCheckListItems= "ctl00_ContentMain_gvChecklistItems"; /*Table : Checklist,Grades,Comments,(Preceptor,Date)|
		Row One is header and Actual Data Rows start from 2	*/																
		
		public String txtAreaOverallComments = "ctl00_ContentMain_txtComments"; //Overall Comments text area
		
		//Final Grade Radio options		
		public String radGradePass= "ctl00_ContentMain_rbPass";
		public String radGradeFail= "ctl00_ContentMain_rbFail";
		public String radGradeInProgress= "ctl00_ContentMain_rbInProgress";
		
		public String btnSave_GradeChanges = "ctl00_ContentMain_btnSave";//Save the Grade Changes
		public String btnBack_RecordEval = "ctl00_ContentMain_btnBack"; //Back button in Record Evaluation page
		
		public String headerText_LearnerNameInRecordEvaluationPage = "//div[@id='ctl00_ContentMain_dvChecklistForm']/h2";// To get LearnerName
		public String tblModuleSkillVersionDetails = "//div[@id='ctl00_ContentMain_dvChecklistForm']/h1/table"; //To get Module and Skill Name, version
		
		
		//CopySkill Dialogs
		public String dlgCopySkill="//div[text()='Are you sure you want to make a copy of this skill?']";
		public String dlgCopySkillYesButton = dlgCopySkill +"/following-sibling::div/div/*/span[text()='Yes']";
		
		
		//**********************Text to Compare with UI**********************
		//Text to display in UI dialog while copying Skill
		public String copySkillUIDialogText  = "Are you sure you want to make a copy of this skill?";
		//Title of Edit Skill Page
		public String editSkillPageTitle  ="Edit Skills Content - Modify General Info : Mosby's Skills";
		
		public String successMessageText ="Your request has been processed successfully";
		
		
		//Instances of other Page Objects & Methods
		EPM_HomePage homePage = new EPM_HomePage(scriptHelper);
		Common common = new Common(scriptHelper);
		
		
	/**
	 * Constructor to initialize the page
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object passed from the
	 *            {@link TestScript}
	 * @param driver
	 *            The {@link WebDriver} object passed from the
	 *            {@link TestScript}
	 */
	public EPM_SkillsPage(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
	}

	@Override
	protected void initializeUIMap()
	{

	}
	
	/* This method should be used only to Edit or Copy the Existing Skill.
	 * 
	 * @strSkillName -Any Skill Name on the first page (OR)
	 * It can be Empty String and then by default it will go to first row skill
	 * @strStatus - Active or Inactive only as String
	 * @strEditORCopy - EDIT or COPY as String only
	 */	
	public void clickCopyEditSkillLink(String strSkillName,String strStatus,String strEditORCopy) throws Exception {
		strSkillName = strSkillName.trim();
		int intRow = 0;
		int intSkillRow = 0;
		WebElement objLinkToClick= null;
		WebElement uiDialogCopySkill = null;
		try{					
			if(validateElementPresence("Skills table" , tblSkills )!=null){		
				selectFromDropDownByValue(selSkillStatus,strStatus);		
				waitTime(5);
				
				if(strSkillName.trim().equals("")){
					intSkillRow = 1;
				}else{
					intSkillRow = getRowWithTextInWebTableByID(tblSkills, strSkillName);				
				}
				
				if(intSkillRow != -1){									
					if(strEditORCopy.toUpperCase().equals("COPY")){
						objLinkToClick = getObjectWithRowColInWebTableByID(tblSkills, "a", 0, intSkillRow, 7);
					}else if(strEditORCopy.toUpperCase().equals("EDIT")){
						objLinkToClick = getObjectWithRowColInWebTableByID(tblSkills, "a", 0, intSkillRow, 6);
					}
					if(objLinkToClick!= null){						
						onMouseClick(objLinkToClick);
						waitTime(2);
						if(System.getProperty("browser").equalsIgnoreCase("chrome") && strEditORCopy.equals("COPY") ){								
							if(driver.findElements(By.xpath(dlgCopySkillYesButton)).size()!= 0){
								System.out.println("yes > 0");
								List<WebElement> objYes_Buttons = driver.findElements(By.xpath(dlgCopySkillYesButton));							
					            for(WebElement objDisplayed : objYes_Buttons){			            	
					                if(objDisplayed.isDisplayed()){	
					                	report.updateTestLog("Verify the UI dialog displayed",  " ' "+ copySkillUIDialogText + " ' " + " dialog is displayed", Status.DONE);
					                	onMouseClick(objDisplayed);					                	
					                	break;
					                }
					            }		
							}											
						}
						if(driver.getTitle().equals(editSkillPageTitle)){
							report.updateTestLog("Verify the '" + strEditORCopy + "' Skill funtionality",  " ' "+ editSkillPageTitle + " ' " + " is displayed Successfully", Status.DONE);
						}
					}
				}//if 
			}else{
				exitTestOnFail();
			}//if - table Exists			
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())+ " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}//catch
		
	}
	
	
	/* This method used to edit skills and Save the changes
	 * 
	 * @strSkillName -Skill Name to edit and internally it will append unique ID 
	 * @strSkillStatus -YES or NO as String
	 * @strAlertStatus - YES or NO as String
	 * @return void
	 * @throw exception
	 */	
	public void editSkillsAndSave(String strNewSkillName, String strSkillStatus, String strAlertStatus) throws Exception{
		try{	
			
			long strUniqID = new java.util.Date().getTime();
			
						
			//Enter or Select all the necessary fields			
			enterSkillName(strNewSkillName);
			selectRadioAlert(strAlertStatus);
			selectRadioSkillStatus(strSkillStatus);
			enterAlert("Sample Alert to Copy skill-" + strUniqID);
			enterSummary("");
			enterVerHistComments("Copying skill - Version History comment-" + strUniqID);	
			//Click Save Button
			clickSaveButton();
			 
			//Handle alert			
			if(actionOnAlertWithText(waitForAlert(5), "ACCEPT", "Are you sure you want to save these changes?")){
				report.updateTestLog("Verify and click 'Yes' on the Dialog",  "Save changes dialog is displayed and Accepted", Status.DONE);
			}else{
				report.updateTestLog("Verify and click 'Yes' on the Dialog",  "Save changes dialog is NOT displayed", Status.FAIL);
			}
				
			//Verify the Success Message
			homePage.verifySuccessMessage(successMessageText);
			//Verify Copied SKill displayed in Skills table
			searchSkillWithinIndex(strNewSkillName);
			
		
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "editSkillsAndSave" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	
	/* To Search a Skill within Index 
	 * @strSkillName -Any Skill Name with Particular Letter
	 * @returns void
	 * @throws Exception
	 */	
	public void searchSkillWithinIndex(String strSkillName) throws Exception{
		try{
			clickSkillLetterLink(strSkillName.substring(0,1));
			waitTime(1);
			typeOnElement(txtSkillSearchIndex, strSkillName);
			onMouseClick(validateElementDisplay("ShowSearchResults Button", btnShowSearchResults));			
			waitTime(2);
			verifySkillTablewithIndex(strSkillName);
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "editSkillsAndSave" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	
	/* To Search a Skill or using First letter of SkillName after Landing in Letter Index page using other function
	 * @strSkillNameOrLetter -Any Skill Name with Particular Letter or first letter of skill Name
	 * @returns void
	 * @throws Exception
	 */	
	public boolean verifySkillTablewithIndex(String strSkillNameOrLetter) throws Exception{
		boolean result = false;
		try{
			if(strSkillNameOrLetter.length() == 1){
				isExist(By.id(tblSkills), 10);
				int rowCnt = getRowsWebTableByID(tblSkills, "ROW");
				for(int i = 1;i<=rowCnt;i++){
					if(!getCellDataWebTableByID(tblSkills, i, 2).trim().substring(0, 1).equalsIgnoreCase(strSkillNameOrLetter)){
						result = false;
						break;
					}else{
						result = true;
					}
				}//for end
				if(result){
					report.updateTestLog("Verify the Presence of Skills start With Letter - '" +strSkillNameOrLetter+ "' only.",  " All Skills are with Letter '"+strSkillNameOrLetter+"' in Skills Search result table", Status.DONE);
				}else{
					report.updateTestLog("Verify the Presence of Skills start With Letter - '" +strSkillNameOrLetter+ "' only.",  " All Skills are NOT with Letter '"+strSkillNameOrLetter+"' in Skills Search result table", Status.FAIL);
				}
			}else{				
				if(getRowWithTextInColInWebTableByID(tblSkills, strSkillNameOrLetter, 2)!= -1){
					report.updateTestLog("Verify the Presence of Skill Name",  " ' "+ strSkillNameOrLetter + " ' " + " is displayed in Skills result table", Status.DONE);
					result = true;
				}else{
					report.updateTestLog("Verify the Presence of Skill Name",  " ' "+ strSkillNameOrLetter + " ' " + " is NOT displayed in Skills result table", Status.FAIL);
				}
			}			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "editSkillsAndSave" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
		return result;
	}// method end
	
	public void enterSkillName(String strSkillName) throws Exception{
		try{
			clearAndTypeOnElement(txtSkillName, strSkillName);
			//typeOnElement(txtSkillName, textToBeTyped);
			report.updateTestLog("Enter Skill Name",  " ' "+ strSkillName + " ' " + " is entered in the UserName text field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterSkillName" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void enterAlert(String strAlertText) throws Exception{
		try{			
			//typeOnElement(txtAlert, strAlertText);
			clearAndTypeOnElement(txtAlert, strAlertText);
			report.updateTestLog("Enter Alert",  " ' "+ strAlertText + " ' " + " is entered in the Alert textArea field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterAlert" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void selectRadioSkillStatus(String strOption) throws Exception{
		String strStatusID = "";
		try{			
			if(strOption.toUpperCase().equals("YES")){
				strStatusID = radActive + "_0";
			}else{
				strStatusID = radActive + "_1";
			}
			SetRadioButton(validateElementPresence("Radio SkillStatus", strStatusID));
			report.updateTestLog("Select Radio SkillStatus option",  " ' "+ strOption + " ' " + " is Selected in the SkillStatus Radio field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "selectRadioSkillStatus" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void selectRadioAlert(String strOption) throws Exception{
		String strAlertID = "";
		try{			
			if(strOption.toUpperCase().equals("YES")){
				strAlertID = radAlert + "_0";
			}else{
				strAlertID = radAlert + "_1";
			}
			SetRadioButton(validateElementPresence("Radio Alert", strAlertID));
			report.updateTestLog("Select Radio Alert option",  " ' "+ strOption + " ' " + " is Selected in the Alert Radio field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "selectRadioAlert" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	
	
	
	
	
	public void enterSummary(String strSummaryText) throws Exception{
		try{			
			typeOnElement(txtSummary, strSummaryText);
			//clearAndTypeOnElement(txtSummary, strSummaryText);
			report.updateTestLog("Enter Summary",  " ' "+ strSummaryText + " ' " + " is entered in the Summary textArea field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterSummary" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void enterVerHistComments(String strVerHistComments) throws Exception{
		try{			
			//typeOnElement(txtVerHistComments, strVerHistComments);
			clearAndTypeOnElement(txtVerHistComments, strVerHistComments);
			report.updateTestLog("Enter Ver Hist Comments",  " ' "+ strVerHistComments + " ' " + " is entered in the Ver Hist Comments textArea field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterVerHistComments" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void verifyLoginFuncAndHome() throws Exception {
		
	
		
		try{
			if(validateElementPresence("Logout Link", "link=Logout") != null && 
					validateElementPresence("Logout Link", "link=MyAccount") != null)
			{				
				report.updateTestLog("Verify the MNS Login functionality", 
						"MNS Home page is displayed with Logout and MyAccount link...", Status.PASS);
			}
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "verifyLoginFuncAndHome" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
		
		
	
	}
	
	
	public void clickSkillLetterLink(String strLetter){
		try{
			//clickElement("link="+strLetter.toUpperCase().trim());
			onMouseClick(validateElementDisplay("Letter " +strLetter.toUpperCase().trim() , "link="+strLetter.toUpperCase().trim()));
		
			report.updateTestLog("Click Skill Letter '" + strLetter + "' link",  "Link is clicked", Status.DONE);
			
			
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickSkillLetterLink" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	
	public void clickSaveButton(){
		try{
			clickElement(btnSave_Skill);
			report.updateTestLog("Click Save button",  "Save button is clicked", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickSaveButton" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	public void clickLogoutLink(){
		try{
			clickElement("link=Logout");
			report.updateTestLog("Click Logout Link",  "Logout link is clicked", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickLogoutLink" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	public void clickMyAccountLink(){
		try{
			clickElement("link=MyAccount");
			report.updateTestLog("Click MyAccount Link",  "MyAccount link is clicked", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickMyAccountLink" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	public void clickAdminTabLink(){
		try{
			clickElement("link=Admin");
			report.updateTestLog("Click Admin Tab Link",  "Admin Tab link is clicked", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickAdminTabLink" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	public void MNS_Logout(){
		try{
			clickLogoutLink();

		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterUserName" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	
	public WebElement VerifyCreateSkillButton(){
		WebElement element = null;		
		try{			
		element = validateElementPresence("Create a Skill button", "ctl00_btnCreateSkill");
		if(element != null){			
		
		}else{
			report.updateTestLog("Verify the Presence of Create Skill Button.",  "Create a Skill Button does NOT exist", Status.FAIL);
		}
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "VerifyCreateSkillButton" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return element;
		
	}
	public WebElement VerifySkillsManagementSection(){
		WebElement element = null;		
		try{			
		element = validateElementPresence("Skills Management Section", "ctl00_pnlAdminSkillsManagement");
		if(element != null){			
		
		}else{
			report.updateTestLog("Verify the Presence of Skills Management Section.",  "'Skills Management' Section does NOT exist", Status.FAIL);
		}
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "VerifySkillsManagementSection" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return element;
		
	}
	
	public WebElement VerifyAdminAccessManagementSection(){
		WebElement element = null;		
		try{			
		element = validateElementPresence("Admin Access Management Section", "ctl00_pnlAdminAccessManagement");
		if(element != null){			
		
		}else{
			report.updateTestLog("Verify the Presence of Admin Access Management Section.",  "'Admin Access Management' Section does NOT exist", Status.FAIL);
		}
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "VerifyAdminAccessManagementSection" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return element;
		
	}
	public WebElement VerifyAdminUsageReportSection(){
		WebElement element = null;		
		try{			
		element = validateElementPresence("Admin Usage Report Section", "ctl00_pnlAdminUsageReport");
		if(element != null){			
		
		}else{
			report.updateTestLog("Verify the Presence of Admin Usage Report Section.",  "'Admin Usage Report' Section does NOT exist", Status.FAIL);
		}
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "VerifyAdminUsageReportSection" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return element;
		
	}
	
	public void NavigateToAdminTab(){	
	
		try{
			clickAdminTabLink();
			
			if (VerifyCreateSkillButton().isDisplayed() && VerifySkillsManagementSection().isDisplayed() )
				report.updateTestLog("Verify the Admin Tab navigation",  "Admin tab is displayed with 'Create A Skill' Button ", Status.PASS);				
			else		
				report.updateTestLog("Verify the Admin Tab navigation",  "Admin tab is NOT displayed with 'Create A Skill' Button ", Status.FAIL);				
		}catch (Exception e){			
			report.updateTestLog("Error occured.", "NavigateToAdminTab function : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	
	//Below Functions to Grade Skills
	
	public void selectFilterToViewCheckListBy(String strFilterBy) throws Exception{
		try{
			selectFromDropDownByValue(selFilter1By, strFilterBy);
			report.updateTestLog("Select value '"+ strFilterBy +"' from FilterToViewCheckListBy drop down.",  "'"+ strFilterBy +"' is selected", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())  + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void selectModuleToViewCheckList(String strModuleName) throws Exception{
		try{
			selectFromDropDownByValue(selFilter1Option, strModuleName);
			common.verifyLoadingPleaseWaitMessageDisappear(30);
			report.updateTestLog("Select value '"+ strModuleName +"' from ModuleToViewCheckList drop down.",  "'"+ strModuleName +"' is selected", Status.DONE);
			//clickElement(btnShowResults1);
			onMouseClick(validateElementPresence("btnShowResults1", btnShowResults1));
			report.updateTestLog("Click 'Show Results' button.",  "Show Results' button is clicked.", Status.DONE);
			common.verifyLoadingPleaseWaitMessageDisappear(30);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())  + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void clickSkillNameForGrade(String strSkillNameForGrade) throws Exception{
		try{
			WebElement objSkillTable = null;
			objSkillTable = validateElementDisplay("Skills Table for Grading", tblGradeFilterResults);
			if(objSkillTable!=null){
				//int intSkillRow = getRowWithTextInColInWebTableByElement(objSkillTable, strSkillNameForGrade, 1);
				int intSkillRow = getRowWithTextInColInWebTableByID(tblGradeFilterResults, strSkillNameForGrade, 1);
				if(intSkillRow!= -1){
					WebElement objSkillLink = getObjectWithRowColInWebTableByElement(objSkillTable, "a", 0, intSkillRow, 1);
					if(objSkillLink!= null){
						onMouseClick(objSkillLink);	
						waitTime(3);//Wait time to page refresh
						if(common.verifyPageNameByH1TagHeader("Grade Learners By Skill") && validateElementDisplay("Learners table", tblGradeFilterResults)!=null){
							report.updateTestLog("Click Skill Named '"+ strSkillNameForGrade+ "' to grade",  "Skill '"+ strSkillNameForGrade+ "' is Clicked Successfully", Status.DONE);
						}else{
							report.updateTestLog("Click Skill Named '"+ strSkillNameForGrade+ "' to grade",  "Skill '"+ strSkillNameForGrade+ "' is Clicked but  'Grade Learners By Skill' Page might NOT loaded", Status.FAIL);
						}										
					}
				}else{
					report.updateTestLog("Click Skill Named'"+ strSkillNameForGrade+ "' to grade",  "Skill Does Not exist in Table", Status.FAIL);
				}
			}		
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())  + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void clickLearnerNameForGrade(String strLearnerNameToGrade) throws Exception{
		try{
			WebElement objLearnersTable = null;
			objLearnersTable = validateElementDisplay("Learners Table for Grading", tblGradeFilterResults);
			if(objLearnersTable!=null){
				//int intLearnerRow = getRowWithPartialTextInColInWebTableByElement(objLearnersTable, strLearnerNameToGrade, 1);
				int intLearnerRow = getRowWithPartialTextInColInWebTableByID(tblGradeFilterResults, strLearnerNameToGrade, 1);
				if(intLearnerRow!= -1){
					WebElement objLearnerLink = getObjectWithRowColInWebTableByElement(objLearnersTable, "a", 0, intLearnerRow, 1);
					if(objLearnerLink!= null){
						onMouseClick(objLearnerLink);	
						waitTime(5);
						if(common.verifyPageNameByH1TagHeader("Record Evaluation") && validateElementDisplay("Checklist Items table", tblCheckListItems)!=null){
							report.updateTestLog("Click Learner Named '"+ strLearnerNameToGrade+ "' to grade",  "Learner '"+ strLearnerNameToGrade+ "' is Clicked Successfully", Status.DONE);
						}else{
							report.updateTestLog("Click Learner Named '"+ strLearnerNameToGrade+ "' to grade",  "Learner '"+ strLearnerNameToGrade+ "' is Clicked but  'Record Evaluation' Page might NOT loaded", Status.FAIL);
						}										
					}
				}else{
					report.updateTestLog("Click Learner Named'"+ strLearnerNameToGrade+ "' to grade",  "Learner Does Not exist in Table", Status.FAIL);
				}
			}		
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())  + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public boolean verifyLearnerNameInRecordEvalPage(String strLearnerName) throws Exception{
		boolean result = false;
		try{
			WebElement objHeader = validateElementPresence("Learner Name in Header", headerText_LearnerNameInRecordEvaluationPage);
			if(objHeader!= null){
				if(objHeader.getText().toLowerCase().contains(strLearnerName.toLowerCase())){
					result = true;
				}
			}			
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())  + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	public boolean verifyModuleNameInRecordEvalPage(String strModuleName) throws Exception{
		boolean result = false;
		try{
			WebElement objTable = validateElementDisplay("Module Skill Verison table", tblModuleSkillVersionDetails);
			if(objTable!= null){
				if(verifyTextInWebTableByElement(objTable, strModuleName)){
					result = true;
				}
			}	
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())  + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	public boolean verifySkillNameInRecordEvalPage(String strSkillName) throws Exception{
		boolean result = false;
		try{
			WebElement objTable = validateElementDisplay("Module Skill Verison table", tblModuleSkillVersionDetails);
			if(objTable!= null){
				if(verifyTextInWebTableByElement(objTable, strSkillName)){
					result = true;
				}
			}	
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())  + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	public void gradeChecklistInRecordEvaluationPage(String strLearnerName, String strModuleName, String strSkillName, String strPreceptor, String strUnivGrade, String strOverallComments, String strOverallGrade) throws Exception{
		try{
			WebElement objChkListTable = null;
			objChkListTable = validateElementDisplay("Checklist Items table", tblCheckListItems);
			if(objChkListTable!=null && getRowsWebTableByElement(objChkListTable, "ROW")>1){
				if(verifyLearnerNameInRecordEvalPage(strLearnerName) && verifyModuleNameInRecordEvalPage(strModuleName) &&
						verifySkillNameInRecordEvalPage(strSkillName)){
					report.updateTestLog("Verify Learner, Module and Skill Name are correctly displayed.",  
							" Learner: '"+ strLearnerName + "' ; Module: '"+ strModuleName + "'; Skill: '"+ strSkillName + " are displayed correctly as expected in Record Eval Page", Status.PASS);
				}else{
					report.updateTestLog("Verify Learner, Module and Skill Name are correctly displayed.",  
							" Learner: '"+ strLearnerName + "' ; Module: '"+ strModuleName + "'; Skill: '"+ strSkillName + " is/are NOT displayed correctly as expected in Record Eval Page", Status.FAIL);				
				}
				
				selectRadioUniversalGrade(strUnivGrade);
				enterOverallGradeComment(strOverallComments);
				selectRadioOverallGrade(strOverallGrade);
				clickSaveGradeChanges();
				
				if(strUnivGrade.trim().equalsIgnoreCase("NOT PERFORMED")){					
					/*if(isDialogPresent()){						
						actionOnAlert(waitForAlert(2), "ACCEPT");
					}*/	
					try{
						System.out.println("handling alert");
						driver.switchTo().alert().accept();
					}catch(Exception e){
						System.out.println(e.getMessage());
					}
					try{
						System.out.println("handling active element");
						driver.switchTo().activeElement();
						System.out.println(driver.getWindowHandle());
					}catch(Exception e){
						System.out.println(e.getMessage());
					}
					closeAlert();
				}
				
				homePage.verifySuccessMessage("Your request has been processed successfully");	
				clickBackInRecordEvalPage();
				if(common.verifyPageNameByH1TagHeader("Grade Learners By Skill")){
					report.updateTestLog("Navigate back to 'Grade Learners By Skill' page",  "'Grade Learners By Skill' is displayed.",Status.DONE);
					verifyLearnerWithGradedInfo(strLearnerName,strOverallGrade,strOverallComments,strPreceptor);
				}
			}		
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())  + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/** To Verify all the below information is displayed in the Learners list table
	 * @param strLearnerName - Learner Last Name
	 * @param strGrade - PASS, FAIL, IN-PROGRESS
	 * @param strOverallComments - Any text
	 * @param strPreceptorName - Should be like 'Administrator, Mosby'  (LastName, FirstName)
	 * @return
	 * @throws Exception
	 */
	public boolean verifyLearnerWithGradedInfo(String strLearnerName, String strGrade, String strOverallComments, String strPreceptorName) throws Exception{
		boolean result = false;
		try{
			WebElement objLearnersTable = null;
			objLearnersTable = validateElementDisplay("Learners Grade table", tblGradeFilterResults);
			if(objLearnersTable!=null && getRowsWebTableByElement(objLearnersTable, "ROW")>0){
				int intLearnerRow = getRowWithPartialTextInColInWebTableByElement(objLearnersTable, strLearnerName, 1);	
				if(intLearnerRow!= -1){
					result = true;
					WebElement objGradeList = getObjectWithRowColInWebTableByElement(objLearnersTable, "select", 0, intLearnerRow, 2);
					String strGradeVal = getSelectedOptionInSelectListByElement(objGradeList).trim();				
					String strComments =  getCellDataWebTableByElement(objLearnersTable, intLearnerRow, 3).trim();
					String strPreceptor = getCellDataWebTableByElement(objLearnersTable, intLearnerRow, 4).trim();
					if(!strGradeVal.equalsIgnoreCase(strGrade.trim())){
						result = false;
						report.updateTestLog("Verify Learner graded displayed as '"+strGrade+"'.","Graded Value displayed as "+strGradeVal , Status.FAIL);
					}
					if(!strComments.toLowerCase().contains(strOverallComments.toLowerCase())){
						result = false;
						report.updateTestLog("Verify Overall comments displayed as '"+strOverallComments+"'.","Overall Comments displayed as "+strComments , Status.FAIL);
					}
					if(!strPreceptor.equalsIgnoreCase(strPreceptorName.trim())){
						result = false;
						report.updateTestLog("Verify Preceptor Name displayed as '"+strPreceptorName+"'.","Preceptor Name displayed as "+strPreceptor , Status.FAIL);
					}
				}
			}//if end		
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())  + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	public void selectRadioUniversalGrade(String strOption) throws Exception{
		String strUnivGradeID = "";
		//Satisfactory ,Unsatisfactory ,Not Performed
		try{			
			if(strOption.toUpperCase().equals("SATISFACTORY")){
				strUnivGradeID = radUnivGradeS;
			}else if(strOption.toUpperCase().equals("UNSATISFACTORY")){
				strUnivGradeID =radUnivGradeU;
			}else if(strOption.toUpperCase().equals("NOT PERFORMED")){
				strUnivGradeID = radUnivGradeNP;
			}
			SetRadioButton(validateElementPresence("Radio UniversalGrade", strUnivGradeID));
			report.updateTestLog("Select Radio Universal Grade option",  " ' "+ strOption + " ' " + " is Selected in the Universal Grade Radio field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())  + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public void enterOverallGradeComment(String strGradeComment) throws Exception{
			
		try{			
			clearAndTypeOnElement(txtAreaOverallComments, strGradeComment.trim());			
			report.updateTestLog("Enter Grade Overall comment ",  " ' "+ strGradeComment + " ' " + " is entered in  Overall Grade Comment text area field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void selectRadioOverallGrade(String strOption) throws Exception{
		String strGradeID = "";		
		
		try{			
			if(strOption.toUpperCase().equals("PASS")){
				strGradeID = radGradePass;
			}else if(strOption.toUpperCase().equals("FAIL")){
				strGradeID =radGradeFail;
			}else if(strOption.toUpperCase().equals("IN-PROGRESS")){
				strGradeID = radGradeInProgress;
			}
			SetRadioButton(validateElementPresence("Radio Overall Grade", strGradeID));
			report.updateTestLog("Select Radio Overall Grade option",  " ' "+ strOption + " ' " + " is Selected in the Overall Grade Radio field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void clickSaveGradeChanges() throws Exception{
		
		try{			
			//clickElement(btnSave_GradeChanges);	
			onMouseClick(validateElementPresence("btnSave_GradeChanges", btnSave_GradeChanges));
			report.updateTestLog("Click Save button",  "Save Button clicked to Save the Grade Changes.", Status.DONE);			
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	public void clickBackInRecordEvalPage() throws Exception{
		
		try{			
			clickElement(btnBack_RecordEval);	
			report.updateTestLog("Click Back button in Record Eval Page",  "Back Button clicked to navigate to 'Grade Skills By ...'Page", Status.DONE);			
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}

}