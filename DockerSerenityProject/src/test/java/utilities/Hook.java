package utilities;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.webdriver.DriverSource;
//**CustomDriver:-> Hook implements DriverSource
public class Hook extends PageObject{
	private static WebDriver driver;
	public static final String CHROME_DRIVER="webdriver.chrome.driver";
	public static final String CHROME_DRIVER_PATH="./Drivers/chromedriver.exe";
	
	
	
	@Before
	public void setUp(){
		//**CustomDriver:-> System.setProperty("webdriver.chrome.driver","./Drivers/chromedriver");
		FileInputStream finput;
		try{
			finput=new FileInputStream("serenity.properties");
			Properties prop=new Properties(System.getProperties());
			prop.load(finput);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		//**CustomDriver:-> driver=new ChromeDriver();
		DesiredCapabilities cap=DesiredCapabilities.firefox();
		cap.setCapability("platform", "LINUX");
		try {
			//driver=new RemoteWebDriver(new URL("http://35.202.106.116:4444/wd/hub"), cap);
			driver=getDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Base.driver=driver;
		System.out.println(driver);
	}
	
	@After
	public void tearDown(Scenario scenario){
		if(scenario.isFailed()){
			//take screenshoot
			try{
				byte[] screenShoot=((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
				//scenario.embed(screenShoot, "image/png");
				//scenario.write("Url At failure"+driver.getCurrentUrl());
			}catch(WebDriverException e){
				scenario.write("Emebed Failed "+e.getMessage());
				e.printStackTrace();
			}
		}
		if(driver!=null)
			driver.quit();
		
	}
	public WebDriver newDriver() {
		return Base.driver;
	}

	public boolean takesScreenshots() {
		return true;
	}

}
