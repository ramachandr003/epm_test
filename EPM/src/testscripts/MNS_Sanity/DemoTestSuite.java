package testscripts.MNS_Sanity;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import supportlibraries.*;
import functionallibraries.*;

import com.cognizant.framework.*;

/**
 * Test for MNS Login and Logout
 * 
 * @author Ramachandr003
 */
public class DemoTestSuite extends TestScript
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
		/*Launch URL
		Verify UserName and Password Fields displayed in login Page
		Enter UserName
		Enter Password
		Click Login Button
		Verify the MNS Login functionality
		Click Admin Tab Link
		Verify the Admin Tab navigation
		Click Logout Link
		Verify UserName and Password Fields displayed in login Page*/
		
		MNS_LoginPage loginPage = new MNS_LoginPage(scriptHelper);
		MNS_HomePage homePage = new MNS_HomePage(scriptHelper);
		try{
			String userName = dataTable.getData("General_Data", "User_Name");
			String passWord = dataTable.getData("General_Data", "Password");
				
			loginPage.verifyLoginPage();
			loginPage.enterUserName(userName);
			loginPage.enterPassword(passWord);
			loginPage.clickLoginButton();
			homePage.verifyLoginFuncAndHome();	
			homePage.NavigateToAdminTab();
			homePage.clickLogoutLink();
			loginPage.verifyLoginPage();
		}
		catch (Exception e)
		{	
			report.updateTestLog("SCRIPT FAILED: Unable to Proceed", " Error=" + e.getMessage(), Status.FAIL);
		}			
	}	
}