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
public class EPM_LearnersPage extends MasterPage
{
	
	
	
	
	
	//Object Locator Values
	
	//Learner Creation (Add New Learner)
	public String txtLearnerID  = "ContentMain_ContentMain_txtLearnerId";	
	public String txtLearnerFn  = "ContentMain_ContentMain_txtFirstName";
	public String txtLearnerLn  = "ContentMain_ContentMain_txtLastName";
	public String selFacilities  = "ContentMain_ContentMain_ddlFacilities";	
	public String selDepartments  = "ContentMain_ContentMain_ddlDepartments";
	
	
	public String radSortByDeptName  = "ContentMain_ContentMain_rbSortByDepartmentName";	
	public String radSortByDeptCode  = "ContentMain_ContentMain_rbSortByDepartmentCode";
	
	
	
	public String txtSubDept  = "ContentMain_ContentMain_txtSubDepartment";
	public String txtJobCode  = "ContentMain_ContentMain_txtPositionCode";
	public String txtBirthDate  = "ctl00_ctl00_ContentMain_ContentMain_rdBirthDatePicker_dateInput";
	public String txtHiteDate  = "ctl00_ctl00_ContentMain_ContentMain_rdHireDatePicker_dateInput";
	public String txtEmail  = "ContentMain_ContentMain_txtEmailAddress";
	public String txtVerCode  = "ContentMain_ContentMain_txtVerificationCode";
	
	public String btnSaveLearner = "ContentMain_ContentMain_btnSave";
	public String btnCancelLearner  = "ContentMain_ContentMain_btnCancel";
	
		
	
	//Instances of other Page Objects & Methods
	EPM_HomePage homePage = new EPM_HomePage(scriptHelper);
		
		
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
	public EPM_LearnersPage(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
	}

	@Override
	protected void initializeUIMap()
	{

	}
	
	
	/* This method used to create a New Learner
	 * 
	 * @strLearnerID
	 * @strFirstName
	 * @strLastName
	 * @strFacility
	 * @strDepartment 
	 * @strEmailID	
	 * @return void
	 * @throw exception
	 */	
	public void createNewLearner(String strLearnerID, String strFirstName, String strLastName, String strFacility, String strDepartment, String strEmailID) throws Exception{
		try{	
						
			//Enter or Select all the necessary fields			
			enterLearnerID(strLearnerID);
			enterLearnerFirstName(strFirstName);
			enterLearnerLastName(strLastName);			
			selectListFacility(strFacility);
			selectRadioSortDepartmentBy("NAME");
			selectListDepartment(strDepartment);			
			enterLearnerEmailID(strEmailID);
			clickSaveLearnerButton();
			homePage.verifySuccessMessage("Your request has been processed successfully.");
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName())+ " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/* This method used to enter a LearnerID
	 * 
	 * @strLearnerID	 
	 * @return void
	 * @throw exception
	 */	
	public void enterLearnerID(String strLearnerID) throws Exception{
		try{	
			typeOnElement(txtLearnerID, strLearnerID);		
			report.updateTestLog("Enter LearnerID",  " ' "+ strLearnerID + " ' " + " is entered in the Learner ID field", Status.DONE);
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterLearnerID" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	/* This method used to enter Learner FirstName
	 * 
	 * @strFirstName	 
	 * @return void
	 * @throw exception
	 */	
	public void enterLearnerFirstName(String strFirstName) throws Exception{
		try{	
			typeOnElement(txtLearnerFn, strFirstName);		
			report.updateTestLog("Enter FirstName",  " ' "+ strFirstName + " ' " + " is entered in the Learner FirstName field", Status.DONE);
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterLearnerFirstName" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	/* This method used to enter a Learner LastName
	 * 
	 * @strLastName	 
	 * @return void
	 * @throw exception
	 */	
	public void enterLearnerLastName(String strLastName) throws Exception{
		try{	
			clearAndTypeOnElement(txtLearnerLn, strLastName);
			//typeOnElement(txtLearnerLn, strLastName);		
			report.updateTestLog("Enter LastName",  " ' "+ strLastName + " ' " + " is entered in the Learner LastName field", Status.DONE);
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterLearnerLastName" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}

	/* This method used to select List Facility
	 * @strFacility	 
	 * @return void
	 * @throw exception
	 */	
	public void selectListFacility(String strFacility) throws Exception{
		try{	
			
			selectFromDropDownByValue(selFacilities,strFacility);
			waitTime(2);
			report.updateTestLog("Select List Facility",  " ' "+ strFacility + " ' " + " is selected in the Learner facility ListBox", Status.DONE);
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "selectListFacility" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	
	/* This method used to select List Department
	 * @strDepartment	 
	 * @return void
	 * @throw exception
	 */	
	public void selectListDepartment(String strDepartment) throws Exception{
		try{	
			
			//selectFromDropDownByValue(selDepartments,strDepartment);
			if(selectRandomIfOptionNotExist(selDepartments, strDepartment)){
				report.updateTestLog("Select List Department",  " ' "+ strDepartment + " ' " + " is selected in the Learner Department ListBox", Status.DONE);
			}else{
				report.updateTestLog("Select List Department",  " ' "+ strDepartment + " ' " + " is NOT selected in the Learner Department ListBox", Status.DONE);
			}
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "selectListDepartment" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	
	/* This method used to Select Radio option - SortByDepartment Name or Code
	 * 
	 * @strSortBY - NAME or CODE
	 * @return void
	 * @throw exception
	 */	
	public void selectRadioSortDepartmentBy(String strSortBY) throws Exception{
		
		try{			
			if(strSortBY.toUpperCase().equals("NAME")){
				SetRadioButton(validateElementPresence("Radio SortByDeptName", radSortByDeptName));
				report.updateTestLog("Select Radio SortDepartmentByName option",  " SortDepartmentByName Radio is Selected", Status.DONE);
			}else{
				SetRadioButton(validateElementPresence("Radio SortByDeptCode", radSortByDeptCode));
				report.updateTestLog("Select Radio SortDepartmentByCode option",  " SortDepartmentByCode Radio is Selected", Status.DONE);
			}
			waitTime(2);
			
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "selectRadioSortDepartmentBy" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	/* This method used to enter a Learner EmailID
	 * 
	 * @strEmailID	 
	 * @return void
	 * @throw exception
	 */	
	public void enterLearnerEmailID(String strEmailID) throws Exception{
		try{	
			typeOnElement(txtEmail, strEmailID);		
			report.updateTestLog("Enter EmailID",  " ' "+ strEmailID + " ' " + " is entered in the Learner EmailID field", Status.DONE);
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterLearnerEmailID" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	

	/**This method used to save the Learner Creation 
	 * 
	 * @throws Exception
	 */
	
	public void clickSaveLearnerButton() throws Exception{
		clickSaveButton();
	}
	
	
	/**This method used to Click the Save Button
	 * 
	 * @throws Exception
	 */
	public void clickSaveButton() throws Exception{
		try{	
			
			clickElement(btnSaveLearner);
			report.updateTestLog("Click Save button",  "Save Button is Clicked", Status.DONE);
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
}//class end