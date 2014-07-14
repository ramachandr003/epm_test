package functionallibraries;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import supportlibraries.*;

import com.cognizant.framework.*;

/**
 * EPM LoginPage class
 * 
 * @author Ramesh
 */
public class EPM_LoginPage extends MasterPage
{
	
	
	EPM_HomePage homePage = new EPM_HomePage(scriptHelper);
	//Object Locator Values
	public String txtUserName = "ctl00_ContentMain_ctl00_Login1_UserName";
	public String txtPassWord= "ctl00_ContentMain_ctl00_Login1_Password";
	public String btnLogin = "ctl00_ContentMain_ctl00_Login1_LoginButton";
	public String linkForgetPwd  = "ctl00_ContentMain_ctl00_lnkForget";
	
	//LSU Environment Login fields
	public String txtFirstName = "ctl00_ContentMain_ctl00_txtFirstName";
	public String txtLastName= "ctl00_ContentMain_ctl00_txtLastName";
	public String txtSSN = "ctl00_ContentMain_ctl00_txtSSN4";
	public String btnLoginLSU  = "ctl00_ContentMain_ctl00_LoginButton";

	
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
	public EPM_LoginPage(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
	}

	@Override
	protected void initializeUIMap()
	{

	}
	
	
	
	public void verifyLoginPage() throws Exception{
		try{
			if(getLoginFieldsCount() == 3){
				verifyLoginPageWith3TextFields();
			}else{
				verifyLoginPageWith2TextFields();
			}
		}catch(Exception e){
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
		}
	}
	
	public int getLoginFieldsCount(){
		try{
			return driver.findElements(By.xpath("//input[@type='text']")).size();			
		}catch(Exception e){
			//Dont want to fail the test. Assuming most of the times we are testing with UserID and Password login
			//so Return 2 in case of any exception
			return 2;
		}
	}
	
	public void loginEPM() throws Exception{
		try{			
			if(getLoginFieldsCount() == 3){				
				String firstName = dataTable.getData("General_Data", "FirstName");
				String lastName = dataTable.getData("General_Data", "LastName");
				String ssn4digits = dataTable.getData("General_Data", "SSN");
				verifyLoginPageWith3TextFields();
				typeOnElement(txtFirstName, firstName);
				typeOnElement(txtLastName, lastName);
				typeOnElement(txtSSN, ssn4digits);
				clickElement(btnLoginLSU);	
				report.updateTestLog("Login with FirstName '"+ firstName +"', LastName '"+ lastName +"', SSN '"+ ssn4digits +"' fields and Submit login.", "Login fields are entered and Login Submitted.", Status.DONE);
				homePage.verifyLoginFuncAndHome();
			}else{
				String userName = dataTable.getData("General_Data", "UserName");
				String passWord = dataTable.getData("General_Data", "Password");
				verifyLoginPageWith2TextFields();
				enterUserName(userName);
				enterPassword(passWord);
				clickLoginButton();
				homePage.verifyLoginFuncAndHome();
			}		
		}catch(Exception e){
			report.updateTestLog("Error occured. LOGIN FAIL",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			exitTestOnFail();
		}
	}
	
	public void verifyLoginPageWith3TextFields() throws Exception{		
		if(validateElementPresence("First name",txtFirstName) != null && validateElementPresence("Last Name", txtLastName) != null && validateElementPresence("SSN", txtSSN) != null){				
			report.updateTestLog("Verify First & Last Name, SSN Fields displayed in login Page", "EPM Login page is displayed with First & Last Name, SSN Fields", Status.PASS);
		}else{
			report.updateTestLog("Verify First & Last Name, SSN Fields displayed in Login Page", "EPM Login page is NOT displayed..", Status.FAIL);	
		}
	}
	
	public void verifyLoginPageWith2TextFields() throws Exception{
		if(validateElementPresence("User Name EditBox",txtUserName) != null && validateElementPresence("Password EditBox", txtPassWord) != null){				
			report.updateTestLog("Verify UserName and Password Fields displayed in login Page", "EPM Login page is displayed with Username and Password fields", Status.PASS);
		}else{
			report.updateTestLog("Verify UserName and Password Fields displayed in Login Page", "EPM Login page is NOT displayed..", Status.FAIL);	
		}		
	}
	
	public void enterUserName(String textToBeTyped) throws Exception{
		try{			
			typeOnElement(txtUserName, textToBeTyped);
			report.updateTestLog("Enter UserName",  " ' "+ textToBeTyped + " ' " + " is entered in the UserName text field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterUserName" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	public void enterPassword(String textToBeTyped) throws Exception{
		try{
			typeOnElement(txtPassWord, textToBeTyped);
			report.updateTestLog("Enter Password",  " ' "+ textToBeTyped + " ' " + " is entered in the Password text field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterPassword" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	public void clickLoginButton() throws Exception {
		try{
			clickElement(btnLogin);
			report.updateTestLog("Click Login Button",  "Login Button is clicked", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickLoginButton" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void clickForgetPwdLink() throws Exception {
		try{
			clickElement(linkForgetPwd);
			report.updateTestLog("Click Forget password Link",  "Forget password Link is clicked", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickForgetPwdLink" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}

	
}