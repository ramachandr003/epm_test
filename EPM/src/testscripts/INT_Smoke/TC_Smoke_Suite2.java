package testscripts.INT_Smoke;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.ClickElement;

import supportlibraries.*;
import functionallibraries.*;

import com.cognizant.framework.*;

/**
 * Test for INt Skills Smoke testing
 * 
 * @author Ramachandr003
 */
public class TC_Smoke_Suite2 extends TestScript
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
		EPM_SkillsPage skillsPage = new EPM_SkillsPage(scriptHelper);
		MNS_HomePage mnsHomePage =  new MNS_HomePage(scriptHelper);
		MNS_SkillsPage mnsSkillsPage =  new MNS_SkillsPage(scriptHelper);
		MNS_PatientEducationPage mnsPatientEducationPage =  new MNS_PatientEducationPage(scriptHelper);		
		Common common = new Common(scriptHelper);
		
		
		try{
			String userName = dataTable.getData("General_Data", "User_Name");
			String passWord = dataTable.getData("General_Data", "Password");
			String strSkillName = dataTable.getData("General_Data", "SkillName");
			strSkillName = "Copy of Abdominal Binder-Copy skill1403019453323"; //SkillName
			String strAlertTextToVerify = "";

			report.addTestLogSubSection("Login into EPM applictaion");	
			loginPage.verifyLoginPage();
			loginPage.enterUserName(userName);
			loginPage.enterPassword(passWord);
			loginPage.clickLoginButton();
			homePage.verifyLoginFuncAndHome();
			
			//*****Smoke Test : Mosby Skills Reference*************//
			/*Step1,Select Mosby's Skills Reference icon located on right corner of page ,
			Verify Mosby's Skills window opens  
			Verify only Skills & Patient Education tabs appear with Skills opened by default*/
			homePage.clickMosbySkillReferenceLink();
			if(switchWindowByTitle("Home : Mosby's Skills")){
				mnsHomePage.verifySkillAndPETabInHomePage();
			}else{
				exitTestOnFail();
			}
			/*Step2,Enter name of skill copied in previous step into Search field Select Go ,
			-Verify Skills Search page opens with results 
			-Verify skill copied in previous step appears as link*/
			mnsHomePage.enterSkillNameToSearch(strSkillName);
			mnsHomePage.clickGoButtonToSearchSkill();
			
			if(mnsSkillsPage.verifySkillInSearchResultsByName(strSkillName)){
				report.updateTestLog("Verify the Presence of Skill "+strSkillName + "in Search Skill Results.",  "Skill Name is displayed", Status.PASS);
			}else{
				report.updateTestLog("Verify the Presence of Skill "+strSkillName + "in Search Skill Results.",  "Skill Name is NOT displayed", Status.FAIL);
				common.closeSecondaryBrowserWindow("Skills Search : Mosby's Skills");
				exitTestOnFail();
			}
			
			
			/*
			Step3,Select Skill Link ,
			Verify Skills Content page opens for selected Skill
			Verify Quick Sheet is selected and opened by default*/ 
			
			mnsSkillsPage.clickSkillInSkillsSearch(strSkillName);			
			if(mnsSkillsPage.verifySkillContentPageDisplayed(strSkillName) && 
					mnsSkillsPage.verifySkillContentSelectedSubTab("QUICK SHEET")){
				report.updateTestLog("Verify Skills Content page opens for selected Skill "+strSkillName + ". Verify Quick Sheet is selected and opened by default",  "Skill Content page is displayed for the selected Skill and Quick Sheet tab is displayed by Default.", Status.PASS);
			}else{
				report.updateTestLog("Verify Skills Content page opens for selected Skill "+strSkillName + ". Verify Quick Sheet is selected and opened by default",  "Skill Content page is NOT displayed for the selected Skill and/or  Quick Sheet tab is NOT displayed by Default.", Status.FAIL);
				common.closeSecondaryBrowserWindow("Skills Search : Mosby's Skills");				
			}
			
			/*Step4,Select Demo tab/icon ,
			Verify Demos page opens for same selected skill 
			Step7,Select Demo link ,
			Verify pop up opens to demo and demo plays ,
			Step8,Close Demo window */
			
			mnsSkillsPage.selectSkillContentSubTab("DEMOS");			
			if(mnsSkillsPage.verifySkillContentTabSelection("DEMOS", strAlertTextToVerify)){
				report.updateTestLog("Select Demo tab/icon",  "Demos page is displayed for same selected skill", Status.PASS);
				common.switchFrame("skill content");
				mnsSkillsPage.clickAnimatedContentLinkDemo();
				if(mnsSkillsPage.verifyAnimatedPlayerOpenInNewWindow()){
					report.updateTestLog("Verify pop up opens to demo and demo plays",  "Animated Player Demo opened in new window and closed.", Status.PASS);				
				}else{
					report.updateTestLog("Verify pop up opens to demo and demo plays",  "Animated Player Demo NOT opened in new window and closed.", Status.FAIL);
				}
			}else{
				report.updateTestLog("Select Demo tab/icon",  "Demos page is NOT displayed.", Status.FAIL);
			}
			
			
			/*Step5,Select Extended Text tab/icon ,
			Verify Extended Text page opens for same selected skill ,*/
			mnsSkillsPage.selectSkillContentSubTab("EXTENDED TEXT");			
			if(mnsSkillsPage.verifySkillContentTabSelection("EXTENDED TEXT", strAlertTextToVerify)){
				report.updateTestLog("Select Extended Text tab/icon",  "Extended Text page is displayed for same selected skill", Status.PASS);
			}else{
				report.updateTestLog("Select Extended Text tab/icon",  "Extended Text page is NOT displayed.", Status.FAIL);
			}
			
			/*Step6,Select Supplies tab/icon ,
			Verify Supplies page opens for same selected skill ,*/
			mnsSkillsPage.selectSkillContentSubTab("SUPPLIES");			
			if(mnsSkillsPage.verifySkillContentTabSelection("SUPPLIES", strAlertTextToVerify)){
				report.updateTestLog("Select Supplies tab/icon",  "Supplies page is displayed for same selected skill", Status.PASS);
			}else{
				report.updateTestLog("Select Supplies tab/icon",  "Supplies page is NOT displayed.", Status.FAIL);
			}
			
			/*Select Test tab/icon Scroll to bottom of page ,
			Verify Note in red font "Note: When you complete this test, the score will be recorded as Self-Assigned and will not count toward any Task completion requirements." appears above the "End of Test" text ,
			Step11,Select Complete the Test button ,
			Verify red "X" appears next to each test question ,*/
			
			mnsSkillsPage.selectSkillContentSubTab("TEST");				
			if(mnsSkillsPage.verifySkillContentTabSelection("TEST", strAlertTextToVerify)){
				report.updateTestLog("Select Test tab/icon",  "Test page is displayed for same selected skill", Status.PASS);
				/*common.switchFrame("skill content");
				if(mnsSkillsPage.verifyNoteInRedFontInTestSubTab()){
					report.updateTestLog("Verify Note in red font 'Note: When you complete this test, the score will be recorded as Self-Assigned and will not count toward any Task completion requirements.' appears above the 'End of Test' text",  "Red font Text displayed as expected", Status.PASS);
					mnsSkillsPage.clickCompleteTestButton();
					if(mnsSkillsPage.verifyRedXMarkAppearToEachQuestion()){
						report.updateTestLog("Verify red 'X' appears next to each test question",  "Red X mark displayed for Wrong answers as expected.", Status.PASS);
					}else{
						report.updateTestLog("Verify red 'X' appears next to each test question",  "Red X mark NOT displayed for Wrong answers as expected.", Status.FAIL);
					}
				}else{
					report.updateTestLog("Verify Note in red font 'Note: When you complete this test, the score will be recorded as Self-Assigned and will not count toward any Task completion requirements.' appears above the 'End of Test' text",  "Red font Text NOT displayed as expected", Status.FAIL);
				}*/
			}else{
				report.updateTestLog("Select Test tab/icon",  "Test page is NOT displayed.", Status.FAIL);
			}
			
			/*Step12,Select Checklist tab/icon ,
			Verify Checklist page opens Verify Checklist is displayed ,*/
			
			mnsSkillsPage.selectSkillContentSubTab("CHECKLIST");			
			if(mnsSkillsPage.verifySkillContentTabSelection("CHECKLIST", strAlertTextToVerify)){
				report.updateTestLog("Select Checklist tab/icon",  "Checklist page is displayed for same selected skill", Status.PASS);
			}else{
				report.updateTestLog("Select Checklist tab/icon",  "Checklist page is NOT displayed.", Status.FAIL);
			}
		
			
			
			/*Select Illustrations tab/icon ,
			Verify Illustrations page opens Verify Illustration images are displayed ,
			Step9,Select the first Illustration image ,
			Verify Illustration popup opens and displays the image ,
			Step10,Close Illustration popup*/
			
			mnsSkillsPage.selectSkillContentSubTab("ILLUSTRATIONS");			
			if(mnsSkillsPage.verifySkillContentTabSelection("ILLUSTRATIONS", strAlertTextToVerify)){
				report.updateTestLog("Select Illustrations tab/icon",  "Illustrations page is displayed for same selected skill", Status.PASS);
				common.switchFrame("skill content");
				mnsSkillsPage.clickAnyIllustarationImage();
				if(mnsSkillsPage.verifyIllustrationImageOpenInNewWindow()){					
					report.updateTestLog("Verify Illustration popup opens and displays the image",  "Illustration Image opened in new window and closed.", Status.PASS);
				}else{
					report.updateTestLog("Verify Illustration popup opens and displays the image",  "Illustration Image NOT opened in new window and closed.", Status.FAIL);
				}
			}else{
				report.updateTestLog("Select Illustrations tab/icon",  "Illustrations page is NOT displayed.", Status.FAIL);
			}
			
			/*Step13,Select Print link located in upper right corner ,
			Verify Print window opens Verify Print Manager opens ,
			Step14,Select Cancel for Print Manager 
			Close Print Manage window*/
			
			/*Select Search Mosby's Nursing Consult link ,
			Verify new browser window opens to Mosby's Nursing Consult  
			Verify Mosby's Nursing Consult Home page is displayed ,
			Step15,Close Mosby's Nursing Consult browser window ---*/
			mnsHomePage.clickSearchMNCLink();
			if(common.verifySecondaryBrowserWindow("Mosby's Nursing Consult: Home Page")){				
				closeWindowByTitle("Mosby's Nursing Consult: Home Page");				
			}
			
			
			/*Select Patient Education tab ,
			Verify Patient Education Home page opens ,*/
			mnsHomePage.clickPatientEducationTabLink();
			mnsPatientEducationPage.verifyPatientEducationHomePage();
			
			/*Step16,Select any link under the Condition Treatment tab ,
			Verify Patient Education Search page opens ,*/
			mnsPatientEducationPage.selectPatientEducationSubTabByName("CONDITION TREATMENT");
			mnsPatientEducationPage.clickConditionTreatmentLinkBySpecialty("");
			mnsPatientEducationPage.verifyPatientEducationSearchPage();
			
			/*Step17,Select any Patient Education Title link ,
			Verify selected Patient Education page opens ,
			****Step18,Select Add to Favorite Patient Education link ,*****
			****Verify "Your request has been processed successfully."*****
			Step19,Select Print link Verify Print popup displays Select Cancel ,
			Verify Print popup closes ,*/
			mnsPatientEducationPage.clickTitleInPatientEducationSearchResultsTableByTitle("");
			mnsPatientEducationPage.verifyPatientEducationDocumentContentPage();
			
			//Click Print Icon
			//******* only for IE browser**********************//
			mnsPatientEducationPage.clickPrintIconInPatientEducationPage();
			
			
			/*Step20,Select Drug tab Select "All Drugs" ,
			Verify "Browse by Drugs: "All Drugs" page opens Verify list of all drugs appears in Alphabetic order ,
			Step21,Select any Drug title link 
			Verify selected Drug Patient Education page opens 
			Select Add to Favorite Patient Education link ,
			Verify "Your request has been processed successfully." ,*/			
			mnsPatientEducationPage.selectPatientEducationSubTabByName("DRUG");
			mnsPatientEducationPage.clickAllDrugsLink();
			mnsPatientEducationPage.verifyPatientEducationSearchPage();
			mnsPatientEducationPage.clickTitleInPatientEducationSearchResultsTableByTitle("");
			mnsPatientEducationPage.verifyPatientEducationDocumentContentPage();
			
			
			
			/*Step22,Select ED Discharge tab Select All ED Discharge link ,
			Verify "Browse by ED Discharge: "All ED Discharge" opens with list of links Verify list of all ED discharge titles appears in Alphabetic order ,
			Step23,Select any Title link 
			Verify selected Patient Education opens ,Close Mosby/s Skills window */
			
			mnsPatientEducationPage.selectPatientEducationSubTabByName("ED DISCHARGE");
			mnsPatientEducationPage.clickAllEDDischargeLink();
			mnsPatientEducationPage.verifyPatientEducationSearchPage();
			mnsPatientEducationPage.clickTitleInPatientEducationSearchResultsTableByTitle("");
			mnsPatientEducationPage.verifyPatientEducationDocumentContentPage();
			closeWindowByTitle("View Patient Education : Mosby's Skills");	
			
			/*Step 23a, Select My Account Link ,
			Verify My Profile page opens ,
			Step24,Select Resource Center link ,
			Verify new browser window opens to <http://elsevierresources.com/> ,
			Step25,Close new browser window Select Help link ,
			Verify new browser window opens to <http://content.elsevierperformancemanager.com/helplinks/EPM_Admin_Help/elsevier_performance_manager_admin_help.htm> ,

			Step26,Close new browser window 
			Select "Other Products:" link Mosby's Nursing Consult link ,
			Verify new browser window opens to Mosby's Nursing Consult  ,

			Step27,Close new browser window 
			Select Logout link Verify login page displays 
			Select Forgot Password? Link ,
			Verify Password Assistance page opens ,
			Step28,Select Back to Login link 
			Verify login page displays 
			Select Forgot Password? Link 
			Verify Password Assistance page opens 
			Enter text into First and Last Name fields Enter 4 digit numbers into SSN field 
			Select Submit button ,
			Verify message in red font: "The information you entered does not match a learner profile we can provide an ID for. Please contact your Training Administrator or Help Desk for further assistance." ,

			Step29,Select Back to Login link ,
			Verify login page displays ,
			Step 30, Close the Browser*/
			
			
			
		
			homePage.clickLogoutLink();		
			loginPage.verifyLoginPage();
			
		}
		catch (Exception e)
		{	
			homePage.ePM_Logout_OnException();
			System.out.println("Exception: "+e.getMessage());
			report.updateTestLog("SCRIPT FAILED: Unable to Proceed", " Error=" + e.getMessage(), Status.FAIL);
		}
	}	
}