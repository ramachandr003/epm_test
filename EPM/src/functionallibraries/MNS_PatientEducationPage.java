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
public class MNS_PatientEducationPage extends MasterPage
{
	
	//Initialize the other classes
	EPM_LoginPage loginPage = new EPM_LoginPage(scriptHelper);
	EPM_HomePage homePage = new EPM_HomePage(scriptHelper);
	EPM_SkillsPage skillsPage = new EPM_SkillsPage(scriptHelper);
	MNS_HomePage mnsHomePage =  new MNS_HomePage(scriptHelper);
	MNS_SkillsPage mnsSkillsPage =  new MNS_SkillsPage(scriptHelper);
	Common common = new Common(scriptHelper);	
	
	
	// UI Map object definitions
	
		
	//for smoke test script
	public String txtSearchSkills = "ctl00_txtUcdSearch";
	public String btnGo_SearchSkill = "ctl00_btnSearch";
	public String linkSearchMNC = "ctl00_hypSearchMnc";
	public String linkSkillsTab = "link=Skills";
	public String linkPatientEducationTab = "link=Patient Education";
	public String linkAllConditionsAndTreatments = "ctl00_lnkAllConditions";
	public String linkAllDrugs = "ctl00_lnkAllMedications";
	public String linkAllEDDischarge = "ctl00_lnkAllEdDischarge";
	public String pnlConditionsTreatmentsLinks = "//div[@id='ucd-id-conditions']";
	public String tblPatientEductaionSearchResults = "ctl00_ContentMain_rdPatientEducationList_ctl00"; //Row start from 1
	public String contentPatientEducationEnglish = "ctl00_ContentMain_PatientEducationDocumentsCtl1_eng";
	public String contentPatientEducationSpanish ="ctl00_ContentMain_PatientEducationDocumentsCtl1_spa";
	public String linkPrint ="//a[@title='Print' and @class='iconPrint']";
	public String contentPatientEducation = "ctl00_ContentMain_PatientEducationDocumentsCtl1_Handouts";
	
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
	public MNS_PatientEducationPage(ScriptHelper scriptHelper)
	{
		super(scriptHelper);
	}

	@Override
	protected void initializeUIMap()
	{

	}
	


	//Smoke test Scripts
	public boolean verifyPatientEducationHomePage() throws Exception{		
		boolean result = false;			
		if(common.verifyPageNameByH1TagHeader("Patient Education Home")){
			report.updateTestLog("Verify the Patient Education Home Page displays",  "Patient Education Home Page is displayed", Status.PASS);	
			result = true;	
		}else{
			report.updateTestLog("Verify the Patient Education Home Page displays",  "Patient Education Home Page is NOT displayed", Status.FAIL);
			result = false;	
		}    
		return result;
	}	
	
	public void selectPatientEducationSubTabByName(String strPESubTabName) throws Exception{		
		String strElementLocator = "";
		if(strPESubTabName.trim().toUpperCase().equals("CONDITION TREATMENT")){
			strPESubTabName = "Conditions & Treatments";
		}else if(strPESubTabName.trim().toUpperCase().equals("ED DISCHARGE")){
			strPESubTabName = "ED Discharge";
		}else if(strPESubTabName.trim().toUpperCase().equals("DRUG")){
			strPESubTabName = "Medications";
		}
		strElementLocator = "//a[@title = '"+strPESubTabName +"']";
		validateElementPresence(strPESubTabName + "Sub tab", strElementLocator).click();	
		report.updateTestLog("Select "+strPESubTabName+" sub tab of Patient Education",  "'" + strPESubTabName +"' sub tab is Selected.", Status.DONE);
	}	
	
	/**To CLick Specialty link under PE tab
	 * @param strSpecialty  - if strSpecialty is empty string then first strSpecialty will be clicked by default
	 * @throws Exception
	 */
	public void clickConditionTreatmentLinkBySpecialty(String strSpecialty) throws Exception{		
		String strElementLocator = "";
		if(!strSpecialty.trim().equals("")){
			strElementLocator = pnlConditionsTreatmentsLinks + "/a[@title='" + strSpecialty.trim() + "']";
		}else{
			strElementLocator = pnlConditionsTreatmentsLinks + "/a[1]";
		}		
		validateElementPresence("Specialty link " + strSpecialty, strElementLocator).click();	
		report.updateTestLog("Click any link under the Condition Treatment tab",  "Condition Treatment Specialty link is clicked", Status.DONE);
	}	
	
