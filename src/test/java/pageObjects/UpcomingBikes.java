package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import factory.BaseClass;
import utilities.ExcelUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

public class UpcomingBikes extends BasePage
{
	Logger logger = BaseClass.getLogger();
	
	public UpcomingBikes(WebDriver driver)
	{
		super(driver);
	}
	
	@FindBy(xpath="//h1[contains(text(),'Upcoming')]")
	WebElement confirmMsg;
	
//	@FindBy(xpath = "//a[normalize-space()='Royal Enfield']")
//    WebElement brandRoyalEnfield;
	
	@FindBy(xpath = "//li[contains(@class,'modelItem')]")
	List<WebElement> bikeList;
	
	public String getMsg()
	{
		return confirmMsg.getText();
	}
	
//	public void selectRoyalEnfieldBrand() {
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//	    try {
//	        // Scroll so you can see it in the demo
//	        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", brandRoyalEnfield);
//	        Thread.sleep(1000); // Give the site a moment to stabilize
//	        
//	        // Use JS click immediately to avoid interception
//	        js.executeScript("arguments[0].click();", brandRoyalEnfield);
//	        logger.info("Successfully clicked Royal Enfield....");
//	        
//	    } catch (Exception e) {
//	        logger.error("Could not click Royal Enfield brand link: " + e.getMessage());
//	    }
//	}
	
	// Replace the hardcoded brandRoyalEnfield WebElement with this method
	public void selectBrand(String brandName) {
	    // Dynamic XPath to find any brand name link
	    WebElement brandLink = driver.findElement(By.xpath("//a[normalize-space()='" + brandName + "']"));
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    try {
	        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", brandLink);
	        Thread.sleep(1000); 
	        js.executeScript("arguments[0].click();", brandLink);
	        logger.info("Successfully clicked " + brandName);
	    } catch (Exception e) {
	        logger.error("Could not click brand: " + brandName);
	    }
	}

	
	// Update this method in your UpcomingBikes class
	public void exportBikesToExcel(String brandName) {
	    List<String[]> excelData = new ArrayList<>();
	    
	    for (WebElement bike : bikeList) {
	        try {
	            // Your existing scraping logic here...
	            String name = bike.findElement(By.xpath(".//a[@data-track-label='model-name']/strong")).getAttribute("textContent").trim();
	            String priceText = bike.getAttribute("data-price").trim();
	            String dateRaw = bike.findElement(By.xpath(".//div[contains(@class,'clr-try')]")).getAttribute("textContent").trim();
	            String date = dateRaw.replace("Expected Launch :", "").trim();

	            double priceValue = parsePrice(priceText);

	            if (priceValue == 0) {
	                excelData.add(new String[]{name, "Price To Be Announced", date});
	            }
	            else if(priceValue > 0 && priceValue <= 400000)
	            {
	            	excelData.add(new String[]{name, priceText, date});
	            }
	        } catch (Exception e) {
	            continue;
	        }
	    }

	    try {
	        // Now passing the dynamic 'brandName' for the sheet name
	        ExcelUtils.writeToExcel("./testData/UpcomingBikes.xlsx", brandName, excelData);
	        logger.info("Excel sheet created for: " + brandName);
	    } catch (Exception e) {
	        logger.error("Excel Export Failed for " + brandName + ": " + e.getMessage());
	    }
	}
	
	// Helper method to turn "Rs. 3.50 Lakh" into 3.50
    private double parsePrice(String price) {
    	try {
            // Removes "Rs.", "Lakh", spaces, and commas
            String cleaned = price.replaceAll("[^0-9.]", "");
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            return 0.0; // For cases like "TBD"
        }
    }
}
