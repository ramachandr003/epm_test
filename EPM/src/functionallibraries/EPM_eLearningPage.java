package functionallibraries;

import java.util.Hashtable;
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
 *
 * 
 * @author Cognizant
 */
public class EPM_eLearningPage extends MasterPage
{
	
	//Instances of other Page Objects & Methods
		EPM_HomePage homePage = new EPM_HomePage(scriptHelper);	
		EPM_LearnersPage learnersPage = new EPM_LearnersPage(scriptHelper);	
		Common common = new Common(scriptHelper);	
		
		
	//Object Locator Values	
	//
	public String selContentSource  = "ContentMain_ContentMain_DropDownList2";	
	public String selCourses  = "ContentMain_ContentMain_DropDownList1"; // for Courses when Elsevier Content
	public String selCategories  = "ContentMain_ContentMain_DropDownList3"; //for Categories
	public String selSpecialties = "ContentMain_ContentMain_DropDownList4"; //for Specialties 
	public String btnSelect = "//*/input[@value='Select' and @class='ucd-button']"; //for any Select button
	
	//public String tblContent = "//div[@class='mcs_panel_content']/div/table"; // Row starts from 2
	public String tblContent =  "//table[@class='RadGrid RadGrid_Office2007']";
	public String btnAddToSavedItems = "//*/input[@value='Add to Saved Items']";
	public String btnCreateNewModule = "//*/input[@value='Create a New Module']";
	public String btnRemoveAllItems = "//*/input[@value='Remove ALL Saved Items']";	
	public String tblSavedItemQueue = "ctl00_ctl00_ContentMain_ContentMain_RadGrid2_ctl00"; // Row Starts from 1 ; 3 cols

	
	//Edit Module Page locators
	public String selModuleToEdit = "ContentMain_ContentMain_EditModuleCtl1_DropDownList1";	//Current Module List Box
	public String btnSelectModuleToEdit = "//*/input[@value='Select Module']"; //Select Module Button to click	
	public String chkBoxCheckListRequiredYes = "name=ckRequired1";
	public String chkBoxCheckListRequiredNo = "name=ckRequired0";	
	public String chkBoxRandomizeTestYes = "name=rand1";
	public String chkBoxRandomizeTestNo = "name=rand0";	
	public String btnUpdate = "//*/input[@value='Update']"; //to update the Items(Lessons) Properties
	
	public String tblAssignableItems =  "//*/table[contains(@id,'_EditModuleTabContainer_tabLessons_RadGrid1_ctl00')]"; //Row starts from 1
	public String tblSkillsPreceptors ="//*/table[contains(@id,'_EditModuleTabContainer_tabSkills_RadGrid2_ctl00')]";  //Row starts from 1
	
	public String btnAddPreceptors_SkillsPreceptorsTab ="//*/input[@value='Add Preceptor(s)' and contains(@id,'_EditModuleTabContainer_tabSkills_')]";
	
	public String btnAddPreceptors_KnowledgeManagerTab ="//*/input[@value='Add Preceptor(s)' and contains(@id,'_EditModuleTabContainer_tabKnowledgeManager_')]";

	
	//
	public String selDeptModules ="ContentMain_ContentMain_ddDeptModules"; //Modules your Department Created:
	
	public String btnChooseGroups=  "//input[@value='Choose Groups']";
	public String btnOkSaveGroups=  "ContentMain_ContentMain_btnSaveGroups";//OK Button in Modal window
	public String btnOkSaveDepartments = "ContentMain_ContentMain_btnSaveDepartments";//OK Button in Modal window
	public String btnOkSaveFacilities = "ContentMain_ContentMain_btnSaveFacilities";
	public String btnAssignNow_DeptModules=  "ContentMain_ContentMain_btnAssignDeptModule";
	
	public String tblSelectedFacilities = "ctl00_ctl00_ContentMain_ContentMain_panelBar_i0_lstSelectedFacilities";
	public String tblSelectedDepartments ="ctl00_ctl00_ContentMain_ContentMain_panelBar_i1_lstSelectedDepartments";
	public String tblSelectedGroups ="ctl00_ctl00_ContentMain_ContentMain_panelBar_i2_lstSelectedGroups";
	public String tblSelectedUsers ="ctl00_ctl00_ContentMain_ContentMain_panelBar_i3_lstSelectedUsers";

