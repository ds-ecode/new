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
	
	@FindBy(xpath = "//li[contains(@class,'modelItem')]")
	List<WebElement> bikeList;
	
	public String getMsg()
	{
		return confirmMsg.getText();
	}
		
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
	
	public void exportToExcel(String brandName) {
	    List<String[]> excelData = new ArrayList<>();
	    // Headers must match your image exactly
	    String[] headers = {"TimeStamp", "Bike Name", "Price", "Launch Date"};
	    
	    for (WebElement bike : bikeList) {
	        try {
	            String name = bike.findElement(By.xpath(".//a[@data-track-label='model-name']/strong")).getAttribute("textContent").trim();
	            String priceText = bike.getAttribute("data-price").trim();
	            String numericPrice = priceText.replaceAll("[^0-9]", ""); 
	            String date = bike.findElement(By.xpath(".//div[contains(@class,'clr-try')]")).getAttribute("textContent").replace("Expected Launch :", "").trim();

	            if (parsePrice(priceText) <= 400000) {
	                // First element is placeholder for TimeStamp (handled in Utility)
	                excelData.add(new String[]{"", name, numericPrice, date});
	            }
	        } catch (Exception e) { continue; }
	    }
	    
	    try {
	        ExcelUtils.writeToExcel("./testData/UpcomingBikes.xlsx", brandName, excelData, headers);
	        logger.info("Excel generated for " + brandName + " with TimeStamp column.");
	    } catch (Exception e) { e.printStackTrace(); }
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
