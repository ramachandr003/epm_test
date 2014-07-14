package functionallibraries;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import com.cognizant.framework.*;
import com.google.common.base.Predicate;

import supportlibraries.*;

/**
 * MasterPage Abstract class
 * 
 * @author Cognizant
 */
/**
 * @author ramachandr003
 *
 */
abstract class MasterPage extends ReusableLibrary
{
	// UI Map object definitions
	Hashtable<String, String> iFrameLocator = new Hashtable<String, String>();
	
	
	
	protected static final String BY_XPATH = "byXpath";
	protected static final String BY_TAG_NAME = "byTagName";
	protected static final String BY_PARTIAL_LINK_TEXT = "byPartialLinkText";
	protected static final String BY_NAME = "byName";
	protected static final String BY_LINK_TEXT = "byLinkText";
	protected static final String BY_ID = "byId";
	protected static final int WAIT_TIME = Integer.parseInt(System.getProperty("wait.time", "90"));

	/**
	 * Constructor to initialize the functional library
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object passed from the
	 *            {@link TestScript}
	 * @param driver
	 *            The {@link WebDriver} object passed from the
	 *            {@link TestScript}
	 */
	protected MasterPage(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
		//Key should be in Lower case letters always and No Exceptions
		iFrameLocator.put("create new module", "//*/iframe[contains(@src,'AdminCreateNewModule')]");		
		iFrameLocator.put("template selector list", "//*/iframe[contains(@src,'TemplateSelectorList')]");
		iFrameLocator.put("add items to module", "//*/iframe[contains(@src,'AdminAddLessonsToModule')]");
		iFrameLocator.put("modify module properties", "//*/iframe[contains(@src,'AdminRenameModule')]");
		iFrameLocator.put("manage module preceptors", "//*/iframe[contains(@src,'AdminManagePreceptors')]");
		iFrameLocator.put("skill content", "ctl00_ContentMain_ifmContent");
		
		initializeUIMap();
	}

