package functionallibraries;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import supportlibraries.*;

import com.cognizant.framework.*;

/**
 * Skills Page for MNS
 * 
 * @author Ramachandr003
 */
public class MNS_SkillsPage extends MasterPage
{
	
	//Other Class Instances
	Common common = new Common(scriptHelper);	
	
	// UI Map object definitions
	
	public String tblSkillsSearchResults = "ctl00_ContentMain_ElsevierGrid1_RadGrid1_ctl00"; //Starts from Row 1
	public String headerText_SkillName = "//ul[@id='ulLinks']/preceding-sibling::h2";
	public String headerText_SkillContentSubTab= "//span[@id='spSubmitContentComments']/..";
	public String secAlertContent = "ctl00_ContentMain_SkillPlayer1_divAlert";
	public String secDemoMediaContent = "ctl00_ContentMain_SkillPlayer1_divMedia";
	public String linkAnimatedDemoContent = "//div[@id='"+secDemoMediaContent+"']/a[contains(@href,'/Content/Animations')]";
	public String secIllustrationsContent = "ctl00_ContentMain_SkillPlayer1_divIllustrations";
	public String linkIllustrationsImage1 = "//div[@id='"+secIllustrationsContent+"']/a[1]";
	public String swfAnimatedPlayer = "//object/embed";
	public String btnCompleteTest = "ctl00_ContentMain_SkillPlayer1_btnTestSubmit";
	public String secTestNoteInRed = "ctl00_ContentMain_SkillPlayer1_lblSelfAssign";
	public String textToVerifyInRed = "Note: When you complete this test, the score will be recorded as Self-Assigned and will not count toward any Task completion requirements.";


	//*******************************************************************************************************
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
	public MNS_SkillsPage(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
	}

	@Override
	protected void initializeUIMap()
	{

	}
	
	//Smoke test scripts
	public void clickSkillInSkillsSearch(String strSkillName) throws Exception{				
		int intSkillRow = getRowWithTextInColInWebTableByID(tblSkillsSearchResults, strSkillName, 1);
		if(intSkillRow!= -1){
			WebElement objLink = getObjectWithRowColInWebTableByID(tblSkillsSearchResults, "a", 0, intSkillRow, 1);
			if(objLink!= null){
				objLink.click();
				report.updateTestLog("Click Skill Name '"+ strSkillName + "' ", "Skill Link is clicked", Status.DONE);
			}
		}else{
			report.updateTestLog("Click Skill Name '"+ strSkillName + "' ", "Skill Does Not Exist", Status.FAIL);
		}					
	}
	
	
	public void clickAnimatedContentLinkDemo() throws Exception{		
		WebElement linkDemo = validateElementPresence("Link Demo- Animated Content", linkAnimatedDemoContent);
		if(linkDemo!=null){
			onMouseClick(linkDemo);
			report.updateTestLog("Click Demo Animated Content '"+ linkDemo.getText() + "' ", "Demo Animated Content Link is clicked", Status.DONE);
		}else{
			report.updateTestLog("Click Demo Animated Content", "Demo Animated Content Link Does Not Exist", Status.FAIL);
		}							
	}
	
	
	public void clickAnyIllustarationImage() throws Exception{	
		
		WebElement imgIllustartion = validateElementPresence("Illustration Image", linkIllustrationsImage1);
		if(imgIllustartion!=null){
			onMouseClick(imgIllustartion);
			report.updateTestLog("Click Illustration Image Content", "Illustration Image Content Link is clicked", Status.DONE);
		}else{
			report.updateTestLog("Click Illustration Image Content", "Illustration Image Content Link Does Not Exist", Status.FAIL);
		}							
	}
	
	public boolean verifySkillContentPageDisplayed(String strSkillName) throws Exception{		
		boolean result = false;		
		if(common.verifyPageNameByH1TagHeader("Skill Content") && 
				validateElementPresence("Skill Name header in Content page", headerText_SkillName).getText().equalsIgnoreCase(strSkillName)){
			result = true;	
		}else{
			result = false;	
		}    
		return result;
	}
	public boolean verifyAnimatedPlayerOpenInNewWindow() throws Exception{		
		boolean result = false;	
		switchWindowByTitle("Animation Player");	
		if(driver.getTitle().trim().equalsIgnoreCase("Animation Player")){
			result = true;
			/*if(verify Swf embed object exists){
				result = true;
			}else{
				result = false;	
			}*/				
		}else{
			result = false;	
		}    
		closeWindowByTitle("Animation Player");
		return result;
	}
	
	public boolean verifyIllustrationImageOpenInNewWindow() throws Exception{		
		boolean result = false;	
		switchWindowByTitle("Show Illustration : Mosby's Skills");	
		if(driver.getTitle().trim().equalsIgnoreCase("Show Illustration : Mosby's Skills")){
			result = true;
			/*if(verify image object exists){
				result = true;
			}else{
				result = false;	
			}*/				
		}else{
			result = false;	
		}    
		closeWindowByTitle("Show Illustration : Mosby's Skills");
		return result;
	}
	
