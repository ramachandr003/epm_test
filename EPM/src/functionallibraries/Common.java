package functionallibraries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import supportlibraries.*;

import com.cognizant.framework.*;

/**
 * SignOnPage class
 * 
 * @author Cognizant
 */
/**
 * @author ramachandr003
 *
 */
/**
 * @author ramachandr003
 *
 */
public class Common extends MasterPage
{
	// UI Map object definitions
	
	public String loadingProcessMessageText = "processMessage";
	
	public String loadingPleaseWait = "ctl00_ContentMain_ModalProgressDialog1_pnlProgress";
	
	public String headerText = "//h1[@class='ucd-content-wrapper-heading']";

	


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
	
	
	public Common(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
	}

	@Override
	protected void initializeUIMap()
	{

	}

	
	public void verifyLoadingProcessMessageDisappear(int intWaitTime) throws Exception{
		waitForElementToDisappearFromDisplay(loadingProcessMessageText,null,intWaitTime);		
	}
	
	public void verifyLoadingPleaseWaitMessageDisappear(int intWaitTime) throws Exception{		
		waitForElementToDisappearFromDisplay(loadingPleaseWait,null,intWaitTime);			
	}
	
	
	
	public boolean SelectTab(String tabName,String pageName)
	{
		boolean result = true;
		try
		{
			WebElement tabProduct = this.isExist(By.id(tabName));
			
			tabProduct.click();
			if (this.isExist(By.id(pageName)) != null )
				result = true ;
			else
				result = false;
		}
		catch (Exception e)
		{
			result = false;
			report.updateTestLog("Error occured.", "SelectTab : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean SelectLink_Table(String TableName,String LinkName)
	{
		boolean result = false;
		try 
		{
			WebElement tableProduct = this.isExist(By.className(TableName));
			tableProduct.findElement(By.className(LinkName)).click();
			result = true;
		}
		catch (Exception e)
		{
			report.updateTestLog("Error occured.", "SelectLink_Table : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Function is used to Transfer the Driver control the secondary browser window.
	 * 
	 * @return
	 * @author: Automation Team
	 * @since:
	 */
	public boolean verifySecondaryBrowserWindow(String strTitleorURL) throws Exception
	{
		boolean result = false;
		try
		{
			Set<String> handles = driver.getWindowHandles();
			for (int i = 0; i < 4; i++)
			{
				if (handles.size() > 1)
					break;
				Thread.sleep(5000);
				handles = driver.getWindowHandles();
			}
			Iterator<String> it = handles.iterator();
			if (it.hasNext())
			{
				String parent = it.next();
				
				if (parent != null)
				{
				}
				
				driver.switchTo().window(it.next());
				waitTime(1);
				driver.manage().window().maximize();
				/*
				if(driver.getTitle().contains(strTitleorURL.trim())||driver.getCurrentUrl().contains(strTitleorURL.trim())){
					result = true;
				}else{
					result = false;
				}*/				
				result = true;
			}
			if (result)
				report.updateTestLog("Verify the secondary browser window", "Verified : Secondary browser window '" + driver.getTitle() + "' is displayed.", Status.PASS);
			else
				report.updateTestLog("Verify the secondary browser window", "Secondary browser window is not displayed.", Status.FAIL);

		}
		catch (Exception e)
		{
			result = false;
			report.updateTestLog("Error occuered.", "VerifySecondary_Browser_Window : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Function is used to Close the Sec Browser and Transfer the Driver control back to Primary window.
	 * 
	 * @return
	 * @author: Automation Team
	 * @since:
	 */
	public boolean closeSecondaryBrowserWindow(String strTitleorURL)
	{
		boolean result = false;
		try
		{
			driver.close();
			report.updateTestLog("Close the Secondary Browser Window.", "'" + strTitleorURL + "' browser window is closed.", Status.DONE);
			smallWait();
			for (String handle : driver.getWindowHandles()) {
				driver.switchTo().window(handle);
			}
				// driver.close();
				// System.out.print(driver.getTitle());
				// driver.switchTo().window(parent);
				result = true;
			
		}
		catch(SessionNotFoundException e){
			report.updateTestLog("Close the Browser" + strTitleorURL , "No Such Browser/Session found to Close.", Status.WARNING);
		}
		catch (Exception e)
		{
			result = false;
			report.updateTestLog("Error occured.", "CloseSecondary_Browser_Window : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return result;
	}

	
	
	public boolean verifyPageNameByH1TagHeader(String strText) throws Exception{
		boolean result = false;
		try{
			waitTime(1);
			if(System.getProperty("browser").equalsIgnoreCase("iexplore")){				
				waitForPageToLoad(60);				
			}
			WebElement objH1Tag = validateElementPresence(strText, headerText);
			if(objH1Tag != null){
				if(objH1Tag.getText().trim().equalsIgnoreCase(strText.trim())){
					result = true;
				}				
			}		
		}catch(Exception e){			
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	public boolean clickPagination(String strTableID,String strPrevNextArrowButton)throws Exception{
		boolean result = false;
		
		try{
			String strXPath = "" ;			
			WebElement objTable = validateElementDisplay(strTableID, strTableID); //driver.findElement(By.id(strTableID));
			
			if(strPrevNextArrowButton.trim().toUpperCase().equals("FIRST")){
				strXPath = "//*/input[@class='rgPageFirst']" ;
			}else if(strPrevNextArrowButton.trim().toUpperCase().equals("LAST")){
				strXPath = "//*/input[@class='rgPageLast']" ;				
			}else if(strPrevNextArrowButton.trim().toUpperCase().equals("PREV")){
				strXPath = "//*/input[@class='rgPagePrev']" ;
			}else if(strPrevNextArrowButton.trim().toUpperCase().equals("NEXT")){
				strXPath = "//*/input[@class='rgPageNext']" ;
			}else if(strPrevNextArrowButton.trim().toUpperCase().equals("...")){
				strXPath = "//*/div[@class='rgWrap rgNumPart']/*/span[text()='"+ strPrevNextArrowButton.trim() +"']" ;
			}else if(CustomFunctions.isNumeric(strPrevNextArrowButton)){
				strXPath = "//*/div[@class='rgWrap rgNumPart']/*/span[text()='"+ strPrevNextArrowButton.trim() +"']" ;
			}
			
			if(objTable != null){
				List <WebElement> pager =objTable.findElements(By.xpath(strXPath));			   
			    for(WebElement PagerArrow : pager){				    	
			    	if(PagerArrow.isEnabled() && PagerArrow.getAttribute("onclick") == null){
				    	onMouseClick(PagerArrow);				    	
				    	waitTime(2);
				    	result = true;
			    	}else{
			    		result = false;
			    		report.updateTestLog("clickPagination method : Click "+strPrevNextArrowButton, "onclick Attribute set to Return False. Not required to Click", Status.WARNING);
			    	}
			    	break;
			    }			    
			}	    
			
		}catch (Exception e)
		{
			result = false;
			report.updateTestLog("Error occured.", getCurrentMethodName(this.getClass().getName()) + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return result;		
	}
	
	
	public String PaginationInfo(String strTableID,String strInfo)throws Exception{
	
		String strXPath = "//*/div[@class='rgWrap rgInfoPart']" ;	
		String strReturn = "";
		try{
			
			WebElement objTable = validateElementDisplay(strTableID, strTableID); //driver.findElement(By.id(strTableID));			
			if(objTable != null){
				List <WebElement> pager =objTable.findElements(By.xpath(strXPath));			    
			    for(WebElement PagerText : pager){			    	
			    	strReturn=  PagerText.getText();			    	
			    	break;
			    }			    
			}	    
			
		}catch (Exception e)
		{			
			report.updateTestLog("Error occured.", "clickPagination : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return strReturn;
	}
	
	
	public boolean verifyItemNameInPaginationTable(String strTableID, String strItemName ) throws Exception{
		boolean result = true;
		try{
			if(validateElementDisplay(strTableID, strTableID)!= null){				
				clickPagination(strTableID, "FIRST");
				while(getRowWithTextInColInWebTableByID(strTableID, strItemName, 1)== -1){
					result = false;
					if(!clickPagination(strTableID, "NEXT")){
						break;
					}
				}
				if(getRowWithTextInColInWebTableByID(strTableID, strItemName, 1)!= -1){
					result = true;
				}
			}
		
		}
		catch(StaleElementReferenceException ex){
			System.out.println("ignore StaleElementReferenceException");
		}
		catch(Exception e){			
			report.updateTestLog("Error occured.",  getCurrentMethodName(this.getClass().getName()) + " : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			throw e;
			}
		return result;
	}
	
	
	
}//class end here
