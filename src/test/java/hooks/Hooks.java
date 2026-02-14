package hooks;

import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.Logger;
import factory.BaseClass;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    WebDriver driver;
    Logger logger;

    @Before
    public void setup() throws IOException {
    	
    	logger = BaseClass.getLogger(); // Initialize the logger
        logger.info("***** Starting Scenario execution *****");
        
        driver = BaseClass.initilizeBrowser();
        Properties p = BaseClass.getProperties();
        driver.get(p.getProperty("appURL"));
        driver.manage().window().maximize();
        
        logger.info("Browser launched and navigated to URL.");
    }

    @After
    public void tearDown(Scenario scenario) {
        // If the scenario fails, take a screenshot
        if (scenario.isFailed()) 
        {
        	logger.error("Scenario FAILED: " + scenario.getName());
        	
			try {
				// Casting driver to TakesScreenshot
				TakesScreenshot ts = (TakesScreenshot) driver;

				// Capture screenshot as byte array (best for Cucumber reports)
				byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);

				// Attach to the Cucumber report
				scenario.attach(screenshot, "image/png", scenario.getName());

			} catch (Exception e) {
				System.out.println("Exception while taking screenshot: " + e.getMessage());
			}
        }
        else
        {
        	logger.info("Scenario PASSED: " + scenario.getName());
        }
        
        logger.info("***** Ending Scenario execution *****");
        
        if (driver != null) {
            driver.quit();
        }
    }
}