	public boolean selectSkillContentSubTab(String strSubTabName) throws Exception{		
		boolean result = false;
		String strElementLocator = "";
		if(strSubTabName.trim().toUpperCase().equals("QUICK SHEET")){
			strSubTabName = "Quick Sheet";
		}else if(strSubTabName.trim().toUpperCase().equals("EXTENDED TEXT")){
			strSubTabName = "Extended Text";
		}else if(strSubTabName.trim().toUpperCase().equals("SUPPLIES")){
			strSubTabName = "Supplies";
		}else if(strSubTabName.trim().toUpperCase().equals("DEMOS")){
			strSubTabName = "Demos";
		}else if(strSubTabName.trim().toUpperCase().equals("ILLUSTRATIONS")){
			strSubTabName = "Illustrations";
		}else if(strSubTabName.trim().toUpperCase().equals("TEST")){
			strSubTabName = "Test";
		}else if(strSubTabName.trim().toUpperCase().equals("CHECKLIST")){
			strSubTabName = "Checklist";
		}
		strElementLocator = "//a[@title = 'Browse to "+strSubTabName +"']/img";
		
        clickElement(strElementLocator); //direct click only enable popup in IE
        //onMouseClick(validateElementPresence(strSubTabName, strElementLocator));
        waitTime(1);
        if(strSubTabName.equals("Test")){
        	actionOnAlert(waitForAlert(5), "ACCEPT");
        }
		if(validateElementPresence("Skill Content Sub Tab - "+strSubTabName , headerText_SkillContentSubTab).getText().trim().toLowerCase().contains(strSubTabName.toLowerCase())){
			result = true;	
		}else{
			result = false;	
		}    
		return result;
	}
	
	public boolean verifySkillContentTabSelection(String strSubTabName, String strAlertContent) throws Exception{		
		boolean result = false;	       
		if(validateElementPresence("Skill Content Sub Tab - "+strSubTabName , headerText_SkillContentSubTab).getText().trim().toLowerCase().contains(strSubTabName.toLowerCase())){
			result = true;	
		}else{
			result = false;	
		}    
		return result;
	}	
	
	
	public boolean verifyAlertContentInEachSubTab(String strAlertContent) throws Exception{		
		boolean result = false;			
		if(validateElementPresence("Alert Content" , secAlertContent).getText().trim().toLowerCase().contains(strAlertContent.toLowerCase())){
			result = true;	
		}else{
			result = false;	
		}    
		return result;
	}	
	
	
	
	public boolean verifySkillContentSelectedSubTab(String strSubTabName) throws Exception{		
		boolean result = false;	       
		String strElementLocator = "";
		if(strSubTabName.trim().toUpperCase().equals("QUICK SHEET")){
			strSubTabName = "Quick Sheet";
		}else if(strSubTabName.trim().toUpperCase().equals("EXTENDED TEXT")){
			strSubTabName = "Extended Text";
		}else if(strSubTabName.trim().toUpperCase().equals("SUPPLIES")){
			strSubTabName = "Supplies";
		}else if(strSubTabName.trim().toUpperCase().equals("DEMOS")){
			strSubTabName = "Demos";
		}else if(strSubTabName.trim().toUpperCase().equals("ILLUSTRATIONS")){
			strSubTabName = "Illustrations";
		}else if(strSubTabName.trim().toUpperCase().equals("TEST")){
			strSubTabName = "Test";
		}else if(strSubTabName.trim().toUpperCase().equals("CHECKLIST")){
			strSubTabName = "Checklist";
		}
		strElementLocator = "//a[@title = 'Browse to "+strSubTabName +"']/..";
		if(validateElementPresence("Skill Content Sub tab " , strElementLocator).getAttribute("class").trim().toLowerCase().contains("selected")){
			result = true;	
		}else{
			result = false;	
		}  
		return result;
	}
	
	public boolean verifyNoteInRedFontInTestSubTab() throws Exception{		
		boolean result = false;		
		WebElement objRedNote = validateElementPresence("RED Note in Test Tab" , secTestNoteInRed);
		if(objRedNote.getText().trim().toLowerCase().contains(textToVerifyInRed.toLowerCase()) && objRedNote.getAttribute("style").contains("red")){
			result = true;	
		}else{
			result = false;	
		}    
		return result;
	}	
	
	public void clickCompleteTestButton() throws Exception{				
		clickElement(btnCompleteTest);
		report.updateTestLog("Click 'Complete The Test' Button", "'Complete Test' Button is clicked", Status.DONE);
	}	
	
	/**
	 * @return
	 * @throws Exception
	 */
	public boolean verifyRedXMarkAppearToEachQuestion() throws Exception{		
		boolean result = false;
		
		List<WebElement> questions = driver.findElements(By.xpath("//*[@id='ctl00_ContentMain_SkillPlayer1_divTestDetails']/b[contains(text(),'Question')]"));
		List<WebElement> wrongAnswers = driver.findElements(By.xpath("//img[@alt='Wrong Answer']"));
		if(questions.size()-1  == wrongAnswers.size()){
			result = true;	
		}else{
			result = false;	
		}    
		return result;
	}	
	
	/** Verify the presence of Skill Name in Search Results table
	 * @param strSkillName
	 * @return
	 * @throws Exception
	 */
	public boolean verifySkillInSearchResultsByName(String strSkillName) throws Exception{			
		return verifyTextInWebTableByID(tblSkillsSearchResults,strSkillName);
	}	
}