	public String getIframeLocatorByName(String strFrameName)  throws Exception
	{
		String result = null;
		try{
			result = iFrameLocator.get(strFrameName.toLowerCase().trim());
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		return result;
	}


	/**
	 * Function to initialize the UI Map
	 */
	protected abstract void initializeUIMap();

	/*
	 * @param by
	 * 		locator
	 * @param timeout
	 * 		time for wait
	 */
	
	public WebElement waitForElement(By by, int timeOut,String expCondAfterWait) {
	    WebDriverWait wait = new WebDriverWait(driver, timeOut);
	    WebElement element = null;
	    expCondAfterWait = expCondAfterWait.toUpperCase();
	    if (expCondAfterWait.equals("CLICKABLE")) {
	    	element = wait.until(ExpectedConditions.elementToBeClickable(by));
		} else if (expCondAfterWait.equals("VISIBLE")) {
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} else {
			element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
		}		    
	    return element;
	}
	public WebElement waitForElement(WebElement element, int timeOut,String expCondAfterWait) {
	    WebDriverWait wait = new WebDriverWait(driver, timeOut);	  
	    expCondAfterWait = expCondAfterWait.toUpperCase();
	    if (expCondAfterWait.equals("CLICKABLE")) {
	    	element = wait.until(ExpectedConditions.elementToBeClickable(element));
		} else if (expCondAfterWait.equals("VISIBLE")) {
			element = wait.until(ExpectedConditions.visibilityOf(element));
		} else {
			//no method available for checking the presence so using visibility
			element = wait.until(ExpectedConditions.visibilityOf(element));
		}		    
	    return element;
	}

	
	/**
	 * compares the Text with the Expected Text in the Application
	 * @param webElement
	 * @param textOnWebElement
	 * @throws Exception
	 */
	public void compareText(String webElement, String textOnWebElement) throws Exception{
		//assertEquals("The Expected Text "+textOnWebElement+" is not present in the application", 
				//validateElementDisplay(webElement, webElement).getText().trim(), textOnWebElement.trim());
	}
	
	/**
	 * Performs Click on the Element
	 * @param elementLocator
	 * @throws Exception
	 */
	public void clickElement(String elementLocator) throws Exception{	
		if(System.getProperty("browser").equalsIgnoreCase("iexplore")){			
		    int attempts = 0;
	        while(attempts < 2) {        	
	            try {
	            	validateElementPresence(elementLocator, elementLocator).click();	               
	                break;
	            } catch(StaleElementReferenceException e) {
	            }            
	            attempts++;
	            System.out.println("clickElement trying for '" + elementLocator + "' in attempt " + attempts );
	        }//while
		}else{
			validateElementPresence(elementLocator, elementLocator).click();
		}
	}
	
	/**
	 * Type on a Web Element Text Box
	 * @param elementLocator
	 * @param textToBeTyped
	 * @throws Exception
	 */
	public void typeOnElement(String elementLocator, String textToBeTyped)throws Exception{
		validateElementPresence(elementLocator, elementLocator).sendKeys(textToBeTyped);
	}
	
	
	/**
	 * Clear & Type on a Web Element Text Box
	 * @param elementLocator
	 * @param textToBeTyped
	 * @throws Exception
	 */
	public void clearAndTypeOnElement(String elementLocator, String textToBeTyped)throws Exception{
		WebElement element = validateElementPresence(elementLocator, elementLocator);
		if(element!=null){
			element.clear();
			element.sendKeys(textToBeTyped);
		}
		//validateElementPresence(elementLocator, elementLocator).sendKeys(textToBeTyped);
	}
	
	/**
	 * Select a visible value from dropdown
	 * @param elementLocator
	 * @param textToBeSelected
	 * @throws Exception
	 */
	public void selectTextFromDropDown(String elementLocator, String textToBeSelected) throws Exception{
		new Select(validateElementPresence(elementLocator, elementLocator)).selectByVisibleText(textToBeSelected);
	}
	
	
	/**
	 * Select a value from dropdown based on idex
	 * @param elementLocator
	 * @param index
	 * @throws Exception
	 */
	public void selectFromDropDownByIndex(String elementLocator, int index) throws Exception{
		new Select(validateElementPresence(elementLocator, elementLocator)).selectByIndex(index);
	}
	
	/**
	 * Select a value from dropdown based on value
	 * @param elementLocator
	 * @param value
	 * @throws Exception
	 */
	public void selectFromDropDownByValue(String elementLocator, String value) throws Exception{
		//new Select(validateElementPresence(elementLocator, elementLocator)).selectByValue(value);
		
		Select(validateElementPresence(elementLocator, elementLocator), value);
	}
	
	/**
	 * Select a Random value from dropdown if Value is Not present or Null or Empty
	 * @param elementLocator
	 * @param value (optional)
	 * @return boolean
	 * @throws Exception
	 */
	public boolean selectRandomIfOptionNotExist(String elementLocator, String strValue) throws Exception{
		//new Select(validateElementPresence(elementLocator, elementLocator)).selectByValue(strValue);
		boolean blnSelect = false;
		WebElement objListBox = validateElementPresence(elementLocator, elementLocator);
		
		List <WebElement> options = objListBox.findElements(By.tagName("option"));
		if(strValue.trim() != ""){
			for(WebElement option : options){			
		        if(option.getText().equalsIgnoreCase(strValue)){
		                option.click();                        
		                blnSelect = true;
		                break;
		        }else{
		        	blnSelect = false;
		        }
		    }//for end
		}
		if(!blnSelect){			
			//Select any Random value if NOT able to select the specified value
			for(WebElement option : options){			
		        if(option.getText().trim() != ""){
		                option.click();                        
		                blnSelect = true;
		                report.updateTestLog("Warning @ selectRandomIfOptionNotExist: '" + strValue + "' option NOT exist in Listbox - " + elementLocator + "." , 
		                		"'" + option.getText() + "' is selected instead of '" + strValue + "'", Status.WARNING);
		                break;
		        }else{
		        	blnSelect = false;
		        }
		    }//for end
			
		}
		return blnSelect;
	}
	
	
	/** To get the Value selected in the list box drop down
	 * @param objListBox
	 * @return
	 * @throws Exception
	 */
	public String getSelectedOptionInSelectListByElement(WebElement objListBox) throws Exception {				
		return new Select(objListBox).getFirstSelectedOption().getText();		
	}
	
	/**
	 * Verify the display of Element Presence
	 * @param elementName
	 * @param element
	 * @throws Exception
	 */
	public WebElement validateElementDisplay(String elementName, String element) throws Exception {
		WebElement elementFromApplication =validateElementPresence(elementName, element); 
		
		if(!elementFromApplication.isDisplayed()){
			report.updateTestLog("Validate Element Display", "'" + elementName + "'" + "is NOT displayed", Status.FAIL);
		}
		return elementFromApplication;
	}
	

	/**
	 * Verify the Element present in the Dom
	 * @param elementName
	 * @param element
	 * @return WebElement
	 * @throws Exception
	 */
	public WebElement validateElementPresence(String elementName, String element) throws Exception {
		WebElement elementFromApplication = waitForElement(element);
		if(elementFromApplication == null){
			//report.updateTestLog("Error occured @ validateElementPresence:", " '" + elementName + "'" + "is NOT present in the DOM", Status.FAIL);
			throw new Exception(" '" + elementName + "'" + "is NOT present in the DOM using the Locator " + element);
		}	
		return elementFromApplication;
	}
	/**
	 * Verify the element presence in DOM but not visible to User
	 * @param elementName
	 * @param element
	 * @throws Exception
	 */
	public void validateElementNotDisplayButPresent(String elementName, String element) throws Exception {
		validateElementNotPresence(elementName, element).isDisplayed();
		//assertFalse("The Element "+elementName+" is present", validateElementNotPresence(elementName, element).isDisplayed());
	}
	
	/**
	 * Verify the element non-presence in DOM
	 * @param elementName
	 * @param element
	 * @return WebElement
	 * @throws Exception
	 */
	public WebElement validateElementNotPresence(String elementName, String element) throws Exception {
		WebElement elementFromApplication = waitForElement(element);
		if(elementFromApplication !=null){
			report.updateTestLog("Error occured @ validateElementNotPresence:", " '" + elementName + "'" + "is present in the DOM", Status.FAIL);
		}
		//assertNull("The Element "+elementName+" is present in the Dom", elementFromApplication);
		return elementFromApplication;
	}



	/**
	 * Waits for an element to appear. It will only return after any overlays, and 
	 * loading icons that make items inactive has disappeared. If this is not done
	 * the element will be available, but the next action will not work since it is 
	 * not active 
	 * @param locator: Locator for the element to find
	 * @param by: The method to search by. Defined in the BY_*** constants
	 * @param parent: If the search should be done in relation to a parent element
	 * 					otherwise set to null
	 * @param activationTime: Once found, how much time to wait for the element to 
	 * 							become active (in seconds)
	 * @param waitTime: How long to wait for the element (in seconds) 
	 * @return A reference to the element if present. null otherwise
	 * @throws Exception
	 */
	private WebElement waitForElement(final String locator, final String by, final WebElement parent, 
			int activationTime, int waitTime) throws Exception {
	
			
		try {
			 
			WebElement element = null;
			// on some machines, the find method throws stale object exceptions when doing the find
			// in these cases catch the error wait a second and try it again
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.ignoring(StaleElementReferenceException.class);
			wait.ignoring(NoSuchElementException.class);
			
			element = wait.until(new ExpectedCondition<WebElement>(){
				public WebElement apply(WebDriver d) {					
					return findElement(locator, by, parent, d);
				}
			});

			try {
				
				WebDriverWait waitTillActive = new WebDriverWait(driver, activationTime);
				waitTillActive.ignoring(StaleElementReferenceException.class);
				waitTillActive.ignoring(NoSuchElementException.class);
				waitTillActive.until(new Predicate<WebDriver>() {
					public boolean apply(WebDriver d) {						
						return (findElement(locator, by, parent, d).isEnabled() && 
							findElement(locator, by, parent, d).isDisplayed());
							
					}
				});
			} catch (Exception e) {
				// if it is not enabled, then ignore the exception as some elements will be
				// verified for this condition on the test
			}
			// There is a block Overlay that will come into effect after some links are clicked.
			// we need to wait until that is disappeared before doing anything else 
		//	TestBase.waitForElementXpathToDisappear(TestBase.GLOBAL_BLOCK_OVERLAY);
			//log.info("found");
			return element;
		} catch (Exception e) {
			//TestBase.log.info("not found", e);
			return null;
		}	
	}
	/**
	 * Instant look at the page to see whether the element is there or not.
	 * There is no waiting.
	 * @param driver TODO
	 * @param locator: Element locator
	 * @param by: Search by method, defined by the BY_*** constants
	 * @param parent: If the search should be done from an element already in the page
	 * 					null otherwise
	 * @return A reference to the element if present
	 * @throws NoSuchElementException If the element is not present on the page
	 */
	
	//, StaleElementReferenceException
	protected  WebElement findElement(String locator, String by, WebElement parent, 
			WebDriver driver) throws NoSuchElementException {
		WebElement element = null;
		try{
			if(System.getProperty("browser").equalsIgnoreCase("iexplore")){				
				waitForPageToLoad(60);				
			}
		if (by.equals(BY_ID)) {
			if (parent != null) {				
				element = parent.findElement(By.id(locator));
			} else {				
				element = driver.findElement(By.id(locator));
			}			
		} else if (by.equals(BY_LINK_TEXT)) {
			if (locator.startsWith("link=")) {
				locator = getLinkText(locator);	
			}			
			if (parent != null) {
				element = parent.findElement(By.linkText(locator));
			} else {
				element = driver.findElement(By.linkText(locator));
			}			
		} else if (by.equals(BY_NAME)) {
			if (locator.startsWith("name=")) {
				locator = getNameText(locator);	
			}
			if (parent != null) {
				element = parent.findElement(By.name(locator));
			} else {
				element = driver.findElement(By.name(locator));
			}			
		} else if (by.equals(BY_PARTIAL_LINK_TEXT)) {
			System.out.println(" please work on it - BY_PARTIAL_LINK_TEXT");
			if (parent != null) {
				element = parent.findElement(By.partialLinkText(locator));
			} else {
				element = driver.findElement(By.partialLinkText(locator));
			}			
		} else if (by.equals(BY_TAG_NAME)) {
			System.out.println(" please work on it - BY_TAG_NAME");
			if (parent != null) {
				element = parent.findElement(By.tagName(locator));
			} else {
				element = driver.findElement(By.tagName(locator));	
			}			
		} else if (by.equals(BY_XPATH)) {
			if (parent != null) {
				element = parent.findElement(By.xpath(locator));
			} else {
				element = driver.findElement(By.xpath(locator));
			}			
		}
		
		
		}catch(StaleElementReferenceException e){
			System.out.println("findElement ----- StaleElementReferenceException for locator " + locator);
		}
		return element;
	}

	/**
	 * The link location was originally done with the locator "link=link name".
	 * Now WebDriver By.linkText() does not need the "link=" bit. This method 
	 * will strip it out 
	 * @param locator
	 * @return
	 */
	private String getLinkText(String locator) {
		String [] splits = locator.split("link=");
		return splits[1];
	}
	
	private String getNameText(String locator) {
		String [] splits = locator.split("name=");
		return splits[1];
	}
	
	/**
	 * There was no locate by method in Selenium RC. Everything was passed in
	 * as a string. However in WebDriver, the findElement() method needs a method
	 * to search by. When the old style locator string is passed to this method, 
	 * it will provide the locate by method 
	 * @param locator
	 * @return
	 */
	private String getLocateByMethod(String locator) {
		if (locator.contains("//")) {
			return BY_XPATH;
		} else if (locator.contains("link=")) {
			return BY_LINK_TEXT;
		} else if (locator.contains("name=")) {
			return BY_NAME;
		} else {
			return BY_ID;
		}					
	}
	
	/**
	 * Search for the web-element in the application 
	 * 
	 * @param locator
	 * @return Web-element
	 * @throws Exception
	 */

	protected WebElement waitForElement(String locator) throws Exception {
		return waitForElement(locator, getLocateByMethod(locator));
	}

	/**
	 * Search for the web-element in the application 
	 * 
	 * @param locator
	 * @param by
	 * @return Web-element
	 * @throws Exception
	 */

	protected  WebElement waitForElement(String locator, String by) throws Exception {
		return waitForElement(locator, by, null);
	}

	/**
	 * Search for an child web-element under the parent web-element in the application 
	 * 
	 * @param locator
	 * @param by
	 * @param parent
	 * @return Web-Element
	 * @throws Exception
	 */
	protected  WebElement waitForElement(String locator, String by, WebElement parent) throws Exception {
		
		return waitForElement(locator, by, parent, 10, WAIT_TIME);
		//return waitForElement(locator, by, parent, 10, WAIT_TIME);
	}

	
	
	/**
	 * Search for an child web-element under the parent web-element in the application in a given time span
	 * 
	 * @param locator
	 * @param by
	 * @param parent
	 * @param waitTime
	 * @return WebElement
	 * @throws Exception
	 */
	protected  WebElement waitForElement(String locator, String by, WebElement parent, int waitTime) throws Exception {
		return waitForElement(locator, by, parent, 10, waitTime);
		//return waitForElement(locator, by, parent, 10, WAIT_TIME);
	}
	

	/**
	 * Waits for the Element to Disappear with the Global Wait time
	 * @param id
	 * @return Element visibility status
	 * @throws Exception
	 */
	protected boolean waitForElementToDisappear(String id) throws Exception {
		return waitForElementToDisappear(id, getLocateByMethod(id) ,WAIT_TIME);
	}

	/**
	 * Waits for the Element to Disappear with the time specified
	 * @param id
	 * @param waitTime
	 * @return Element visibility status
	 * @throws Exception
	 */
		protected boolean waitForElementToDisappear(String id, int waitTime) throws Exception {
		return waitForElementToDisappear(id, getLocateByMethod(id), waitTime);
	}

		
		/**
		 * Will wait until the given element is disappeared off the screen
		 * @param locator: Element locator
		 * @param by: Search by method, defined by the BY_*** constants
		 * @param waitTime: How long to wait (in seconds)
		 * @return true if the element is disappeared. false otherwise 
		 * @throws Exception
		 */
		protected boolean waitForElementToDisappear(String locator, String by, int waitTime) throws Exception {
			boolean elementGone = false;
			if (by == null) {
				by = getLocateByMethod(locator);
			}
			try {
				// The time that is needed for the overlays to disappear is the same as
				// the time needed for page loads. So we use the WAIT_TIME here 
				for (int i = 0; i < waitTime; i++) {
					findElement(locator, by, null, driver);
					// log only the first time, and even then avoid logging for block overlay
					// because it fills up the log
					//log.info(locator + " found. Waiting for it to disappear " + i);	
					Thread.sleep(1000);
				}
				//log.info("Not Gone after wait time expiry");
			} catch (NoSuchElementException e) {
				// this is fine, the element has disappeared
				elementGone = true;
			}		
			return elementGone;
		}
	/**
	 * Will wait until the given element is disappeared off the screen but availble in DOM
	 * @param locator: Element locator
	 * @param by: Search by method, defined by the BY_*** constants
	 * @param waitTime: How long to wait (in seconds)
	 * @return true if the element is disappeared. false otherwise 
	 * @throws Exception
	 */
	
		// throws Exception
	protected boolean waitForElementToDisappearFromDisplay(String locator, String by, int waitTime) throws Exception {
		boolean elementGone = false;
		if (by == null) {
			by = getLocateByMethod(locator);			
		}
		waitTime(1); //by default Wait time
		if(System.getProperty("browser").equalsIgnoreCase("iexplore")){				
			waitForPageToLoad(60);				
		}
		try {
			// The time that is needed for the overlays to disappear is the same as
			// the time needed for page loads. So we use the WAIT_TIME here 
			for (int i = 0; i < waitTime; i++) {				
				WebElement ele = findElement(locator, by, null, driver);
				if(ele != null){
					//highlightElement(ele);					
					if(!ele.isDisplayed() ){						
						//report.updateTestLog("INFO for "+ locator +" :", "Waited "+(i+2) +" seconds to disappear the Loading message",Status.DONE);
						elementGone= true;
						break;
					}else{
						Thread.sleep(1000);
						//report.updateTestLog(locator + " found." + i , " Waiting for it to disappear ", Status.WARNING);
					}
				}					
			}
		}catch (NoSuchElementException e) {						
			// this is fine, the element has disappeared
			elementGone = true;
		}		
		return elementGone;
	}
	
	
	//Alert Functions
	/** verify Alert Present or NOT
	 * @return
	 */
	protected boolean isAlertPresent() {
		try{
			waitTime(2);
			driver.getTitle();
			return false;
		}catch(UnhandledAlertException e){		
			return true;
			
		}
	}
	
	protected boolean isDialogPresent() {
		try{
			waitTime(2);
			driver.switchTo().alert();			
			return true;
		}catch(NoAlertPresentException e){			
			return false;			
		}
	}
	//Webtable Functions
	
	public int getRowsWebTableByID(String strTableID, String strRowCol) throws Exception
	{
		
		String strXPath = "";
		int intCount = 0;
		try
		{				
			strXPath = "id('"+ strTableID + "')/tbody/tr" ;						
			//WebElement objTable = driver.findElement(By.id(strTableID));
			WebElement objTable = validateElementDisplay(strTableID, strTableID);
			System.out.println(strTableID + "objTable" +  objTable.isDisplayed());
		    List <WebElement> rowColl=objTable.findElements(By.xpath(strXPath));
		    intCount = rowColl.size();
		    if(strRowCol.trim().equals("ROW"))
		    {
		    	return intCount;		    	
		    }
		    else		    	
		    {		    	
			    for(WebElement trElement : rowColl)
			    {
			        List<WebElement> colColl=trElement.findElements(By.xpath("td"));
			        intCount = colColl.size();
			        return intCount;
			    }
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			intCount = 0;
		}
		return intCount;
	}
	
	
	
	public boolean verifyTextInWebTableByID(String strTableID, String strData)
	{
		
		String strText = "";
		boolean blnExist = false;

		try
		{				
				
			//WebElement objTable = driver.findElement(By.id(strTableID));
			WebElement objTable = validateElementDisplay(strTableID, strTableID);
			strText = objTable.getText();
			if(strText.toLowerCase().trim().contains(strData.toLowerCase()))
			{
				blnExist = true;
			}
			else
				blnExist = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return blnExist;
		}
		return blnExist;
	}
	
	
	public String getCellDataWebTableByID(String strTableID, int intRow, int intCol)
	{
		
		String strXPath = "";
		int intRowCount = 2;
		int intColCount = 0;
		String strData = "";
		try
		{				
			strXPath = "id('"+ strTableID + "')/tbody/tr" ;			
			//WebElement objTable = driver.findElement(By.id(strTableID));
			WebElement objTable = validateElementDisplay(strTableID, strTableID);
		    List <WebElement> rowColl=objTable.findElements(By.xpath(strXPath));
		    intRowCount = rowColl.size();	
		    if(intRow <= intRowCount)
		    {
			    for(WebElement trElement : rowColl)
			    {
			        List<WebElement> colColl=trElement.findElements(By.xpath("td"));
			        intColCount = colColl.size();
			        if(intCol <= intColCount)
			        {
			        	strData = driver.findElement(By.xpath("//table[@id='" + strTableID + "']/tbody/tr[" + intRow + "]/td[" + intCol + "]")).getText();
				        return strData;
			        }
			    }		    
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			strData = "";
		}
		return strData;
	}
	
	
	public int getRowWithTextInWebTableByID(String strTableID, String strData)
	{
		
		String strText = "";			
		int row_num;
		int col_num;
		try
		{				
				
			//WebElement objTable = driver.findElement(By.id(strTableID));
			WebElement objTable = validateElementDisplay(strTableID, strTableID);
			highlightElement(objTable);
			strText = objTable.getText();
			if(strText.toLowerCase().trim().contains(strData.trim().toLowerCase()))
			{
				List<WebElement> tr_collection=objTable.findElements(By.xpath("id('" + strTableID + "')/tbody/tr"));		       
		        row_num=1;
		        for(WebElement trElement : tr_collection)
		        {
		            List<WebElement> td_collection=trElement.findElements(By.xpath("td"));		           
		            col_num=1;
		            for(WebElement tdElement : td_collection)
		            {	//System.out.println(row_num + "---" + tdElement.getText());
		                if(tdElement.getText().toLowerCase().contains(strData.toLowerCase()))
		                {	
		                	return row_num;
		                	//break;	                	
		                }		               
		                col_num++;
		            }
		            row_num++;
		        }
			}
			else
			{
				return -1;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		return row_num;
	}
	
	
	public int getRowWithTextInColInWebTableByID(String strTableID, String strText, int intCol)
	{
		
		String strXPath = "";			
		strText = strText.trim().toLowerCase();
		int intRowNo = 0;
		boolean blnExist = false;
		try
		{				
			//strXPath = "id('"+ strTableID + "')/tbody/tr" ;	
			strXPath = "tbody/tr" ;	
			//WebElement objTable = driver.findElement(By.id(strTableID));
			WebElement objTable = validateElementDisplay(strTableID, strTableID);
		    List <WebElement> objRows=objTable.findElements(By.xpath(strXPath));
		    if(objRows.size()> 0 ){		    	
		    	for(WebElement rowElement : objRows)
			    {
		    		intRowNo = intRowNo + 1;
		    		if(rowElement.findElement(By.xpath("td[" + intCol + "]")).getText().toLowerCase().equals(strText)){
		    			blnExist= true;
		    			return intRowNo;
		    		}else{
		    			blnExist= false;
		    		}
			    }	
		    }else{
		    	return -1;
		    }		    	
		    
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		if(!blnExist){
			return -1;
		}else{
			return intRowNo;
		}
	}
	
	
	public int getRowWithPartialTextInColInWebTableByID(String strTableID, String strText, int intCol)
	{
		
		String strXPath = "";			
		strText = strText.trim().toLowerCase();
		int intRowNo = 0;
		boolean blnExist = false;
		try
		{				
			//strXPath = "id('"+ strTableID + "')/tbody/tr" ;	
			strXPath = "tbody/tr" ;	
			WebElement objTable = validateElementDisplay(strTableID, strTableID);
			//WebElement objTable = driver.findElement(By.id(strTableID));
		    List <WebElement> objRows=objTable.findElements(By.xpath(strXPath));
		    if(objRows.size()> 0 ){		    	
		    	for(WebElement rowElement : objRows)
			    {
		    		intRowNo = intRowNo + 1;
		    		if(rowElement.findElement(By.xpath("td[" + intCol + "]")).getText().toLowerCase().contains(strText)){
		    			blnExist= true;
		    			return intRowNo;
		    		}else{
		    			blnExist= false;
		    		}
			    }	
		    }else{
		    	return -1;
		    }		    	
		    
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		if(!blnExist){
			return -1;
		}else{
			return intRowNo;
		}
	}
	
	public int getRowWithMultipleTextInColInWebTableByID(String strTableID, String strNextLinkID, String strDeLimitedText)
	{
		String strDelimiter = System.getProperty("DataDelimiter");
		
		String strXPath = "";			
		strDeLimitedText = strDeLimitedText.trim().toLowerCase();
		String arrCnt[];
		arrCnt = strDeLimitedText.split(strDelimiter);
		int intArrLength = arrCnt.length;
		int intRowNo = 0;
		int i = 0;
		String strRowData = "";
		WebElement linkPagNext = null;
		boolean blnExist = false;
		try
		{	
			
	 
			do{
				strXPath = "tbody/tr" ;	
				intRowNo = 0;
				WebElement objTable = validateElementDisplay(strTableID, strTableID);
				//WebElement objTable = driver.findElement(By.id(strTableID));
			    List <WebElement> objRows=objTable.findElements(By.xpath(strXPath));
			    int intTotalCnt = objRows.size();
			    if(intTotalCnt > 0 ){
			    	int intExist = 0;
			    	for(WebElement rowElement : objRows)
				    {
			    		intRowNo = intRowNo + 1;
			    		strRowData = rowElement.getText().toLowerCase();
			    		intExist = 0;
			    		for(i=0;i<intArrLength;i++){
			    			if(strRowData.contains(arrCnt[i])){
				    			blnExist= true;		
				    			intExist = intExist+1;
				    		}else{
				    			blnExist= false;
				    		}
			    		}//for end	
			    		if(intExist == intArrLength ){
			    			break;
			    		}
				    }//for end
			    	
			    	if(blnExist && intExist == intArrLength ){
		    			return intRowNo;
		    		}else{
		    			linkPagNext = driver.findElement(By.id(strNextLinkID.trim()));
		    			if(intTotalCnt ==intRowNo && linkPagNext.isDisplayed() && !linkPagNext.getAttribute("class").toLowerCase().contains("disabled")){
			    			linkPagNext.click();
			    			waitTime(5);
			    		}
		    		}
			    }else{
			    	return -1;
			    }
			}while(linkPagNext.isDisplayed() && !linkPagNext.getAttribute("class").toLowerCase().contains("disabled"));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		if(!blnExist){
			return -1;
		}else{
			return intRowNo;
		}
	}

	public int getColWithTextInRowInWebTableByID(String strTableID, String strText, int intRow)
	{
		
		String strXPath = "";			
		strText = strText.trim().toLowerCase();
		int intColNo = 0;
		boolean blnExist = false;
		try
		{				
			strXPath = "id('"+ strTableID + "')/tbody/tr["+ intRow +"]" ;	
			WebElement objTable = validateElementDisplay(strTableID, strTableID);
			//WebElement objTable = driver.findElement(By.xpath(strXPath));
		    List <WebElement> objCols=objTable.findElements(By.xpath("td"));
		    if(objCols.size()> 0 ){		    	
		    	for(WebElement colElement : objCols)
			    {
		    		intColNo = intColNo + 1;
		    		if(colElement.getText().toLowerCase().contains(strText)){
		    			blnExist= true;
		    			return intColNo;
		    		}else{
		    			blnExist= false;
		    		}
			    }	
		    }else{
		    	return -1;
		    }		    	
		    
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		if(!blnExist){
			return -1;
		}else{
			return intColNo;
		}
	}
	/**
	 * To Get the Object WebElement from Web table based on the Parameters
	 * 
	 * @param strTableID - id Property of Webtable            
	 * @param strObjClass - should be tagName of the element (Eg. img, a, div)
	 * @param intIndex - should be integer and starts with 0 (Eg: 0 is for first object of above objClass)
	 * @param intRow - row number where you want to retrieve the object (should be start with 1 and above)
	 * @param intCol - Column number where you want to retrieve the object(should be start with 1 and above)
	 *           
	 * @return Identified element will be returned. null will be returned if the
	 *         element not found.
	 */
	public WebElement getObjectWithRowColInWebTableByID(String strTableID, String strObjClass, int intIndex, int intRow, int intCol)
	{
		// intIndex should start from Value 0 and should be an integer
		String strXPath = "";	
		WebElement objReturn = null;
		strObjClass = strObjClass.trim().toLowerCase();
		boolean blnExist = false;
		int intobjNo = 0;
		try
		{				
			strXPath = "id('"+ strTableID + "')/tbody/tr[" + intRow + "]/td[" + intCol + "]" ;			
			
			//WebElement objTable = driver.findElement(By.id(strTableID));
			WebElement objTable = validateElementDisplay(strTableID, strTableID);
		    List <WebElement> objColl=objTable.findElements(By.xpath(strXPath));
		    if(objColl.size()== 1){	    	
		    	for(WebElement tdCell : objColl){
		    		if(tdCell.isDisplayed()){
		    			onMouseOver(tdCell);
			    		int intCnt = tdCell.findElements(By.tagName(strObjClass)).size();
			    		
			    		if(intCnt > 0 ){
			    			List <WebElement> objColls = tdCell.findElements(By.tagName(strObjClass));
			    			for(WebElement objChild : objColls){
			    				intobjNo = intobjNo + 1;
			    				
			    				if(intIndex + 1 == intobjNo){				    					
			    					objReturn = objChild;
			    					blnExist = true;
			    					break;
			    				}else{
			    					blnExist = false;
			    					System.out.println("GetObjectWithRowColInWebTableByID Error: No Match for the index value");
			    				}
			    			}
			    		}else{
			    			blnExist = false;
			    			System.out.println("GetObjectWithRowColInWebTableByID Error: No Match for the objClass ["+ strObjClass +"] value");
			    		}
		    		}
			    }	
		    }else{
		    	blnExist = false;
		    	System.out.println("GetObjectWithRowColInWebTableByID Error: No Match for the Row and Column");
		    }		    	
		    
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		if(!blnExist){
			return null;
		}else{
			return objReturn;
		}
		
	}
	
	/**
	 * To Verify the Text present in each rows of a WebTable
	 * 
	 * @param strTableID - id Property of Webtable            
	 * @param strText - Text to verify in each row of webtable
	 *            
	 * @return boolean value
	 */
	public boolean verifyTextInEachRowWebTableByID(String strTableID, String strText){		
		String strXPath ="";
		int intRowNo = 0;
		boolean blnExist = false;
		try{
			strXPath = "id('"+ strTableID + "')/tbody/tr" ;	
			WebElement objTable = validateElementDisplay(strTableID, strTableID);
			//WebElement objTable = driver.findElement(By.id(strTableID));
		    List <WebElement> objColl=objTable.findElements(By.xpath(strXPath));
		    //int intRows = objColl.size();
		    if(objColl.size()> 0){	 
		    	for(WebElement objRow : objColl){
    				intRowNo = intRowNo + 1;    				
    				if(objRow.getText().toLowerCase().contains(strText.toLowerCase())){   					
    					blnExist = true;
    				}else{
    					blnExist = false;
    					System.out.println("Error: VerifyTextInEachRowWebTableByID: Data Not present in Row["+ intRowNo +"] - FAIL");
    					break;
    				}
    			}		    
		    }else{
		    	System.out.println("Error: VerifyTextInEachRowWebTableByID: No Rows in Table - FAIL");
		    	return false;
		    }
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}		
		if(blnExist){
			return true;
		}else{
			return false;
		}
	}

	
	
	
	
	
	
	
	/**
	 * 
	 * @param objCheckbox
	 *            Checkbox object
	 * @param sCheckboxName
	 *            Checkbox name . This will be used for report log
	 * @param sCheckValue
	 *            Values should be ignore or true,check or false,uncheck
	 * @return
	 * @author Sivaraj
	 * @since Sep 6, 2012
	 */
	protected boolean CheckBox_isSelected(WebElement objCheckbox, String sCheckboxName, String sCheckValue)
	{
		boolean result = false;
		try
		{
			if (!sCheckValue.equalsIgnoreCase("ignore"))
			{
				boolean blnCheck = sCheckValue.trim().equalsIgnoreCase("true") || sCheckValue.trim().equalsIgnoreCase("CHECK");
				if (objCheckbox.isSelected() == blnCheck)
				{
					report.updateTestLog("Verify the '" + sCheckboxName + "' checkbox is " + (blnCheck ? "checked" : "unchecked") + ".",
							"Verified : The '" + sCheckboxName + "' checkbox is " + (blnCheck ? "checked" : "unchecked") + ".", Status.PASS);
					result = true;
				}
				else
					report.updateTestLog("Verify the '" + sCheckboxName + "' checkbox is " + (blnCheck ? "checked" : "unchecked") + ".",
							"The '" + sCheckboxName + "' checkbox is NOT " + (blnCheck ? "checked" : "unchecked") + ".", Status.FAIL);
			}
		}
		catch (Exception e)
		{
			report.updateTestLog("Error occured.", "MasterPage - CheckBox_isSelected : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
			return false;
		}
		return result;
	}

	/**
	 * Sign out link click
	 * 
	 * @return
	 */
	public boolean ID593_BC_AUTH_Sign_Out()
	{
		boolean result = true;
		try
		{
//			WebElement lnkSignOutLink = this.isExist(By.id("SignOutLinkButton"));
			WebElement lnkSignOutLink = this.isExist(By.xpath("//a[contains(@data-func,'opensignOut')]"));
			if (lnkSignOutLink != null)
			{
//				driver.findElement(By.id("SignOutLinkButton")).click();
				lnkSignOutLink.click();
				// if (this.isExist(By.id("login")) != null &&
				// driver.findElement(By.id("login")).findElements(By.linkText("Un-save my sign-in information")).size()
				// > 0)
				// driver.findElement(By.id("login")).findElement(By.linkText("Un-save my sign-in information")).click();
				if (this.isExist(By.name("userid")) != null)
					report.updateTestLog("Click the Sign Out link.", "Verified : Lexis Advance Sign In Page is displayed.", Status.PASS);
				else
				{
					result = false;
					report.updateTestLog("Click the Sign Out link.", "Lexis Advance Sign In Page is not displayed.", Status.FAIL);
				}
			}
			else
			{
				result = false;
				report.updateTestLog("Click the Sign out link.", "Unable to click Sign Out link.", Status.FAIL);
			}

		}
		catch (Exception e)
		{
			result = false;
			report.updateTestLog("Error occuered.", "ID593_BC_AUTH_Sign_Out : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return result;
	}

	public boolean ID617_BC_COMM_Close_Browser()
	{
		boolean result = true;
		try
		{
			String parent = null;
			Set<String> handles = driver.getWindowHandles();
			if (handles.size() > 1)
			{
				Iterator<String> it = handles.iterator();
				if (it.hasNext())
				{
					parent = it.next();
					// parent = it.next();
				}
			}

			if (parent != null)
			{
				driver.close();
				driver.switchTo().window(parent);
			}
			report.updateTestLog("Close the active browser window.", "Active browser window closed.", Status.PASS);
		}
		catch (Exception e)
		{
			result = false;
			report.updateTestLog("Error occuered.", "ID617_BC_COMM_Close_Browser : " + e.getMessage(), Status.FAIL);
			e.printStackTrace();
		}
		return result;
	}
	
	
	public String getCurrentMethodName(String strClassName){	
		String returnText = "";
		StackTraceElement[] methodName = Thread.currentThread().getStackTrace();
		for(int i=0; i<methodName.length; i++){
			if(methodName[i].toString().startsWith(strClassName)){
				//String temp = methodName[i].toString();
				//System.out.println(temp);
				//temp = temp.substring(temp.indexOf("(")+1, temp.indexOf(")"));
				//returnText = temp.replace(".java:", " at line of ");
				returnText = methodName[i].toString();				
				break;
			}
		
		}
		return returnText;
	}
	
	
	public void switchFrame(String IframeLocator) throws Exception{
		driver.switchTo().frame(validateElementPresence(IframeLocator, getIframeLocatorByName(IframeLocator)));
		report.updateTestLog("Verify the window Frame'" + IframeLocator + "' is opened.",  "'" + IframeLocator + "' frame window is displayed.", Status.DONE);
	}
	
	public void closeFrame(String strFrameNameToReport) throws Exception{
		boolean blnCheck = false;
		driver.switchTo().defaultContent();
		List<WebElement> closeColl = driver.findElements(By.xpath("//*/a[@class='rwCloseButton']"));	
		for(WebElement objDisplayed : closeColl){			            	
            if(objDisplayed.isDisplayed()){            	
            	onMouseClick(objDisplayed);	
            	blnCheck = true;            	
            	break;
            }
        }
		waitTime(1);
		if(blnCheck){
			if(strFrameNameToReport.trim().equalsIgnoreCase("Error Handling")){
				report.updateTestLog("WARNING - Close the frame for " + strFrameNameToReport + "",  "Frame Window is closed.", Status.WARNING);
			}else{
				report.updateTestLog("Close the frame " + strFrameNameToReport + "",  " ' "+ strFrameNameToReport + " ' is closed and Driver Control back to Parent Window", Status.DONE);
			}			
		}
	}
	
	public boolean checkFrameDisplayed()  throws Exception{
		boolean blnCheck = false;
		List<WebElement> iframeColl = driver.findElements(By.xpath("//*/iframe"));
		for(WebElement objDisplayed : iframeColl){			            	
            if(objDisplayed.isDisplayed()){            	
            	blnCheck = true;            	
            	break;
            }
        }
		return blnCheck;
	}
	
	/**
	 * Close Alert by Acceptance and Report What Alert displayed while closing.
	 * When the browser session is not closed properly,
	 * an error dialogue is given on the login page. This method is mainly used to 
	 * get rid of it.
	 */
	protected void closeAlertAndReport() throws Exception{
		// sometimes if the browser session is not closed properly an error alert appears
		// we need to check for it for 5 seconds and accept if it appears
		try {
			String strText = returnTextOnAlert(waitForAlert(1));
			actionOnAlert(waitForAlert(1) , "ACCEPT");
			report.updateTestLog("Close the Alert if any displayed. " ,  " ' "+ strText + " ' Alert is closed", Status.DONE);
		} catch (Exception e) {
			System.out.println("No Alert Present");
		}
	}
	
	
	public void handlePrintWindow() throws Exception{
		try {
			if(System.getProperty("browser").equalsIgnoreCase("iexplore") ){
				actionOnAlert(waitForAlert(2),"DISMISS");
				report.updateTestLog("Close the Print window Dialog",  "Print window dialog is closed.", Status.DONE);
			}
			//if(System.getProperty("browser").equalsIgnoreCase("chrome") || System.getProperty("browser").equalsIgnoreCase("firefox")){				
			//	WebElement objCancelButton = validateElementPresence("Cancel print Button", "//div[@id='print-header']/*/button[text()='Cancel']");
			//	if(objCancelButton!=null){
			//		objCancelButton.click();
			//	}
			//}
		} catch (Exception e) {
			System.out.println("Masterpage.handlePrintWindow - Unable to handle Print window \n" +  e.getMessage());
		}
	}

	public void waitForPageToLoad(int WAIT_TIME_IN_SECONDS){
		try{
			(new WebDriverWait(driver, WAIT_TIME_IN_SECONDS)).until(new ExpectedCondition<Boolean>() {
	      public Boolean apply(WebDriver d) {
	        return (((org.openqa.selenium.JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
	      }
	    });
		}catch(Exception e){
			System.out.println("*******  waitForPageToLoad ----- " + e.getMessage());
		}
	}
	

}// class end