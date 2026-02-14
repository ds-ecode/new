package stepDefinations;

import org.junit.Assert;

import factory.BaseClass;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.HomePage;
import pageObjects.UpcomingBikes;
import pageObjects.UsedCarsPage;

public class UsedCars 
{
	HomePage hp;
    UpcomingBikes ub;
    UsedCarsPage uc;
    
	@Given("User navigates to HomePage")
	public void user_navigates_to_home_page() {
		BaseClass.getLogger().info("User is on the Home Page");
	}

	@When("User navigates to Used Cars page")
	public void user_navigates_to_used_cars_page() {
		hp = new HomePage(BaseClass.getDriver());
        hp.selectUsedCars();
	}
	
	@And("user selects the city {string}")
	public void user_selects_the_city(String cityName)
	{
		uc = new UsedCarsPage(BaseClass.getDriver());
	    uc.selectCity(cityName);
	}
	
	@Then("user exports filtered used cars to excel for {string}")
	public void user_exports_filtered_used_cars_to_excel_for(String cityName)
	{
		uc.filterByAllPopularModels();
	    uc.scrollAndExtract(cityName);
	    BaseClass.getLogger().info("Extraction complete for city: " + cityName);
	}
	
	@Then("checks if thats Used cars page or not {string}")
	public void checks_if_thats_used_cars_page_or_not(String cityName) {
	    String actual = uc.getMsg();
	    Assert.assertEquals("Used Cars in " + cityName ,actual);
	}
	
	
}
