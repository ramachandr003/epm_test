package functionallibraries;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import supportlibraries.*;

import com.cognizant.framework.*;

/**
 * MNS_HomePage class
 * 
 * @author Ramachandr003
 */
public class MNS_HomePage extends MasterPage
{
	
	
	
	
	
	// UI Map object definitions
	
	
	public String linkLogout = "link=Logout";
	public String linkMyAccount = "link=MyAccount";
	public String linkAdminTab = "link=Admin";
	public String btnCreateSkill = "ctl00_btnCreateSkill";
	public String pnlAdminSkillsMgmt = "ctl00_pnlAdminSkillsManagement";
	public String pnlAdminAccessMgmt = "ctl00_pnlAdminAccessManagement";
	public String pnlAdminUsageReport = "ctl00_pnlAdminUsageReport";
	
	//for smoke test script
	public String txtSearchSkills = "ctl00_txtUcdSearch";
	public String btnGo_SearchSkill = "ctl00_btnSearch";
	public String linkSearchMNC = "ctl00_hypSearchMnc";
	public String linkSkillsTab = "link=Skills";
	public String linkPatientEducationTab = "link=Patient Education";
	
	
	
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
	public MNS_HomePage(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
	}

	@Override
	protected void initializeUIMap()
	{

	}
	


	public void verifyLoginFuncAndHome() throws Exception {
		
	
		
		try{
			if(validateElementPresence("Logout Link", linkLogout) != null && 
					validateElementPresence("Logout Link", linkMyAccount) != null)
			{				
				report.updateTestLog("Verify the MNS Login functionality", 
						"MNS Home page is displayed with Logout and MyAccount link...", Status.PASS);
			}
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "verifyLoginFuncAndHome" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
		
		
		/*try{
			if(validateElementPresence("Logout Link", "link=Logout") != null && validateElementPresence("Logout Link", "link=MyAccount") != null)
			{				
				report.updateTestLog("Verify the MNS Login functionality", "MNS Home page is displayed with Logout and MyAccount link...", Status.PASS);
			}
			else
			{
				report.updateTestLog("Verify the MNS Login functionality", "MNS home page is NOT displayed..", Status.FAIL);	
			}

		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "verifyLoginFuncAndHome" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}*/
	}
	
	public void clickLogoutLink(){
		try{
			clickElement(linkLogout);
			report.updateTestLog("Click Logout Link",  "Logout link is clicked", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickLogoutLink" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	public void clickMyAccountLink(){
		try{
			clickElement(linkMyAccount);
			report.updateTestLog("Click MyAccount Link",  "MyAccount link is clicked", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickMyAccountLink" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	public void clickAdminTabLink(){
		try{
			clickElement(linkAdminTab);
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
		element = validateElementPresence("Create a Skill button", btnCreateSkill);
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
		element = validateElementPresence("Skills Management Section", pnlAdminSkillsMgmt);
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
		element = validateElementPresence("Admin Access Management Section",pnlAdminAccessMgmt );
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
		element = validateElementPresence("Admin Usage Report Section", pnlAdminUsageReport);
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

	
	//Smoke test scripts
	public void enterSkillNameToSearch(String strSkillName) throws Exception{				
			typeOnElement(txtSearchSkills, strSkillName);
			report.updateTestLog("Enter Skill Name",  " ' "+ strSkillName + " ' " + " is entered in the Search Skill text field", Status.DONE);		
	}
	public void clickGoButtonToSearchSkill() throws Exception{				
		clickElement(btnGo_SearchSkill);
		report.updateTestLog("Click Go Button to Search a Skill.",  " Go Button is clicked", Status.DONE);	
	}
	public void clickSearchMNCLink() throws Exception{				
		clickElement(linkSearchMNC);
		report.updateTestLog("Click 'Search Mosby Nursing Consult' link",  "Search MNC link is clicked", Status.DONE);	
	}
	
	public void clickPatientEducationTabLink() throws Exception{				
		clickElement(linkPatientEducationTab);
		report.updateTestLog("Click 'PatientEducation tab' link",  "PatientEducation tab is selected", Status.DONE);	
	}
	public boolean verifySkillAndPETabInHomePage() throws Exception{		
		boolean result = false;	
		if(validateElementPresence("Skill Tab - ", linkSkillsTab)!=null &&
				validateElementPresence("PatientEducation Tab - ", linkPatientEducationTab)!=null){
			report.updateTestLog("Verify the Presence of Skills and Patient Education tab in Home page",  "Both Skills and PE tab are displayed", Status.PASS);	
			result = true;	
		}else{
			report.updateTestLog("Verify the Presence of Skills and Patient Education tab in Home page",  "Both Skills and PE tab are NOT displayed", Status.FAIL);
			result = false;	
		}    
		return result;
	}	
	
}