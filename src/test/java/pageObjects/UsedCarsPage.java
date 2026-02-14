package pageObjects;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import factory.BaseClass;
import utilities.ExcelUtils;

public class UsedCarsPage extends BasePage 
{
	
	@FindBy(xpath="//h1[@id='usedcarttlID']")
	WebElement page_verify;
	
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    JavascriptExecutor js = (JavascriptExecutor) driver;

    public UsedCarsPage(WebDriver driver) {
        super(driver);
    }

    // Locators based on your friend's code and your screenshots
    @FindBy(xpath = "//ul[contains(@class,'popularModels')]//input[@type='checkbox']")
    List<WebElement> popularModelCheckboxes;

    @FindBy(xpath = "//a[@data-track-label='Car-name']")
    List<WebElement> carNames;

    @FindBy(xpath = "//span[contains(@class,'zw-cmn-price')]")
    List<WebElement> carPrices;

    @FindBy(id = "thatsAllFolks")
    WebElement endOfPageMessage;
    
    public void selectCity(String cityName) 
    {
        WebElement city = driver.findElement(By.xpath("//a[normalize-space()='" + cityName + "']"));
        city.click();
    }
    
	public String getMsg()
	{
		return page_verify.getText();
	}

    public void filterByAllPopularModels() 
    {
        // Wait for sidebar to be visible as per your friend's logic
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[contains(@class,'popularModels')]")));
        
        for (WebElement cb : popularModelCheckboxes) 
        {
            if (!cb.isSelected()) {
                js.executeScript("arguments[0].click();", cb);
            }
        }
    }
    
    public void scrollUntilEnd(String cityName) {
        BaseClass.getLogger().info("Starting infinite scroll for " + cityName);
        
        while (true) {
            try {
                // Check if the "That's all Folks!!" message is displayed
                if (endOfPageMessage.isDisplayed()) {
                    BaseClass.getLogger().info("Reached the end: 'That's all Folks!!' is visible.");
                    break;
                }
            } catch (Exception e) {
                // If the element isn't even in the DOM yet, keep scrolling
            }

            // Scroll down by 1000 pixels
            js.executeScript("window.scrollBy(0, 1000);");
            
            // Short wait to allow the loader to fetch more results
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
        }
    }

    public void scrollAndExtract(String cityName) 
    {
        
    	scrollUntilEnd(cityName);
        List<String[]> excelData = new ArrayList<>();
        String[] headers = {"TimeStamp", "Car Name", "Price"};

        int count = Math.min(carNames.size(), carPrices.size());

        for (int i = 0; i < count; i++) {
            String name = carNames.get(i).getText().trim();
            String price = carPrices.get(i).getText().trim();
            
            if (!name.isEmpty()) {
                // Applying your alphanumeric cleaning logic
                name = name.replaceAll("[^a-zA-Z0-9 ]", " ").trim();
                // Placeholder "" for TimeStamp handled by your ExcelUtils
                excelData.add(new String[]{"", name, price});
            }
        }

        try {
            ExcelUtils.writeToExcel("./testData/UsedCars.xlsx", cityName, excelData, headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}