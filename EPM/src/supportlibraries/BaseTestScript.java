package supportlibraries;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.internal.compiler.lookup.UpdatedMethodBinding;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cognizant.framework.CraftliteDataTable;
import com.cognizant.framework.Settings;

import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author RxxS1
 * 
 */
public class BaseTestScript
{

	private WebDriver driver = null;
	protected WebDriverWait wait = null;
	private Properties properties = null;
	private CraftliteDataTable dataTable = null;

	public BaseTestScript()
	{
	}

	protected void setDriver(WebDriver drive, CraftliteDataTable dataTabl)
	{
		driver = drive;
		wait = new WebDriverWait(driver, 60);
		properties = Settings.getInstance();
		dataTable = dataTabl;
	}

	public String getData(String columnName)
	{
		return dataTable.getData("General_Data", columnName);
	}

	/**
	 * Function is used to highlight the UI element
	 * 
	 * @element Element which need to be highlighted
	 */
	public void highlightElement(WebElement element)
	{
		try
		{
			for (int i = 0; i < 1; i++)
			{
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: yellow; border: 3px solid black;");
				Thread.sleep(300);
				js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Wait for the element to be visible
	 * 
	 * @param by
	 *            used to identify the element
	 * @return Identified element will be returned. null will be returned if the
	 *         element not found.
	 */
	
	public WebElement isExist(By by)
	{
		try
		{
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
			highlightElement(element);
			return element;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Function is used to verify the element Font is Bold: Developed By : Gokul
	 * 
	 * @param element
	 * @return
	 */
	public boolean CSS_Font_IsBold(WebElement element)
	{
		boolean result = false;
		try
		{
			result = (element.getCssValue("font-weight").equalsIgnoreCase("bold") || Integer.parseInt(element.getCssValue("font-weight")) > 400);
		}
		catch (Exception e)
		{
			result = false;
		}
		return result;
	}

	/**
	 * 
	 * @param parent
	 *            parent WebElement
	 * @param sign
	 *            Possible value : + or -
	 * @param FolderName
	 *            Possible value : Folder1;folder2;folder
	 * @param IsClick
	 *            Values ; True - Will click the +/- symbol, False - verifies
	 *            the +/- symbol
	 * @return
	 * @author : Anup Agarwal
	 * @since : Aug 30, 2012
	 * @last Modified Sep 11,2012
	 */
	public boolean Select_FolderTree_Plus_Minus(WebElement parent, String sign, String FolderName, boolean IsClick)
	{
		boolean result = false;
		try
		{
			String folders[] = FolderName.toLowerCase().trim().split(",");
			String strLinkName = null;
			String strLink[];
			boolean blnflag = false;
			WebElement parent1 = parent;
			for (int j = 0; j < folders.length; j++)
			{
				List<WebElement> lis = parent1.findElements(By.tagName("li"));
				for (int i = 0; i < lis.size(); i++)
				{
					strLinkName = lis.get(i).findElement(By.tagName("a")).getText();
					if (strLinkName.contains("("))
					{
						strLink = strLinkName.split("\\(");
						if (strLink[0].trim().equalsIgnoreCase(folders[j]) && lis.get(i).findElement(By.tagName("a")).isDisplayed())
						{
							blnflag = true;
						}
					}
					else if (strLinkName.trim().equalsIgnoreCase(folders[j]) && lis.get(i).findElement(By.tagName("a")).isDisplayed())
					{
						blnflag = true;
					}

					if (blnflag)
					{
						if (j == folders.length - 1)
						{
							if (sign.equalsIgnoreCase("-"))
							{

								if (lis.get(i).findElement(By.tagName("ul")).isDisplayed())
								{
									if (IsClick)
										lis.get(i).findElement(By.tagName("ins")).click();
									result = true;
									break;
								}
							}
							else if (sign.equalsIgnoreCase("+"))
							{
								if (!(lis.get(i).findElement(By.tagName("ul")).isDisplayed()))
								{
									if (IsClick)
										lis.get(i).findElement(By.tagName("ins")).click();
									result = true;
									break;
								}
							}

						}
						parent1 = lis.get(i);
						break;
					}
				}
				if (result)
					break;
			}
		}
		catch (Exception e)
		{
			result = false;
		}
		return result;
	}

	/**
	 * 
	 * @param parent
	 *            Parent webelement
	 * @param FolderName
	 *            Possible value : Folder1;folder2;folder
	 * @param IsClick
	 *            Values ; True - Will click the folder, False - verifies the
	 *            folder
	 * @return
	 * @author Sivaraj
	 * @since Aug 24, 2012
	 * @last Modified Sep 11,2012(Anup Agarwal)
	 */
	public boolean Select_FolderTree(WebElement parent, String FolderName, boolean IsClick)
	{
		boolean result = false;
		boolean blnflag = false;
		try
		{
			String folders[] = FolderName.split(",");
			String strLinkName;
			String strLink[];
			WebElement parent1 = parent;
			for (int j = 0; j < folders.length; j++)
			{
				List<WebElement> lis = parent1.findElements(By.tagName("li"));
				for (int i = 0; i < lis.size(); i++)
				{
					strLinkName = lis.get(i).findElement(By.tagName("a")).getText();
					if (strLinkName.contains("("))
					{
						strLink = strLinkName.split("\\(");
						if (strLink[0].replaceAll("[^a-zA-Z0-9]", "").equalsIgnoreCase(folders[j].replaceAll("[^a-zA-Z0-9]", "")) && lis.get(i).findElement(By.tagName("a"))
								.isDisplayed())
						{
							blnflag = true;
						}
					}
					else if (strLinkName.replaceAll("[^a-zA-Z0-9]", "").equalsIgnoreCase(folders[j].replaceAll("[^a-zA-Z0-9]", "")) && lis.get(i).findElement(By.tagName("a"))
							.isDisplayed())
					{
						blnflag = true;
					}

					if (blnflag)
					{
						if (j == folders.length - 1)
						{
							if (IsClick)
								lis.get(i).findElement(By.tagName("a")).click();
							result = true;
							break;
						}
						else if (!lis.get(i).findElement(By.tagName("ul")).isDisplayed())
							lis.get(i).findElement(By.tagName("ins")).click();
						parent1 = lis.get(i);
						break;
					}
				}
			}
		}
		catch (Exception e)
		{
			result = false;
		}
		return result;
	}

	/**
	 * Wait for the element to be visible
	 * 
	 * @param by
	 *            used to identify the element
	 * @param timeOut
	 *            waits for the specified time. (value in Seconds)
	 * @return Identified element will be returned. null will be returned if the
	 *         element not found.
	 */
	public WebElement isExist(By by, int timeOut)
	{
		try
		{
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebDriverWait wait1 = new WebDriverWait(driver, timeOut);
			WebElement element = wait1.until(ExpectedConditions.elementToBeClickable(by));
			//highlightElement(element);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			return element;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		return null;
	}

	/**
	 * Wait for the element to be visible
	 * 
	 * @param parent
	 *            parent element
	 * @param by
	 *            used to identify the element
	 * @param timeOut
	 *            waits for the specified time. (value in Seconds)
	 * @return Identified element will be returned. null will be returned if the
	 *         element not found.
	 */
	public WebElement isExist(WebElement parent, By by, int timeOut)
	{
		try
		{
			driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
			WebElement element = parent.findElement(by);
			highlightElement(element);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			return element;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		return null;
	}

	
	/**
	 * Waits for Specified Seconds.
	 */
	public void waitTime(int intWaitSec)
	{   try
		{
			Thread.sleep(intWaitSec * 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Waits for few seconds which is specified in the config file.
	 */
	public void smallWait()
	{
		try
		{
			Thread.sleep(Integer.parseInt(properties.getProperty("SmallWait")) * 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Waits for few seconds which is specified in the config file.
	 */
	public void mediumWait()
	{
		try
		{
			Thread.sleep(Integer.parseInt(properties.getProperty("MediumWait")) * 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Waits for few seconds which is specified in the config file.
	 */
	public void longWait()
	{
		try
		{
			Thread.sleep(Integer.parseInt(properties.getProperty("LongWait")) * 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean Select(WebElement element, String value)
	{
		boolean result = false;
		try
		{
			Select select = new Select(element);
			select.selectByVisibleText(value);
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public String Select_GetSelected(WebElement element)
	{
		String result = null;
		try
		{
			Select select = new Select(element);
			if (select.getAllSelectedOptions().size() > 0)
				result = select.getAllSelectedOptions().get(0).getText();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	/**
	 * 
	 * @param color1
	 *            The value retrieved from getAttribute function
	 * @param color2
	 *            The format should be #rrggbb Ex: #097865
	 * @return
	 * @author Sivaraj
	 * @since Sep 21, 2012
	 */
	public boolean compareColor(String color1, String color2)
	{
		boolean result = false;
		try
		{
			Color col1 = null;
			if (color1.contains("rgba"))
			{
				final Pattern RGBA_PATTERN = Pattern.compile("^\\s*rgba\\(\\s*" + "(\\d{1,3})\\s*,\\s*(\\d{1,3})\\s*,\\s*" + "(\\d{1,3})\\s*,\\s*(0|1|0\\.\\d+)\\s*\\)\\s*$");

				Matcher m = RGBA_PATTERN.matcher(color1);
				if (m.matches())
				{
					color1 = "rgb(" + m.group(1) + "," + m.group(2) + "," + m.group(3) + ")";
				}
			}

			col1 = Color.fromString(color1);
			Color col2 = Color.fromString(color2);
			return col1.asRgb().equals(col2.asRgb());
		}
		catch (Exception e)
		{
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public void scrollDown(int height)
	{
		String str = Integer.toString(height);
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + str + ");");
	}

	public void scrollUp(int height)
	{
		if (height > 0)
		{
			String str = Integer.toString(height);
			((JavascriptExecutor) driver).executeScript("scroll(0,-" + str + ");");
		}
		else
		{
			String str = Integer.toString(height * -1);
			((JavascriptExecutor) driver).executeScript(("window.scrollTo(0," + str + ");"));
		}
	}

	public void scrollDown()
	{
		// ((JavascriptExecutor) driver).executeScript("scroll(0,200);");
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}

	public void scrollUp()
	{
		// ((JavascriptExecutor) driver).executeScript("scroll(0,200);");
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, -document.body.scrollHeight);");
	}

	public boolean ScrollToView(WebElement element)
	{
		boolean result = false;
		try
		{
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('scrollIntoView', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('scrollIntoView');}";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(mouseOverScript, element);
			Thread.sleep(2000);
			int y = element.getLocation().y;
			int y1 = 380; // driver.manage().window().getSize().getHeight();
			this.scrollUp(y1 / 2 - y);
			Thread.sleep(2000);
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * 
	 * @param parent
	 *            Parent elements which contains no of links
	 * @param linkText
	 *            linkText which need to be clicked
	 * @param ignoreSpecialChar
	 *            Specifies compare link text with special chars or not
	 * @return
	 */
	public boolean SubLink_Click(WebElement parent, String linkText, boolean ignoreSpecialChar)
	{
		boolean result = false;
		String str = null;
		try
		{
			List<WebElement> lnkName = parent.findElements(By.tagName("a"));
			for (int x = 0; x < lnkName.size(); x = x + 1)
			{
				str = lnkName.get(x).getText();
				if (str != null)
				{
					if (ignoreSpecialChar && str.trim().replaceAll("[^a-zA-Z0-9]", "").equalsIgnoreCase(linkText.replaceAll("[^a-zA-Z0-9]", "")))
					{
						lnkName.get(x).click();
						result = true;
						break;
					}
					if (!ignoreSpecialChar && str.trim().equalsIgnoreCase(linkText))
					{
						lnkName.get(x).click();
						result = true;
						break;
					}
				}
			}
			Thread.sleep(3000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * Function is used to verify the element is selected in the select drop
	 * down
	 * 
	 * @param element
	 * @param value
	 * @return
	 */
	public boolean Select_IsSelected(WebElement element, String value)
	{
		boolean result = false;
		try
		{
			List<WebElement> options = element.findElements(By.tagName("option"));
			for (WebElement option : options)
			{
				if (option.getText().trim().equalsIgnoreCase(value))
				{
					if (option.isSelected() == true)
					{
						result = true;
						break;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * @param by
	 * @return
	 */
	public boolean SetCheckBox(By by)
	{
		boolean result = false;
		try
		{
			for (int i = 0; i < 4; i++)
			{
				if (!driver.findElement(by).isSelected())
					driver.findElement(by).click();
				if (driver.findElement(by).isSelected())
					break;
				else
					Thread.sleep(400);
			}
			result = driver.findElement(by).isSelected();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * @param obj
	 *            Checked box object which needs to be checked
	 * @return
	 */
	public boolean SetCheckBox(WebElement obj)
	{
		boolean result = false;
		try
		{
			for (int i = 0; i < 4; i++)
			{
				
				if (!obj.isSelected())
					obj.click();				
				if (obj.isSelected())
					break;
				else
					Thread.sleep(400);
			}
			result = obj.isSelected();
			
		}
		catch(StaleElementReferenceException e){
			System.out.println("Just ignore this 'StaleElementReferenceException' while Accessing Radio Button object repeatitively");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * @param obj
	 * @param unCheck
	 *            The check box will be unchecked if the value is false
	 *            otherwise it will be checked.
	 * @return
	 */
	public boolean SetCheckBox(WebElement obj, boolean isCheck)
	{
		boolean result = false;
		try
		{
			for (int i = 0; i < 4; i++)
			{
				if (obj.isSelected() != isCheck)
				{
					obj.click();
					Thread.sleep(400);
				}
				else
					break;
			}
			result = obj.isSelected() == isCheck;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean SetRadioButton(By by)
	{
		return this.SetCheckBox(by);
	}

	public boolean SetRadioButton(WebElement obj)
	{
		return this.SetCheckBox(obj);
	}

	public boolean SetRadioButton(WebElement obj, boolean isCheck)
	{
		return this.SetCheckBox(obj, isCheck);
	}

	public boolean DeleteCookies()
	{
		boolean result = false;
		try
		{
			driver.manage().deleteAllCookies();
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean onMouseOver(WebElement element)
	{
		boolean result = false;
		try
		{
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(mouseOverScript, element);
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean onMouseClickUsingJScript(WebElement element)
	{
		boolean result = false;
		try
		{			
			String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initMouseEvent('click', true, false, window,0,0,0,0,0,false,false,false,false,2,null); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { var ev = document.createEventObject();ev.button = 2; arguments[0].fireEvent('onclick',ev);}";
            JavascriptExecutor js = (JavascriptExecutor) driver;
            //this.highlightElement(objAction);
            js.executeScript(mouseOverScript, element);
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	public boolean onMouseClick(WebElement element)
	{
		boolean result = false;
		try
		{
			
			if(System.getProperty("browser").contains("firefox")){
				element.click();
			}else{
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);				
			}
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public boolean onMouseEventClick(WebElement element)
	{
		boolean result = false;
		try
		{		
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	// WebTable functions By Element
	
	public int getRowsWebTableByElement(WebElement objTable, String strRowCol)
	{
		
		String strXPath = "";
		int intCount = 0;
		try
		{				
			strXPath = "tbody/tr" ;			
			//WebElement objTable = driver.findElement(By.id(strTableID));
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
	

	
	
	
	public boolean verifyTextInWebTableByElement(WebElement objTable, String strExpText)
	{
		
		String strGetText = "";
		boolean blnExist = false;

		try
		{		
			//WebElement objTable = driver.findElement(By.id(strTableID));
			strGetText = objTable.getText();
			if(strGetText.toLowerCase().trim().contains(strExpText.toLowerCase())){
				blnExist = true;
			}else{
				blnExist = false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return blnExist;
		}
		return blnExist;
	}
	

	
	
	
	
	public String getCellDataWebTableByElement(WebElement objTable, int intRow, int intCol)
	{
		
		String strXPath = "";
		int intRowCount = 0;
		int intColCount = 0;
		String strData = "";
		try
		{				
			strXPath = "tbody/tr" ;					
		    List <WebElement> rowColl=objTable.findElements(By.xpath(strXPath));
		    intRowCount = rowColl.size();	
		    if(intRow <= intRowCount){
			    for(WebElement trElement : rowColl)
			    {
			        List<WebElement> colColl=trElement.findElements(By.xpath("td"));
			        intColCount = colColl.size();
			        if(intCol <= intColCount){
			        	strData = objTable.findElement(By.xpath("tbody/tr[" + intRow + "]/td[" + intCol + "]")).getText();
				        return strData;
			        }else{
			        	System.out.println("GetCellDataWebTableByElement - Column "+intCol+" Does not Exist");
			        }
			    }		    
		    }else{
		    	System.out.println("GetCellDataWebTableByElement - Row "+intRow+" Does not Exist");
		    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
			strData = "";
		}
		return strData;
	}
	
	
	public int getRowWithTextInTwoColsInWebTableByElement(WebElement objTable, String strText1, int intCol1, String strText2, int intCol2)
	{
		
		String strXPath = "";			
		strText1 = strText1.trim().toLowerCase();
		strText2 = strText2.trim().toLowerCase();
		int intRowNo = 0;
		boolean blnExist = false;
		try
		{				
			strXPath = "tbody/tr" ;	
		    List <WebElement> objRows=objTable.findElements(By.xpath(strXPath)); 		
		    if(objRows.size()> 0 ){		    	
		    	for(WebElement rowElement : objRows)
			    {
		    		intRowNo = intRowNo + 1;		    		
		    		//if(rowElement.findElement(By.xpath("//tr["+intRowNo+"]/td[" + intCol1 + "]")).getText().toLowerCase().equals(strText1) && 
		    		//		rowElement.findElement(By.xpath("//tr["+intRowNo+"]/td[" + intCol2 + "]")).getText().toLowerCase().equals(strText2)){
		    		if(objTable.findElement(By.xpath("tbody/tr["+intRowNo+"]/td[" + intCol1 + "]")).getText().toLowerCase().equals(strText1) && 
		    				objTable.findElement(By.xpath("tbody/tr["+intRowNo+"]/td[" + intCol2 + "]")).getText().toLowerCase().equals(strText2)){
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
	
	public int getRowWithTextInColInWebTableByElement(WebElement objTable, String strText, int intCol)
	{
		
		String strXPath = "";			
		strText = strText.trim().toLowerCase();
		int intRowNo = 0;
		boolean blnExist = false;
		try
		{				
			
			strXPath = "tbody/tr" ;				
		    List <WebElement> objRows=objTable.findElements(By.xpath(strXPath));
		    if(objRows.size()> 0 ){		    	
		    	for(WebElement rowElement : objRows)
			    {	
		    		intRowNo = intRowNo + 1;
		    		//if(rowElement.findElement(By.xpath("td[" + intCol + "]")).getText().toLowerCase().equals(strText)){		    		
		    		if(rowElement.findElement(By.xpath("//tr["+intRowNo+"]/td[" + intCol + "]")).getText().toLowerCase().equals(strText)){
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
		
		/*String strXPath = "";			
		strText = strText.trim().toLowerCase();
		int intRowNo = 0;
		boolean blnExist = false;
		try
		{		
			strXPath = "tbody/tr" ;				
		    List <WebElement> objRows=objTable.findElements(By.xpath(strXPath));		  
		    if(objRows.size()> 0 ){		
		    	//for(WebElement rowElement : objRows){
		    	for(intRowNo = 1; intRowNo <=objRows.size();intRowNo++){		    		
		    		//intRowNo = intRowNo + 1;		    		
		    		//if(rowElement.findElement(By.xpath("//tr["+intRowNo+"]/td[" + intCol + "]")).getText().toLowerCase().equals(strText)){
		    		if(objTable.findElement(By.xpath("tbody/tr["+intRowNo+"]/td[" + intCol + "]")).getText().toLowerCase().equals(strText)){
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
		}*/

	}
	
	/** Get Row from Webtable where the Partial text but should be Unique in the table
	 * @param objTable
	 * @param strText
	 * @param intCol
	 * @return
	 */
	public int getRowWithPartialTextInColInWebTableByElement(WebElement objTable, String strText, int intCol)
	{
		
		String strXPath = "";			
		strText = strText.trim().toLowerCase();
		int intRowNo = 0;
		boolean blnExist = false;
		try
		{				
			
			strXPath = "tbody/tr" ;				
		    List <WebElement> objRows=objTable.findElements(By.xpath(strXPath));
		    if(objRows.size()> 0 ){		    	
		    	for(WebElement rowElement : objRows)
			    {
		    		intRowNo = intRowNo + 1;
		    		if(objTable.findElement(By.xpath("tbody/tr["+intRowNo+"]/td[" + intCol + "]")).getText().toLowerCase().contains(strText)){
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
	public WebElement getObjectWithRowColInWebTableByElement(WebElement objTable, String strObjClass, int intIndex, int intRow, int intCol)
	{
		// intIndex should start from Value 0 and should be an integer
		String strXPath = "";	
		WebElement objReturn = null;
		strObjClass = strObjClass.trim().toLowerCase();
		boolean blnExist = false;
		int intobjNo = 0;
		try
		{				
			//strXPath = "id('"+ strTableID + "')/tbody/tr[" + intRow + "]/td[" + intCol + "]" ;			
			//WebElement objTable = driver.findElement(By.id(strTableID));
			highlightElement(objTable);
			strXPath = "tbody/tr[" + intRow + "]/td[" + intCol + "]" ;
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
			    					System.out.println("GetObjectWithRowColInWebTableByElement Error: No Match for the index value");
			    				}
			    			}
			    		}else{
			    			blnExist = false;
			    			System.out.println("GetObjectWithRowColInWebTableByElement Error: No Match for the objClass ["+ strObjClass +"] value");
			    		}
		    		}
			    }	
		    }else{
		    	blnExist = false;
		    	System.out.println("GetObjectWithRowColInWebTableByElement Error: No Match for the Row and Column");
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
	 * Will wait for an alert to appear for the given time. If it appears,
	 * the method will switch focus to it, and return.
	 * @param waitTime: Time to wait for the alert in seconds
	 * @return
	 */
	public Alert waitForAlert(int waitTime) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		return wait.until(new ExpectedCondition<Alert>(){			
			public Alert apply(WebDriver d) {
				//System.out.println("Alert Present");
				return d.switchTo().alert();
			}});
	}
	

	/**
	 * Verify the TEXT presence on ALERT window and do the action ACCEPT/DISMISS
	 * Action will be done only if expected TEXT Present in ALERT window
	 * 
	 * @param strAction - ACCEPT or DISMISS	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean actionOnAlertWithText(Alert objAlert, String strAction ,String strText) throws Exception {
		boolean result = false;
		if(strText.trim() != "" && objAlert != null){
			if(objAlert.getText().toUpperCase().contains(strText.toUpperCase())){
				if(strAction.toUpperCase().trim().equals("ACCEPT")){
					objAlert.accept();
				}else{
					objAlert.dismiss();
				}		
				waitTime(1);
				result = true;
			}
		}
		return result;		
	}
	
	
	
	/**Return text from Alert and No Action on Alert 
	 * @param objAlert as Alert
	 * @return text on Alert
	 * @throws Exception
	 */
	public String returnTextOnAlert(Alert objAlert) throws Exception {		
		return objAlert.getText();		
	}


	/**
	 * Do the action ACCEPT/DISMISS on ALERT	 *
	 * 
	 * @param strAction - ACCEPT or DISMISS	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean actionOnAlert(Alert objAlert, String strAction) throws Exception {
		boolean result = false;
		if(objAlert != null){
			if(strAction.toUpperCase().trim().equals("ACCEPT")){
				objAlert.accept();
			}else{
				objAlert.dismiss();
			}		
			waitTime(1);
			result = true;			
		}
		return result;		
	}
	
	/**
	 * Close Alert by Acceptance
	 * When the browser session is not closed properly,
	 * an error dialogue is given on the login page. This method is mainly used to 
	 * get rid of it.
	 */
	protected void closeAlert(){
		// sometimes if the browser session is not closed properly an error alert appears
		// we need to check for it for 5 seconds and accept if it appears
		try {
			actionOnAlert(waitForAlert(1) , "ACCEPT");
		} catch (Exception e) {
			System.out.println("No Alert Present");
		}
	}
	
	
	
	
	
	/**
	 * Select value from ListBox
	 * 
	 * @param listBox - WebELement object
	 * @param strValue - Value to select
	 * @return boolean
	 * @throws Exception
	 */
	public boolean selectListOption(WebElement listBox, String strValue){
		boolean blnSelect = false;
		
		try{			
			if(System.getProperty("browser").contains("safari")){
				List <WebElement> options1 = listBox.findElements(By.tagName("optgroup"));
				if(options1.size()!= 0){
					for(WebElement optGrp : options1){		
						List <WebElement> options = optGrp.findElements(By.tagName("option"));					
						for(WebElement option : options){		
							//System.out.println("SelectListOption FOR " +option.getText());
				            if(option.getText().equalsIgnoreCase(strValue)){
				                    option.click();                        
				                    blnSelect = true;
				                    break;
				            }else{
				            	blnSelect = false;
				            }
				        }//for end	
						if(blnSelect){
							break;
						}
					}//for end	
				}else{
					List <WebElement> options = listBox.findElements(By.tagName("option"));
					for(WebElement option : options){		
						//System.out.println("SelectListOption FOR " +option.getText());
			            if(option.getText().equalsIgnoreCase(strValue)){
			                    option.click();                        
			                    blnSelect = true;
			                    break;
			            }else{
			            	blnSelect = false;
			            }
			        }//for end	
				}//options1.size() if-else end				
			}else{
				List <WebElement> options = listBox.findElements(By.tagName("option"));
				for(WebElement option : options){		
					//System.out.println("SelectListOption FOR " +option.getText());
		            if(option.getText().equalsIgnoreCase(strValue)){
		                    option.click();                        
		                    blnSelect = true;
		                    break;
		            }else{
		            	blnSelect = false;
		            }
		        }//for end	
			}
		}catch(Exception e){
			System.out.println("ERROR: SelectListOption " + strValue);
			e.printStackTrace();
			blnSelect = false;
		}
		return blnSelect;
	}
	
	
	public boolean switchWindowByTitle(String strText) {
		boolean result = false;
	    WebDriver browserWindow = null;
	    waitTime(3);
	    Iterator<String> windowIterator = driver.getWindowHandles()
	            .iterator();
	    while (windowIterator.hasNext()) {
	        String windowHandle = windowIterator.next();
	        browserWindow = driver.switchTo().window(windowHandle);
	        String strTitle = browserWindow.getTitle();	
	        //System.out.println(strTitle);
	        if (strTitle.contains(strText)) {
	        	driver.manage().window().maximize();
	        	result = true;
	            break;
	        }
	    }
	    return result;
	}
	
	public void switchWindowByURL(String strText) {
	    WebDriver browserWindow = null;
	    Iterator<String> windowIterator = driver.getWindowHandles()
	            .iterator();
	    while (windowIterator.hasNext()) {
	        String windowHandle = windowIterator.next();
	        browserWindow = driver.switchTo().window(windowHandle);
	        String strTitle = browserWindow.getCurrentUrl();
	       // System.out.println(strTitle);
	        if (strTitle.contains(strText)) {
	            break;
	        }
	    }
	}
	
	
	public void closeWindowByTitle(String strTitle) {
	    WebDriver browserWindow = null;	   
	    Iterator<String> windowIterator = driver.getWindowHandles()
	            .iterator();
	    while (windowIterator.hasNext()) {
	        String windowHandle = windowIterator.next();
	        browserWindow = driver.switchTo().window(windowHandle);
	        String strGetTitle = browserWindow.getTitle();	       
	        if (strGetTitle.contains(strTitle)) {
	        	driver.close();
	            break;
	        }
	    }//while end
	    
	    //Control back to last Browser session
	    for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
		}
	}
	
	
	public void exitTestOnFail() throws Exception{
		throw new Exception();
	}
	
	
}// class end here
