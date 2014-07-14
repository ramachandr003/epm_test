package supportlibraries;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.safari.SafariDriver;

import com.cognizant.framework.FrameworkParameters;
import com.opera.core.systems.OperaDriver;

/**
 * Factory for creating the Driver object based on the required browser
 * 
 * @author Cognizant
 * @version 3.0
 * @since October 2011
 */
public class WebDriverFactory
{
	/**
	 * Function to return the appropriate {@link RemoteWebDriver} object based
	 * on the browser name passed
	 * 
	 * @param browser
	 *            The name of the browser
	 * @return The corresponding {@link RemoteWebDriver} object
	 */
	private static FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

	public static final String MAXIMIZE_BROWSER_WINDOW = "if (window.screen) {window.moveTo(0, 0);window.resizeTo(window.screen.availWidth,window.screen.availHeight);};";

	public static Object maximizeBrowserWindow(WebDriver driver)
	{
		return executeJavascript(driver, MAXIMIZE_BROWSER_WINDOW);
	}

	private static Object executeJavascript(WebDriver driver, String script)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return js.executeScript(script);
	}

	public static RemoteWebDriver getDriver(Browser browser)
	{
		WebDriver driver = null;
		switch(browser)
		{
		case chrome:
			if(System.getProperty("os.name").contains("Windows"))
				System.setProperty("webdriver.chrome.driver", frameworkParameters.getRelativePath() + "\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments(Arrays.asList("--start-maximized"));
			options.addArguments("test-type");
			driver = new ChromeDriver(options);
			break;
		case firefox:
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			break;
		case htmlunit:
			driver = new HtmlUnitDriver();
			break;
		case safari:
			driver = new SafariDriver();
			driver.manage().window().maximize();
			break;
		case iexplore:
			if(System.getProperty("os.name").contains("Windows"))
			{
				if(System.getProperty("os.arch").indexOf("64") != -1)
					System.setProperty("webdriver.ie.driver", frameworkParameters.getRelativePath() + "\\IEDriverServer_64.exe");
				else
					System.setProperty("webdriver.ie.driver", frameworkParameters.getRelativePath() + "\\IEDriverServer_32.exe");
			}
			driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
			break;
		case opera:
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("opera.port", -1);
			caps.setCapability("opera.profile", "");
			driver = new OperaDriver(caps);
			break;
		default:
			break;
		}
		
		System.setProperty("browser",browser.toString());
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		if (browser != Browser.chrome && browser != Browser.safari)
			driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);

		// maximizeBrowserWindow(driver);
		return (RemoteWebDriver) driver;
	}
}