	public boolean verifyPatientEducationSearchPage() throws Exception{		
		boolean result = false;		    
		if(validateElementPresence("Patient Eductaion Search Results", tblPatientEductaionSearchResults)!=null && 
				common.verifyPageNameByH1TagHeader("Patient Education Search")){
			report.updateTestLog("Verify the Patient Education Search Page displays",  "Patient Education Search Page is displayed", Status.PASS);	
			result = true;	
		}else{
			report.updateTestLog("Verify the Patient Education Search Page displays",  "Patient Education Search Page is NOT displayed", Status.FAIL);
			result = false;	
		}    
		return result;
	}	
	
	/**To Click Title link under PE Search results page
	 * @param strTitle - if strTitle is empty string then first title will be clicked by default
	 * @return
	 * @throws Exception
	 */
	public boolean clickTitleInPatientEducationSearchResultsTableByTitle(String strTitle) throws Exception{		
		boolean result = false;	
		int intTitleRow =  -1;
		if(validateElementPresence("Patient Eductaion Search Results", tblPatientEductaionSearchResults)!=null){
			if(strTitle.trim().equals("")){
				intTitleRow = 1;
				strTitle = getCellDataWebTableByID(tblPatientEductaionSearchResults, intTitleRow, 1);
			}else{
				intTitleRow = getRowWithTextInWebTableByID(tblPatientEductaionSearchResults, strTitle);
			}			
			if(intTitleRow != -1){
				WebElement objTitle = getObjectWithRowColInWebTableByID(tblPatientEductaionSearchResults, "a", 0, intTitleRow, 1);
				if(objTitle != null){
					onMouseClick(objTitle);
					result = true;
					report.updateTestLog("Click Title '" + strTitle + "' in PatientEducation Search Results table.",  "Title is clicked", Status.DONE);
				}else{
					result = false;	
					report.updateTestLog("Click Title '" + strTitle + "' in PatientEducation Search Results table.",  "Title is NOT clicked", Status.FAIL);
				}
			}else{
				result = false;	
				report.updateTestLog("Click Title '" + strTitle + "' in PatientEducation Search Results table.",  "Title does NOT exist", Status.FAIL);
			}			
		}else{			
			result = false;	
			report.updateTestLog("Click Title '" + strTitle + "' in PatientEducation Search Results table.",  "PatientEducation Search Results table does NOT exist", Status.FAIL);
		} 
		return result;
	}
	
	
	public boolean verifyPatientEducationDocumentContentPage() throws Exception{
		boolean result = false;
		if(validateElementPresence("Patient Education content Document in English", contentPatientEducationEnglish)!=null &&
				common.verifyPageNameByH1TagHeader("Patient Education")){
			report.updateTestLog("Verify the Patient Education Document Content Page displays",  "Patient Education Document Content Page is displayed", Status.PASS);	
			result = true;	
		}else{
			report.updateTestLog("Verify the Patient Education Document Content Page displays",  "Patient Education Document Content Page is NOT displayed", Status.FAIL);
			result = false;			
		}
		return result;
	}
	
	public void clickPrintIconInPatientEducationPage()  throws Exception {
		if(System.getProperty("browser").equalsIgnoreCase("iexplore")){
			validateElementPresence("Print link on PE content page" , linkPrint).click();		
			report.updateTestLog("Click Print link to print the Patient Eductaion page",  "Print link is clicked", Status.DONE);
			handlePrintWindow();
		}
	}
	
	public void clickAllDrugsLink()  throws Exception {
		onMouseClick(validateElementPresence("All Drugs Link" , linkAllDrugs));
		//validateElementPresence("All Drugs Link" , linkAllDrugs).click();	
		report.updateTestLog("Click 'All Drugs' Link",  "All Drugs Link is clicked", Status.DONE);
	}
	public void clickAllEDDischargeLink()  throws Exception {
		onMouseClick(validateElementPresence("AllEDDischarge Link" , linkAllEDDischarge));
		//validateElementPresence("AllEDDischarge Link" , linkAllEDDischarge).click();	
		report.updateTestLog("Click 'All ED Discharge' Link",  "All ED Discharge Link is clicked", Status.DONE);
	}
	
}//class end