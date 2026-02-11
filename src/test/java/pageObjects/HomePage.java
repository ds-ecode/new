package pageObjects;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.Logger;
import factory.BaseClass;

public class HomePage extends BasePage
{
	Logger logger = BaseClass.getLogger();
	public HomePage(WebDriver driver)
	{
		super(driver);
	}
	
	@FindBy(xpath="//span[normalize-space()='NEW BIKES']")
	WebElement newBikes_icon;
	
	@FindBy(linkText ="Upcoming Bikes")
	WebElement upcoming_Bikes;
	
	public void selectUpcomingBikes() {
		
		logger.info("Hovering over 'New Bikes' menu...");
		
        Actions actions = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Hover over the main menu
        wait.until(ExpectedConditions.visibilityOf(newBikes_icon));
        actions.moveToElement(newBikes_icon).perform();

        // Wait for dropdown item to be visible and click it
        wait.until(ExpectedConditions.elementToBeClickable(upcoming_Bikes));
        
        logger.info("Clicking on 'Upcoming Bikes' option...");
        upcoming_Bikes.click();
    }

}
