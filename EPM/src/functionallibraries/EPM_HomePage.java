package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import supportlibraries.*;

import com.cognizant.framework.*;

/**
 * SignOnPage class
 * 
 * @author Cognizant
 */
public class EPM_HomePage extends MasterPage
{
	
	Common common = new Common(scriptHelper);	
	
	
	
	//Object Locator Values
		public String linkLogout = "HeaderDefaultCtl1_LoginStatus1";
		public String linkMyAccount= "//*/a[@title='MyAccount']";////*[@id="tblLogged"]/tbody/tr[1]/td/a[2]
		public String linkMosbySkillReference = "HeaderDefaultCtl1_lnkReferrer";
		//not tested below
		public String textWelcome= "HeaderDefaultCtl1_lblWelcome";
		public String linkResCenter  = "HeaderDefaultCtl1_lnkResourceCenter";
		public String linkHelp  = "lnkHelp";
		public String linkMNC  = "//a[text()='Mosby's Nursing Consult']";
		
		public String btnAcceptWarnings = "btnContinue";
		//mMenu - Main Menu
		
		public String mMenuTab  = "//div[@id='ctl00_ctl00_ContentTopMenu_MainMenu1_RadMenu1']";
		public String tblSkills  = "//*[@id='ctl00_ContentMain_ElsevierGrid1_RadGrid1_ctl00']";
		//Success Message
		public String msgRequestSuccess ="//*/div[@id='infoMessagesDisplay']/p";
		public String msgRequestSuccessID ="infoMessagesDisplay";
		//*****************************TEXT to verify in DOM******************************/
		public String successMessageText ="Your request has been processed successfully";
		
		
		
		/*System.out.println(driver.getCurrentUrl());
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
			
		}
		System.out.println(driver.getCurrentUrl());
		//homePage.VerifyMsgRequestSuccess();
		System.out.println(driver.getCurrentUrl());*/
		
		
		
		

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
	public EPM_HomePage(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
	}

	@Override
	protected void initializeUIMap()
	{

	}
	
	
	
	/* This method should be used only when there is need of Sub menu navigation and 
	 * maximum of 3 sub level including the main Menu
	 * Example: Classes & Events->Manage Classes & Events->Create & Edit Course Templates
	 * @strSubMenuPath - Path to navigate thro Sub menus with Delimiter "->"
	 */
	
	public void NavToSubMenuandClick(String strSubMenuPath) throws Exception {
		
		String strMainMenu = "";
		String strSubMenuL1 = "";
		String strSubMenuL2 = "";
		String[] arrMenuPath; 
		int intLCnt = 0;
		String xMenupath = "";
		WebElement subMenuL1 = null;
		WebElement subMenuL2 = null;
		WebElement objMenu = null;
		int i = 0;
		try{
			if(strSubMenuPath!= "" ){  
				//&& strSubMenuPath.contains("->")
				arrMenuPath =strSubMenuPath.split("->");
				intLCnt = arrMenuPath.length;	
				
				
				strMainMenu = mMenuTab + "/ul/*/a/span[text()='" +arrMenuPath[0].trim()+"']";				
				if(validateElementPresence(strSubMenuPath , strMainMenu )!=null){				
					for (i = 1; i <= intLCnt ; i++){
						objMenu = null;
						if(i == 1){
							xMenupath = mMenuTab + "/ul/*/a/span[text()='" +arrMenuPath[0].trim()+"']";
							//objMenu = isExist(By.xpath(xMenupath));
							objMenu = driver.findElement(By.xpath(xMenupath));
							//onMouseOver(validateElementPresence(strSubMenuPath , xMenupath ));
							if(objMenu != null)								
							onMouseOver(objMenu);	
							//onMouseClickUsingJScript(objMenu);		
							
						}else if(i == 2){
							xMenupath = mMenuTab + "/ul/*/a/span[text()='" +arrMenuPath[0].trim()+"']/../following-sibling::div/ul/*/a/span[text()='" + arrMenuPath[1].trim() + "']";
							objMenu = driver.findElement(By.xpath(xMenupath));
							if(objMenu != null)							
							highlightElement(objMenu);
								onMouseOver(objMenu);	
								//onMouseClickUsingJScript(objMenu);
								
							//onMouseOver(validateElementPresence(strSubMenuPath , xMenupath ));
						}else if(i == 3){						
							xMenupath = mMenuTab + "/ul/*/a/span[text()='" +arrMenuPath[0].trim()+"']/../following-sibling::div/ul/*/a/span[text()='" + arrMenuPath[1].trim() + "']/../following-sibling::div/ul/*/a/span[text()='" + arrMenuPath[2].trim() + "']";
							objMenu = driver.findElement(By.xpath(xMenupath));
							if(objMenu != null)		
							
							onMouseOver(objMenu);
							//onMouseOver(validateElementPresence(strSubMenuPath , xMenupath ));
						}
						if(i==intLCnt){
							//clickElement(xMenupath);
							if(System.getProperty("browser").equalsIgnoreCase("iexplore")){
								waitTime(1);
								onMouseClickUsingJScript(objMenu);	
								waitTime(2);
							}else{
								onMouseClick(objMenu);
							}
							report.updateTestLog("Navigate and click the " + strSubMenuPath + " menu path Link",  " '"+ arrMenuPath[intLCnt-1] + "' link is clicked", Status.DONE);
						}//if
					}//for
				}//if
				
				
		
			}//if
			
		
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "NavToSubMenuandClick" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}//catch
		
		
	
	}
	