	//Below Locators will work only if Driver Control Switch to Respective farme **** IFRAME *****
	
	public String txtNewModuleName ="ContentMain_TextBox1";
	public String chkBoxMakeModuleGlobal = "ContentMain_CheckBox1";
	public String btnCreateModule=  "//*/input[@value='Create Module']";
	public String chkBoxSelectAll = "//*/input[@type='checkbox' and contains(@onclick,'selectAllRows')]";
	
	public String tblItemsLessonsData ="//*[@id='ctl00_ContentMain_RadGrid1_ctl00']";  //Row Starts from 1 
	public String pnlModuleCreated ="ContentMain_pnlModuleCreated"; 
	
	public String btnCreateAnotherModule = "//*/input[@value='Create Another Module']";
	public String btnEditModuleProperties = "//*/input[@value='Edit Module Properties']";
	public String btnAssignTraining = "//*/input[@value='Assign Training']";
	
	public String tblManageModulePreceptors = "//table[@id='ctl00_ContentMain_RadGrid1_ctl00']"; /*Table to select and add Preceptors 																								
																									in Skills and KM Preceptors Tabs*/
	
	public String btnAddSelectedUsers = "//*/input[@value='Add Selected Users']";
	
	//Frame names to use in iframeLocator
	
	public String frameCreateNewModule = "create new module";
	public String frameManageModulePreceptors = "manage module preceptors";

	
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
	public EPM_eLearningPage(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
	}

	@Override
	protected void initializeUIMap()
	{

	}
	
	
	
	
	
	
	
