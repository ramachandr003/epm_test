package testscripts.INT_Smoke;



import org.junit.Test;


import supportlibraries.*;
import supportlibraries.RandomString.Mode;
import functionallibraries.*;

import com.cognizant.framework.*;

/**
 * Test for INt Skills Smoke testing
 * 
 * @author Ramachandr003
 */
public class TC_Smoke_Suite1 extends TestScript
{
	@Test
	public void runTC_Demo()
	{
		testParameters.setIterationMode(IterationOptions.RunOneIterationOnly);
		driveTestExecution();
	}
	
	@Override
	protected void executeTestcase()
	{
		EPM_LoginPage loginPage = new EPM_LoginPage(scriptHelper);
		EPM_HomePage homePage = new EPM_HomePage(scriptHelper);
		EPM_LearnersPage learnersPage = new EPM_LearnersPage(scriptHelper);
		EPM_SystemPage systemPage = new EPM_SystemPage(scriptHelper);				
		EPM_SkillsPage skillsPage = new EPM_SkillsPage(scriptHelper);
		EPM_eLearningPage eLearningPage = new EPM_eLearningPage(scriptHelper);
		Common common = new Common(scriptHelper);
	
		
		try{
			
			
			//Skill Edit Copy
			String strskillName = "Abdominal Binder";
			String strRandomString = RandomString.randomString(6, Mode.ALPHA);
			String strNewSkillName = "AAA Auto "+ strskillName + "-" + strRandomString;
			//String strNewSkillName ="Copy of Abdominal Binder-VEtnqL";			
			String[] arrSkillsName = {strNewSkillName};
			
			//Learner Creations
			String[] arrLearnerLastName = new String[1];	//Stores Maximum of 4 Learners
			

			
			String strRandomString2 ="";
			
			//Group Creation
			//String strGroupCodeName ="1234"; 
			String strGroupCodeName = "AutoGroupName"+strRandomString;
			String strGroupDesc = "AutoGroupDesc"+strRandomString;			
			String[] arrGroupCodeName = {strGroupCodeName};
			
			
			
			//String strNewModuleName = "0528 anna test";	
			String strNewModuleName = "AutoModuleSkills"+strRandomString;
			String strContentSource = "Mosby Skills";
			String[] arrPreceptors = {"Administrator, Mosby","Administrator, System","Administrator, Global"};
			
			
			//Skill Grade by Each learner
			/*String[] arrUnivGrade ={"Satisfactory" ,"Unsatisfactory" ,"Not Performed"};
			String[] arrOverallComments={"Smoke Test - Pass","Smoke Test - Fail","Smoke Test - InProgress"};
			String[] arrOverallGrade={"PASS", "FAIL", "IN-PROGRESS"};*/
			String[] arrUnivGrade ={"Not Performed"};
			String[] arrOverallComments={"Smoke Test - InProgress"};
			String[] arrOverallGrade={"IN-PROGRESS"};
		
			
			String strFacility = dataTable.getData("General_Data", "Facility");
			String strDepartment  = dataTable.getData("General_Data", "Department");
			String strEmailID = dataTable.getData("General_Data", "EmailID");
		
			report.addTestLogSubSection("Login into EPM applictaion");	
			loginPage.loginEPM();
			
			
			
			//******************************************************************************//
			//Copy a Skill
			report.addTestLogSubSection("Copy any existing Skill say - '"+ strskillName + "'");	
			homePage.NavToSubMenuandClick("Skills->Add/Edit Skills");			
			//if(common.verifySecondaryBrowserWindow("Skills Search : Mosby's Skills")){	
			if(switchWindowByTitle("Skills Search : Mosby's Skills")){
				skillsPage.clickCopyEditSkillLink(strskillName, "Active","COPY");
				skillsPage.editSkillsAndSave(strNewSkillName, "YES", "YES");				
				//Close Skill Window
				//common.closeSecondaryBrowserWindow("Skills Search : Mosby's Skills");	
				closeWindowByTitle("Skills Search : Mosby's Skills");
			}else{
				exitTestOnFail();
			}
			
			
			//*******************************************************************************//*
			
			//Create 4 New Learners
			for(int i= 0;i<arrLearnerLastName.length;i++){
				strRandomString2 = RandomString.randomString(6, Mode.ALPHA);
				String strLearnerID = "AutoLrnID" + strRandomString2;
				String strFirstName = "AutoLrnFN" + strRandomString2;
				String strLastName = "AutoLrnLN" + strRandomString2;
				report.addTestLogSubSection("Create a Learner : " + (i+1) + "        |              Learner ID:" + strLearnerID);	
				//Add Learners Last name to an Array for future use
				arrLearnerLastName[i] =	strLastName;
				homePage.NavToSubMenuandClick("Learners->Add a New Learner");			
				learnersPage.createNewLearner(strLearnerID,strFirstName,strLastName,strFacility,strDepartment,strEmailID);			
			}			
			
			//*********************************************************************************//*
			// Create Group and Add above 4 Learners
			
			report.addTestLogSubSection("Create a Group and Add 4 learners to Group - '"+ strGroupCodeName + "'");	
			homePage.NavToSubMenuandClick("System->Manage Groups");			
			systemPage.createNewGroup(strGroupCodeName, strGroupDesc);
			systemPage.addLearnersToGroup(strGroupCodeName, arrLearnerLastName);
		
			//*********************************************************************************//
			
			//Create a Module 
			report.addTestLogSubSection("Create a Module '"+ strNewModuleName + "' with Skill");
			homePage.NavToSubMenuandClick("eLearning->Create an Assignable Module");			
			eLearningPage.manageAssignableItemQueue(strContentSource,arrSkillsName);
			String strModuleNameToEdit = eLearningPage.createNewModule(strNewModuleName, strContentSource, arrSkillsName, false);		
			
			//Edit Module properties to Assign Preceptor and Checklist
			report.addTestLogSubSection("Edit a Module '"+ strNewModuleName + "' and assign  Preceptors & Checklist");
			homePage.NavToSubMenuandClick("eLearning->Edit Existing Modules");			
			eLearningPage.selectModuleToEdit(strModuleNameToEdit);
			homePage.clickOnTabByName("Assignable Items");
			eLearningPage.selectCheckBoxPreRequisite("YES");
			eLearningPage.clickUpdateButton();			
			homePage.clickOnTabByName("Skills Preceptors");
			eLearningPage.addPreceptors("Skills Preceptors", arrPreceptors);
			
			
			//***********************************************************************************************************//
			/*Step 1,05 Assign Training to Group 'Select eLearning > Assign Training'
			Verify Manage eLearning page opens 
			Step 2, Select Module created in previous step from Modules drop-down Select Choose Groups button Select Group previously created Select OK button ,
			Verify the selected Group  appears in the Group section 
			Step 3,Select Assign Now button ,
			Verify [Module Name] module was successfully assigned */
			
			report.addTestLogSubSection("Assign the Module to a Group which has 4 learners");
			homePage.NavToSubMenuandClick("eLearning->Assign Training");	
			eLearningPage.selectModuleToAssignByDept(strNewModuleName);
			eLearningPage.clickChooseByAssigned("GROUPS");
			eLearningPage.selectCheckBoxGroupsToAssign(arrGroupCodeName);
			eLearningPage.clickOKButtonByAssigned("GROUPS");
			eLearningPage.verifyAssignedItemAddedToModule("GROUPS", arrGroupCodeName);
			eLearningPage.clickAssignNowButtonByDept();
			homePage.verifySuccessMessage("The "+strNewModuleName+" module was successfully assigned");
			
			
			//*************************************************************************************************************//
			/*Step 1,06 Grade Checklist on  Select Skills > Grade Checklist 
			 * Select Module as Filter [Default] Select Module created in previous step 
			 * Select Show Results 
			 * Select the Skill created for the module/test 
			 * Select Learner #1 
			-Verify Record Evaluation page opens -Verify Module name and skill are correct ,
			
			Step 2,Select Universal Grade "S" [Satisfactory] Scroll to bottom of page Enter Overall Comments "Smoke Test - Pass" Select Grade = Pass Select Save ,
			Verify "Your request has been processed successfully." ,
			//********Your request has been processed successfully.
			
			Step 3,Scroll to bottom of page Select Back button ,
			-Verify page returns to "Grade Learners By Skill" -Verify Learner graded is displayed as "Pass" with comments  
			-Verify Preceptor name appears in Preceptors field for Learner graded -Verify All 4 Learners appear for selected Module ,
			
			Step 4,Select Learner #2 ,
			-Verify Record Evaluation page opens -Verify Module name and skill are correct ,
			
			Step 5,Select Universal Grade "U" [Unsatisfactory] Scroll to bottom of page Enter Overall Comments "Smoke Test - Fail" Select Grade = Fail Select Save ,
			Verify "Your request has been processed successfully." ,
			
			Step 6,Scroll to bottom of page Select Back button ,
			-Verify page returns to "Grade Learners By Skill" -Verify Learner graded is displayed as "Failed" with comments  
			-Verify Preceptor name appears in Preceptors field for Learner graded -Verify All 4 Learners appear for selected Module ,
			
			Step 7,Select Learner #3 ,
			-Verify Record Evaluation page opens -Verify Module name and skill are correct ,
			
			Step 8,Select Universal Grade "NP" [Not Performed] Scroll to bottom of page Enter Overall Comments "Smoke Test - In-Progress" Select Grade = In-Progress Select Save ,
			Verify popup message "You are about to save your entries for this checklist.  
			You may return to complete the checklist later. 
			Note that your learners cannot review in-progress checklist.  Check OK button to proceed." ,
			//************* Alert accept and verify text of Alert
			Step 9,Select OK ,
			Verify "Your request has been processed successfully." ,
			
			Step 10,Scroll to bottom of page Select Back button ,
			-Verify page returns to "Grade Learners By Skill" -Verify Learner graded is displayed as "In-Progress" with comments  -Verify Preceptor name appears in Preceptors field for Learner graded -Verify All 4 Learners appear for selected Module ,
			Step 11,Close Grade Learners By Skill window*/ 
			
			
			report.addTestLogSubSection("Grade the Checklists");
			homePage.NavToSubMenuandClick("Skills->Grade Checklists");	
			//if(common.verifySecondaryBrowserWindow("View Checklist : Mosby's Skills")){	
			if(switchWindowByTitle("View Checklist : Mosby's Skills")){
				common.verifyPageNameByH1TagHeader("View Checklist");
				skillsPage.selectFilterToViewCheckListBy("Module Name");
				skillsPage.selectModuleToViewCheckList(strNewModuleName);
				skillsPage.clickSkillNameForGrade(arrSkillsName[0]);
				for(int j=0;j<arrOverallGrade.length;j++){
					String strLearnerForGrade = arrLearnerLastName[j];
					skillsPage.clickLearnerNameForGrade(strLearnerForGrade);
					skillsPage.gradeChecklistInRecordEvaluationPage(strLearnerForGrade, strNewModuleName, arrSkillsName[0],arrPreceptors[0],arrUnivGrade[j], arrOverallComments[j], arrOverallGrade[j]);
				}
				//Close Skill Window
				closeWindowByTitle("Grade Learners By Skill");
				//common.closeSecondaryBrowserWindow("Grade Learners By Skill");				
			}else{
				exitTestOnFail();
			}
		
			//**************************************************************************************************************//
			driver.switchTo().defaultContent();
			homePage.clickLogoutLink();		
			loginPage.verifyLoginPage();			
		}
		catch (Exception e)
		{	
			System.out.println("Exception: "+e.getMessage());
			homePage.ePM_Logout_OnException();			
			report.updateTestLog("SCRIPT FAILED: Unable to Proceed", " Error=" + e.getMessage(), Status.FAIL);
		}			
	}	
}