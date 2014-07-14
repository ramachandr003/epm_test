package functionallibraries;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import supportlibraries.*;

import com.cognizant.framework.*;

/**
 * MNS_LoginPage class
 * 
 * @author Ramachandr003
 */
public class MNS_LoginPage extends MasterPage
{
	// UI Map object definitions
	
	
	
	public String txtLoginUser = "ctl00_HeaderDefaultCtl_txtLogin";
	public String txtLoginPword = "ctl00_HeaderDefaultCtl_txtPassword";
	public String btnLoginSubmit = "ctl00_HeaderDefaultCtl_btnLogin";
	

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
	public MNS_LoginPage(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
	}

	@Override
	protected void initializeUIMap()
	{

	}
	public void verifyLoginPage() throws Exception{
		try{
			if(validateElementPresence("User Name EditBox", txtLoginUser) != null && validateElementPresence("Password EditBox", txtLoginPword) != null)
			{				
				report.updateTestLog("Verify UserName and Password Fields displayed in login Page", "MNS Login page is displayed with Username and Password fields", Status.PASS);
			}
			else
			{
				report.updateTestLog("Verify UserName and Password Fields displayed in Login Page", "MNS Login page is NOT displayed..", Status.FAIL);	
			}

		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterUserName" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void enterUserName(String textToBeTyped){
		try{
			/*WebElement element = driver.findElement(By.id("ctl00_HeaderDefaultCtl_txtLogin"));
			System.out.println(element.isDisplayed());*/
			typeOnElement(txtLoginUser, textToBeTyped);
			report.updateTestLog("Enter UserName",  "'"+ textToBeTyped + "'" + " is entered in the UserName text field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterUserName" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	public void enterPassword(String textToBeTyped){
		try{
			typeOnElement(txtLoginPword, textToBeTyped);
			report.updateTestLog("Enter Password",  "'"+ textToBeTyped + "'" + " is entered in the Password text field", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "enterPassword" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	public void clickLoginButton(){
		try{
			clickElement(btnLoginSubmit);
			report.updateTestLog("Click Login Button",  "Login Button is clicked", Status.DONE);
		}catch(Exception e){
			report.updateTestLog("Error occured.",  this.getClass().getName() + "." + "clickLoginButton" + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
	}
	


	
}