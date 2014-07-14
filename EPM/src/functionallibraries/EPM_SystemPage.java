package functionallibraries;

import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import supportlibraries.*;

import com.cognizant.framework.*;
import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;

/**
 *
 * 
 * @author Cognizant
 */
public class EPM_SystemPage extends MasterPage
{
	
	//Instances of other Page Objects & Methods
		EPM_HomePage homePage = new EPM_HomePage(scriptHelper);	
		EPM_LearnersPage learnersPage = new EPM_LearnersPage(scriptHelper);	
		Common common = new Common(scriptHelper);	
		
		
	//Object Locator Values	
	//Group Creation (Add New Group)
	public String btnAddNewGrp  = "ctl00_ctl00_ContentMain_ContentMain_rdGridGroups_ctl00_ctl02_ctl00_btnInitInsert";	
	public String txtGroupCode = "ContentMain_ContentMain_txtGroupCode";
	public String txtGroupDesc = "ContentMain_ContentMain_txtGroupName";
	public String btnInsertGroup = "ContentMain_ContentMain_btnSave";
	public String tblGroupTable = "ctl00_ctl00_ContentMain_ContentMain_rdGridGroups_ctl00"; 
	
	//Add Learners to Group
	public String btnListLearner  = "ContentMain_ContentMain_btnListLearner";	
	public String lstAvailableLearners ="ctl00_ctl00_ContentMain_ContentMain_rdlstAvailableUsers"; 
	public String lstSelectedLearners ="ctl00_ctl00_ContentMain_ContentMain_rdlstSelectedUsers"; 
	
	
	public String btnAddNewDept  = "ctl00_ctl00_ContentMain_ContentMain_rdDepartments_ctl00_ctl02_ctl00_btnInitInsert";

		
	
		
	
	
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
	public EPM_SystemPage(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
	}

	@Override
	protected void initializeUIMap()
	{

	}
	
	public void createNewDepartment(String strDeptName) throws Exception{
		
	}
	