	public void clickOnTabByName(String strTabName) throws Exception {		
		try{			
			String tabSelectByName = "//descendant::span[text()='"+ strTabName +"']";		
			WebElement objTab = validateElementPresence(strTabName + " TAB", tabSelectByName);
			highlightElement(objTab);			
			if(objTab!=null && objTab.isEnabled()){			
				onMouseClick(objTab);
				//objTab.click();
				report.updateTestLog("Click On Tab - " + strTabName ,  "' "+ strTabName +"' tab is selected.", Status.DONE);
			}			
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void verifySuccessMessage(String strMessageText) throws Exception {		
		try{			
		
			WebElement objMsg = validateElementDisplay(strMessageText, msgRequestSuccess);
			
			if(objMsg != null && objMsg.getText().toLowerCase().contains(strMessageText.trim().toLowerCase())){			
				report.updateTestLog("Verify the Presence of ' "+ strMessageText +"'.",  "' "+ strMessageText +"' message is displayed", Status.PASS);
			}else{
				report.updateTestLog("Verify the Presence of ' "+ strMessageText +"'.",  "' "+ strMessageText +"' message is NOT displayed", Status.FAIL);
			}
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	

	
	public void verifyLoginFuncAndHome() throws Exception {		
		try{			
			//Not for Iexplore
			if(!System.getProperty("browser").equalsIgnoreCase("iexplore")){
				
				if(isExist(By.id(btnAcceptWarnings), 10)!=null){
					isExist(By.id(btnAcceptWarnings),10).click();
				}
			}			
			
			if(validateElementPresence("Logout Link", linkLogout) != null && 
					validateElementPresence("My Account Link", linkMyAccount) != null){				
				report.updateTestLog("Verify the EPM Login functionality", 
						"EPM Home page is displayed with Logout and MyAccount link...", Status.PASS);
			}
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "verifyLoginFuncAndHome" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public void clickLogoutLink(){
		try{
			
			//Click Logout link
			//clickElement(linkLogout);	
			onMouseClick(validateElementPresence(linkLogout, linkLogout));
			report.updateTestLog("Click Logout Link",  "Logout link is clicked", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickLogoutLink" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	
	public void ePM_Logout_OnException(){
		try{
			System.out.println("**************************** ERROR ***********************************");
			//Below Methods for Error Handling
			closeFrame("Error Handling");
			closeAlertAndReport();			
			//Close Secondary browser if any
			common.closeSecondaryBrowserWindow("");
			//Click Logout link
			clickLogoutLink();			
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
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
	
	public void clickMosbySkillReferenceLink() throws Exception{		
			onMouseClick(validateElementPresence("MosbySkillReference", linkMosbySkillReference));
			report.updateTestLog("Select Mosby's Skills Reference icon located on right corner of page",  "Icon is Clicked", Status.DONE);			
	}

}