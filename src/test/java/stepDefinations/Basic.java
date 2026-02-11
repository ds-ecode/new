package stepDefinations;

import org.junit.Assert;
import factory.BaseClass;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.HomePage;
import pageObjects.UpcomingBikes;

public class Basic {
    
    HomePage hp;
    UpcomingBikes ub;
    
    @Given("User navigates to Home Page")
    public void user_navigates_to_home_page() {
        // Validation of navigation is handled in Hooks (@Before)
        BaseClass.getLogger().info("User is on the Home Page");
    }

    @When("user goes to New Bikes and Clicks Upcoming Bikes")
    public void user_goes_to_new_bikes_and_clicks_upcoming_bikes() {
        hp = new HomePage(BaseClass.getDriver());
        hp.selectUpcomingBikes(); 
    }

    @When("user selects the manufacturer {string}")
    public void user_selects_the_manufacturer(String manufacturer) {
        ub = new UpcomingBikes(BaseClass.getDriver());
        // Using the dynamic method to click any brand name
        ub.selectBrand(manufacturer);
    }

    @Then("user sees the {string} upcoming bikes page")
    public void user_sees_the_upcoming_bikes_page(String manufacturer) {
        String actualMsg = ub.getMsg();
        BaseClass.getLogger().info("Verifying page header for: " + manufacturer);
        
        // Dynamic assertion: checks if the header contains the brand name
        Assert.assertEquals("Upcoming " + manufacturer +" Bikes", actualMsg);
    }

    @Then("user exports filtered bikes to excel for {string}")
    public void user_exports_filtered_bikes_to_excel_for(String manufacturer) {
        // Passing the brand name to the utility so the Excel sheet is named correctly
        ub.exportBikesToExcel(manufacturer);
        BaseClass.getLogger().info("Data export completed for: " + manufacturer);
    }
}