	public void createNewGroup(String strGroupCode, String strGroupDesc) throws Exception{
		try{
			clickAddNewGroupButton();
			enterGroupCode(strGroupCode);
			enterGroupDesc(strGroupDesc);
			clickInsertGroupButton();
			waitTime(2);
			if(verifyGroupNameInTable(strGroupCode)){
				report.updateTestLog("Create a Group with Name '" + strGroupCode + "'" , "Group is created and verified", Status.PASS);	
			}else{
				report.updateTestLog("Create a Group with Name '" + strGroupCode + "'" , "Group is NOT created and/or verified", Status.FAIL);
			}
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	public void addLearnersToGroup(String strGroupCode, String[] arrLearnerLastName) throws Exception{
		try{
			
			clickGroupByName(strGroupCode);
			for(int i=0;i<arrLearnerLastName.length;i++){				
				learnersPage.enterLearnerLastName(arrLearnerLastName[i]);
				clickListLearnersButton();
				if(moveListItemsByPartialName(lstAvailableLearners, lstSelectedLearners, arrLearnerLastName[i], "TORIGHT")){
					System.out.println(arrLearnerLastName[i] + " - Added to Right");
					report.updateTestLog("Add a Learner " +arrLearnerLastName[i] +" to a group'" + strGroupCode + "'" , "Learner is Added to Group Sucessfully", Status.DONE);
				}				
			}	
			learnersPage.clickSaveButton();
			homePage.verifySuccessMessage("Your request has been processed successfully");
			
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	public boolean moveListItemsByPartialName(String strAvailableID, String strSelectedID,String strUniqueItemName, String strAction) throws Exception{
		boolean result = false;
		try{			
			String strXPathButton = "";
			String strXPathItem = "//ul[@class='rlbList']/*/span[contains(text(),'"+strUniqueItemName+"')]";
			WebElement lstLeftAvailable = validateElementPresence(strAvailableID, strAvailableID);
			
			WebElement lstRightSelected = validateElementPresence(strSelectedID, strSelectedID);
			
			WebElement itemToSelect = null;
			WebElement itemToVerify = null;
			if(lstLeftAvailable!=null && lstRightSelected!=null){
				if(strAction.equalsIgnoreCase("ToRight")){
					strXPathButton = "//*/a[@title='To Right']";
				}else if(strAction.equalsIgnoreCase("ToLeft")){
					strXPathButton = "//*/a[@title='To Left']";
				}else if(strAction.equalsIgnoreCase("AllToRight")){
					strXPathButton = "//*/a[@title='All To Right']";
				}else if(strAction.equalsIgnoreCase("AllToLeft")){
					strXPathButton = "//*/a[@title='All To Left']";
				}
				//Select Value in Left
				if(strAction.equalsIgnoreCase("ToRight") || strAction.equalsIgnoreCase("AllToRight")){	
					itemToSelect = validateElementPresence(strUniqueItemName + " value in list", strXPathItem);
					//itemToSelect = lstLeftAvailable.findElement(By.xpath(strXPathItem));				
				}else{
					itemToSelect = lstRightSelected.findElement(By.xpath(strXPathItem));
				}
				if(itemToSelect!=null){
					onMouseClick(itemToSelect);					
				}	
							
				//Button to CLick  (rlbDisabled  - class attribute for Disabled objects)
				//WebElement btnToClick = lstLeftAvailable.findElement(By.xpath(strXPathButton));
				WebElement btnToClick = validateElementPresence(strAction + " Button", strXPathButton);	
				if(!btnToClick.getAttribute("class").contains("rlbDisabled")){					
					onMouseClick(btnToClick);
					result = true;
				}else{
					System.out.println("Error- Button Not clickable");
					exitTestOnFail();
				}
				
				//lstLeftAvailable = validateElementPresence(strAvailableID, strAvailableID);
				//lstRightSelected = validateElementPresence(strSelectedID, strSelectedID);
				//verify the Value in Right
				if(strAction.equalsIgnoreCase("ToRight") || strAction.equalsIgnoreCase("AllToRight")){					
					itemToVerify = validateElementPresence(strUniqueItemName + " value in list", strXPathItem);				
				}else{
					//itemToVerify = lstLeftAvailable.findElement(By.xpath(strXPathItem));
					itemToVerify = validateElementPresence(strUniqueItemName + " value in list", strXPathItem);		
				}
				if(itemToVerify!=null){
					System.out.println("object moved");
					result = true;
				}
			}			
		}
		catch(StaleElementReferenceException ex){
			System.out.println("Ignoring StaleElementReferenceException @ " + getCurrentMethodName(this.getClass().getName()));
		}
		catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
		return result;
	}
	public boolean verifyGroupNameInTable(String strGroupName) throws Exception{
		return common.verifyItemNameInPaginationTable(tblGroupTable, strGroupName);
		/*boolean result = true;
		try{
			if(validateElementPresence("Group Table", tblGroupTable)!= null){
				while(getRowWithTextInColInWebTableByID(tblGroupTable, strGroupName, 1)== -1){
					result = false;
					if(!common.clickPagination(tblGroupTable, "NEXT")){
						break;
					}
				}
				if(getRowWithTextInColInWebTableByID(tblGroupTable, strGroupName, 1)!= -1){
					result = true;
				}
			}			
		}catch(Exception e){			
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
		return result;*/
	}
	
	
	public void clickGroupByName(String strGroupName) throws Exception{
		
		try{	
			
			onMouseClick(validateElementPresence(strGroupName + " link", "link="+strGroupName));
			//clickElement("link="+strGroupName);
			report.updateTestLog("Click Group " + strGroupName,  "Group '"+ strGroupName +"' link is Clicked", Status.DONE);
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	
	public void clickListLearnersButton() throws Exception{
		
		try{
			onMouseClick(validateElementPresence("List Learner", btnListLearner));
			//clickElement(btnListLearner);
			report.updateTestLog("Click List Learners button.",  "List Learners Button is Clicked", Status.DONE);			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	public void clickAddNewGroupButton() throws Exception{
		
		try{	
			WebElement objAddNewGrp = validateElementPresence(btnAddNewGrp, btnAddNewGrp);
			if(objAddNewGrp!= null){
				onMouseClick(objAddNewGrp);
				report.updateTestLog("Click Add New Group button.",  "Add New Group Button is Clicked", Status.DONE);
			}
			
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	
	
	public void clickInsertGroupButton() throws Exception{
		
		try{	
			
			//clickElement(btnInsertGroup);
			onMouseClick(validateElementPresence("Insert Group", btnInsertGroup));
			report.updateTestLog("Click Insert Group button.",  "Insert Group button is clicked", Status.DONE);
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickInsertGroupButton" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	
	
	public void enterGroupCode(String strGroupCode) throws Exception{
		try{	
			typeOnElement(txtGroupCode, strGroupCode);		
			report.updateTestLog("Enter GroupCode",  " ' "+ strGroupCode + " ' " + " is entered in the GroupCode text field", Status.DONE);
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterGroupCode" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	
	public void enterGroupDesc(String strGroupDesc) throws Exception{
		try{	
			typeOnElement(txtGroupDesc, strGroupDesc);		
			report.updateTestLog("Enter Group Description",  " ' "+ strGroupDesc + " ' " + " is entered in the Group Description text field", Status.DONE);
			
		}catch(Exception e){
		report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterGroupDesc" + " : " + e.getMessage(), Status.FAIL);
		e.printStackTrace();
		throw e;
		}
	}
	
	
}//class end