	public String createNewModule(String strNewModuleName,String strContentSource,String[] itemsList, boolean blnGlobal ) throws Exception{
		
		String strModuleName = "";
		try{
			
			clickCreateNewModuleButton();			
			if(validateElementPresence(frameCreateNewModule, getIframeLocatorByName(frameCreateNewModule))!= null){
				switchFrame(frameCreateNewModule);
				enterNewModuleName(strNewModuleName.trim());
				selectCheckBoxGlobal(blnGlobal);
				WebElement objContentTable = validateElementPresence("Content/Lessons Table", tblItemsLessonsData);
				
				if(itemsList.length>0 && objContentTable!=null){
					for(int j= 0 ; j<itemsList.length;j++){
						selectCheckBoxContentItemByName(objContentTable,itemsList[j]);
					}
				}
				
				clickCreateModuleButton();
				waitTime(1);
				homePage.verifySuccessMessage("Your request has been processed successfully");
				if(blnGlobal){
					//G-2012 ATCN
					strModuleName = "G-" + strNewModuleName;
				}else{
					//L-2011 Annual Skills (no Wound Photo) ChkLists
					strModuleName = "L-" + strNewModuleName;
				}
				//Close Frame and Control Back to TopFrame
				driver.switchTo().defaultContent();				
				closeFrame(frameCreateNewModule.toUpperCase());
			}
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
		return strModuleName;
	}
	
	public boolean addItemsToQueue(String[] itemsList) throws Exception{
		boolean result = true;
		try{
			int intArrayLength = itemsList.length;
			WebElement objContentTable = validateElementDisplay("Content Table", tblContent);	
			WebElement objCheckBox = null;
			if(objContentTable!= null){
				if(intArrayLength > 0 ){
					for(int k=0;k<intArrayLength;k++){						
						if(System.getProperty("browser").equalsIgnoreCase("iexplore")){
							objCheckBox = validateElementPresence("Item Checkbox for " + itemsList[k] , "//a/span[text()='"+itemsList[k].trim()+"']/../../preceding-sibling::td/span/input");
							result = false;
							if(objCheckBox !=null && objCheckBox.isEnabled()){
								onMouseClick(objCheckBox);								
								report.updateTestLog("Select the Checkbox for Item(Lessons/Skills) '" + itemsList[k].trim() + "'",  "Item  '" + itemsList[k].trim() + "' checkbox is Selected", Status.DONE);
								result = true;
							}else{
								result = false;
								report.updateTestLog("Select the Checkbox for Item(Lessons/Skills) '" + itemsList[k].trim() + "'",  "Item  '" + itemsList[k].trim() + "' checkbox is NOT Selected", Status.FAIL);
								exitTestOnFail();
							}							
						}else{
							if(!selectCheckBoxContentItemByName(objContentTable,itemsList[k])){
								result = false;
								exitTestOnFail();
							}
						}//if else end							
					}
					if(result){
						clickAddToSavedItems();
					}
				}else{
					report.updateTestLog("Add Items to Queue",  "ERROR : No Items given to add", Status.FAIL);
					result = false;
				}				
			}else{
				report.updateTestLog("Add Items to Queue from Content Table ",  "ERROR :Content Table Does NOT exist", Status.FAIL);
				result = false;
			}			
		}catch(Exception e){
			result = false;
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	public boolean selectCheckBoxContentItemByName(WebElement objContentTable, String strItemName) throws Exception{
		boolean result = false;
		try{			
			if(objContentTable !=null){			
				int intItemRow = getRowWithTextInColInWebTableByElement(objContentTable, strItemName, 2);
				if(intItemRow != -1){
					WebElement objChBox = getObjectWithRowColInWebTableByElement(objContentTable, "input", 0, intItemRow, 1);
					if(objChBox!= null && objChBox.isEnabled() && !objChBox.isSelected()){
						//objChBox.click();
						onMouseClick(objChBox);
						if(objChBox.isSelected()){
						result = true;
						report.updateTestLog("Select the Checkbox for Item(Lessons/Skills) '" + strItemName + "'",  "Item  '" + strItemName + "' checkbox is Selected", Status.DONE);
						}						
					}
				}else{
					report.updateTestLog("Select the Checkbox for Item(Lessons/Skills) '" + strItemName + "'",  "Item does NOT exist", Status.FAIL);
				}
			}
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
		return result;
	}
	
	
	public boolean selectCheckBoxPreceptorsByName(String strPreceptorLastAndFirstName) throws Exception{
		boolean result = false;
		String strLastName = "";
		String strFirstName = "";
		try{			
			
			WebElement objPreceptorsTable = validateElementDisplay("Preceptors Table", tblManageModulePreceptors);			
			if(objPreceptorsTable !=null){
				if(strPreceptorLastAndFirstName!=""){
					strLastName = strPreceptorLastAndFirstName.split(",")[0].trim();
					strFirstName = strPreceptorLastAndFirstName.split(",")[1].trim();
				}else{
					report.updateTestLog("Select the Checkbox for Preceptor(LastName, FirstName) '" + strPreceptorLastAndFirstName + "'",  "ERROR : Preceptor value is null", Status.FAIL);
				}
				int intItemRow = getRowWithTextInTwoColsInWebTableByElement(objPreceptorsTable, strLastName, 2,strFirstName, 3);				
				if(intItemRow != -1){
					WebElement objChBox = getObjectWithRowColInWebTableByElement(objPreceptorsTable, "input", 0, intItemRow, 1);
					if(objChBox!= null && objChBox.isEnabled() && !objChBox.isSelected()){
						objChBox.click();
						if(objChBox.isSelected()){
						result = true;	
						report.updateTestLog("Select the Checkbox for Preceptor(LastName, FirstName) '" + strPreceptorLastAndFirstName + "'",  "Preceptor " + strPreceptorLastAndFirstName + " is Selected ", Status.DONE);
						}						
					}
				}else{
					report.updateTestLog("Select the Checkbox for Preceptor(LastName, FirstName) '" + strPreceptorLastAndFirstName + "'",  "Preceptor does NOT exist", Status.FAIL);
				}
			}
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
		return result;
	}
	
	public void manageAssignableItemQueue(String strContentSource,String[] itemsList) throws Exception{
		try{
			clickRemoveAllSavedItemsButton();
			selectContentSource(strContentSource);
			
			
			if(strContentSource.trim().equalsIgnoreCase("Elsevier Licensed Content")){
				
			}else if(strContentSource.trim().equalsIgnoreCase("Linked Content")){
				
			}else if(strContentSource.trim().equalsIgnoreCase("Assessments")){
				
			}else if(strContentSource.trim().equalsIgnoreCase("Checklists")){
				
			}else if(strContentSource.trim().equalsIgnoreCase("Discussions")){
				
			}else if(strContentSource.trim().equalsIgnoreCase("eLearning Modules")){
				
			}else if(strContentSource.trim().equalsIgnoreCase("Mosby Skills")){
				//No other Selection required for Adding Skills Content - Go and Add Items to Queue
			}else if(strContentSource.trim().equalsIgnoreCase("Mosby's Essential Nursing CE")){
				
			}
			
			if(addItemsToQueue(itemsList)){
				
			}else{
				System.out.println("FAILED - Items added to Queue");
				exitTestOnFail();
			}
			
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	
	
	public void enterNewModuleName(String strNewModuleName) throws Exception{
		try{
			typeOnElement(txtNewModuleName, strNewModuleName);
			report.updateTestLog("Enter value '"+ strNewModuleName +"'",  "'"+ strNewModuleName +"' is entered in New Module text field", Status.DONE);
		}catch(Exception e){
			System.out.println("Check for Frame switch");
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public boolean selectCheckBoxGlobal(boolean blnCheck) throws Exception{
		boolean result = false;
		try{
			WebElement objChBox = validateElementDisplay("Global Module CheckBox", chkBoxMakeModuleGlobal);
			if(blnCheck){				
				if(objChBox!= null && objChBox.isEnabled() && !objChBox.isSelected()){
					objChBox.click();
					if(objChBox.isSelected()){
					result = true;
					}						
				}
			}else{
				if(objChBox!= null && !objChBox.isSelected()){
					result = true;
				}
			}
			
		}catch(Exception e){
			System.out.println("Check for Frame switch");
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	
	public boolean selectCheckBoxPreRequisite(String strCheck) throws Exception{
		boolean result = false;
		try{
			WebElement objChkBox = null;
			if(strCheck.trim().toUpperCase().equals("YES")){
				objChkBox = validateElementPresence("PreRequisite YES CheckBox", chkBoxCheckListRequiredYes);
			}else{
				objChkBox = validateElementPresence("PreRequisite NO CheckBox", chkBoxCheckListRequiredNo);
			}
						
			if(objChkBox!= null && objChkBox.isEnabled() && !objChkBox.isSelected()){
				objChkBox.click();
				if(objChkBox.isSelected()){
				result = true;
				}						
			}		
			
			if(strCheck.trim().toUpperCase().equals("YES")){
				report.updateTestLog("Select PreRequisite 'YES' CheckBox",  "PreRequisite is ON", Status.DONE);
			}else{
				report.updateTestLog("Select PreRequisite 'NO' CheckBox",  "PreRequisite is OFF", Status.DONE);
			}
		}catch(Exception e){			
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	
	
	
	public void selectContentSource(String strContentSource) throws Exception{
		try{
			selectFromDropDownByValue(selContentSource, strContentSource);					
			report.updateTestLog("Select value '"+ strContentSource +"' from Content Source drop down.",  "'"+ strContentSource +"' is selected", Status.DONE);
			common.verifyLoadingProcessMessageDisappear(30);	
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	public void selectModuleToEdit(String strModuleNameToEdit) throws Exception{
		try{
			selectFromDropDownByValue(selModuleToEdit, strModuleNameToEdit);
			onMouseClick(validateElementPresence("Select Module button", btnSelectModuleToEdit));
			//clickElement(btnSelectModuleToEdit);		
			report.updateTestLog("Select value '"+ strModuleNameToEdit +"' from Select Module drop down and click 'Select Module' Button to Edit the Module.",  "'"+ strModuleNameToEdit +"' is selected", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	
	public void clickUpdateButton() throws Exception{
		try{
			//clickElement(btnUpdate);	
			onMouseClick(validateElementPresence("Update button", btnUpdate));
			waitTime(1);
			report.updateTestLog("Click Update button",  "Update button is clicked", Status.DONE);			
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	public void clickAddPreceptorsButton_SkillsPreceptorsTab() throws Exception{
		try{
			//clickElement(btnAddPreceptors_SkillsPreceptorsTab);		
			onMouseClick(validateElementDisplay("SkillsPreceptors Tab", btnAddPreceptors_SkillsPreceptorsTab));
			report.updateTestLog("Click Add Preceptors button in Skills Preceptors tab",  "Add Preceptors button is clicked", Status.DONE);			
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	public void addPreceptors(String strTabName,String[] PreceptorsName) throws Exception{
		
		try{
			boolean blnTest = false;
			/*WebElement objTable = null;
			if(strTabName.trim().equalsIgnoreCase("Skills Preceptors")){				
				objTable = validateElementDisplay("SkillsPreceptors Table", tblSkillsPreceptors);
				if(verifyTextInWebTableByElement(objTable, "No records to display")){					
					clickAddPreceptorsButton_SkillsPreceptorsTab();	
					blnTest = true;
				}				
			}else if(strTabName.trim().equalsIgnoreCase("Knowledge Manager Preceptors")){
				//clickAddPreceptorsButton_KnowledgeManagerPreceptorsTab();
				//objTable = validateElementPresence("Knowledge Manager Preceptors Table", );
				if(verifyTextInWebTableByElement(objTable, "No records to display")){					
					clickAddPreceptorsButton_SkillsPreceptorsTab();	
					blnTest = true;
				}	
			}*/
			clickAddPreceptorsButton_SkillsPreceptorsTab();	
			blnTest = true;
			if(blnTest){
				if(validateElementPresence(frameManageModulePreceptors, getIframeLocatorByName(frameManageModulePreceptors))!= null){
					switchFrame(frameManageModulePreceptors);								
					if(PreceptorsName.length>0){
						for(int j= 0 ; j<PreceptorsName.length;j++){							
							selectCheckBoxPreceptorsByName(PreceptorsName[j]);
						}
					}
					clickAddSelectedUsersButton();				
					homePage.verifySuccessMessage("Your request has been processed successfully");
					//Verify the Selected users displayed in Skills Preceptors table
					
					//Close Frame and Control Back to TopFrame
					driver.switchTo().defaultContent();				
					closeFrame(frameManageModulePreceptors.toUpperCase());
					waitTime(1);
				}
			}else{
				report.updateTestLog("UNABLE TO PROCEED @ " + getCurrentMethodName(this.getClass().getName()),  "Preceptors Table does NOT exist", Status.DONE);
			}
							
						
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	public void clickAddToSavedItems() throws Exception{
		try{
			clickElement(btnAddToSavedItems);						
			report.updateTestLog("Click Add To Saved Items button",  "Add To Saved Items button is clicked", Status.DONE);
			common.verifyLoadingProcessMessageDisappear(30);
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	public void clickCreateNewModuleButton() throws Exception{
		try{
			clickElement(btnCreateNewModule);
			report.updateTestLog("Click Create Module button",  "Create New Module Button is clicked", Status.DONE);
			waitTime(2);
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	public void clickCreateModuleButton() throws Exception{
		try{
			//clickElement(btnCreateModule);
			onMouseClick(validateElementPresence(btnCreateModule, btnCreateModule));
			report.updateTestLog("Click Create Module button",  "Create Module Button is clicked", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	public void clickRemoveAllSavedItemsButton() throws Exception{
		try{
			clickElement(btnRemoveAllItems);			
			report.updateTestLog("Click Remove All Saved Items button",  "Remove All Saved Items button is clicked", Status.DONE);
			if(isDialogPresent()){
				actionOnAlert(waitForAlert(3), "ACCEPT");
				report.updateTestLog("Accept the Alert if displayed any",  "Alert is Accepted", Status.DONE);
			}
			common.verifyLoadingProcessMessageDisappear(30);			
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	
	public void clickAddSelectedUsersButton() throws Exception{
		try{
			//clickElement(btnAddSelectedUsers);		
			onMouseClick(validateElementPresence(btnAddSelectedUsers, btnAddSelectedUsers));
			report.updateTestLog("Click 'Add Selected Users' Button",  "Add Selected Users button is clicked", Status.DONE);
		}catch(Exception e){
			System.out.println("btnAddSelectedUsers - Check frame is displayed or not");
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	//Manage Elearning page methods
	public void selectModuleToAssignByDept(String strModuleNameToAssign) throws Exception{
		try{
			selectFromDropDownByValue(selDeptModules, strModuleNameToAssign);					
			report.updateTestLog("Select value '"+ strModuleNameToAssign +"' from Module to Assign by Department drop down.",  "'"+ strModuleNameToAssign +"' is selected", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	/**To click the Choose FACILITIES or DEPARTMENTS or GROUPS or INDIVIDUALS button based on Parameter strToAssign.
	 * @param strToAssign - FACILITIES or DEPARTMENTS or GROUPS or INDIVIDUALS
	 * @throws Exception
	 */
	public void clickChooseByAssigned(String strToAssign) throws Exception{
		try{
			if(strToAssign.trim().toUpperCase().equals("FACILITIES")){
				
			}else if(strToAssign.trim().toUpperCase().equals("DEPARTMENTS")){
				
			}else if(strToAssign.trim().toUpperCase().equals("GROUPS")){
				clickElement(btnChooseGroups);
			}else if(strToAssign.trim().toUpperCase().equals("INDIVIDUALS")){
				
			}				
			report.updateTestLog("Click 'Choose "+ strToAssign + "' Button",  " 'Choose "+ strToAssign + "' button is clicked", Status.DONE);
		}catch(Exception e){
			System.out.println("btnAddSelectedUsers - Check frame is displayed or not");
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	
	//Select the Groups you would like to have added:
	public boolean selectCheckBoxGroupsToAssign(String[] GroupNameCodeList) throws Exception{
		boolean result = false;
		try{
			WebElement objChkBox = null;
			String strXpath = "";
			if(GroupNameCodeList.length > 0){
				for(int i= 0; i<GroupNameCodeList.length ;i++){
					strXpath = "//input[@value='"+ GroupNameCodeList[i].trim() +"' and @type='checkbox']";
					objChkBox = validateElementPresence(GroupNameCodeList[i] + " CheckBox",strXpath );
					if(objChkBox!= null && objChkBox.isEnabled() && !objChkBox.isSelected()){
						objChkBox.click();
						if(objChkBox.isSelected()){
						result = true;
						report.updateTestLog("Select the Group you would like to have added:",  GroupNameCodeList[i].trim() +" Group is Selected.", Status.DONE);
						}						
					}
				}//for end				
			}else{
				report.updateTestLog("Select the Groups you would like to have added:",  "No Groups To add", Status.FAIL);
			}
		}catch(Exception e){			
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	
	public void clickOKButtonByAssigned(String strToAssign) throws Exception{
		try{
			if(strToAssign.trim().toUpperCase().equals("FACILITIES")){
				clickElement(btnOkSaveFacilities);
			}else if(strToAssign.trim().toUpperCase().equals("DEPARTMENTS")){
				clickElement(btnOkSaveDepartments);
			}else if(strToAssign.trim().toUpperCase().equals("GROUPS")){
				clickElement(btnOkSaveGroups);
			}else if(strToAssign.trim().toUpperCase().equals("INDIVIDUALS")){
				//clickElement(btnOkSaveIndividuals);
			}				
			report.updateTestLog("Click 'OK' Button to Save the '"+ strToAssign + "'",  "'OK' button is clicked", Status.DONE);
		}catch(Exception e){			
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	
	public void verifyAssignedItemAddedToModule(String strToAssign, String[] assignedItemList) throws Exception{
		try{
			
			WebElement objTable = null;
			if(strToAssign.trim().toUpperCase().equals("FACILITIES")){
				objTable = validateElementDisplay("tblSelectedFacilities", tblSelectedFacilities);
			}else if(strToAssign.trim().toUpperCase().equals("DEPARTMENTS")){
				objTable = validateElementDisplay("tblSelectedDepartments", tblSelectedDepartments);
			}else if(strToAssign.trim().toUpperCase().equals("GROUPS")){
				objTable = validateElementDisplay("tblSelectedGroups", tblSelectedGroups);
			}else if(strToAssign.trim().toUpperCase().equals("INDIVIDUALS")){
				objTable = validateElementDisplay("tblSelectedUsers", tblSelectedUsers);
			}	
			
			if(objTable!=null){
				if(assignedItemList.length >0){
					for(int i=0;i<assignedItemList.length;i++){
						if(verifyTextInWebTableByElement(objTable, assignedItemList[i])){
							report.updateTestLog("Verify the  '"+strToAssign +"' - '"+ assignedItemList[i] +"' is assigned",  "'"+ assignedItemList[i] +"' is assigned to Module.", Status.PASS);	
						}else{
							report.updateTestLog("Verify the  '"+strToAssign +"' - '"+ assignedItemList[i] +"' is assigned",  "'"+ assignedItemList[i] +"' is NOT assigned to Module.", Status.FAIL);
						}
					}//for end
				}else{
					report.updateTestLog("Verify the  '"+strToAssign +"' is assigned",  "ERROR: No Items passed to assign to Module.", Status.FAIL);
				}				
			}
			
		}catch(Exception e){			
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	public void clickAssignNowButtonByDept() throws Exception{
		try{
			clickElement(btnAssignNow_DeptModules);				
			report.updateTestLog("Click 'Assign Now' Button to Dept Module to assign.",  "'Assign Now' button is clicked", Status.DONE);
		}catch(Exception e){			
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
	}
	
	
	
}//class end