package testRunner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {".//features//basic.feature",".//features//usedcars.feature"},
    glue = {"stepDefinations","hooks"}, // Ensure this matches your package name!
    plugin= {
					"pretty", "html:reports/myreport.html",   
					"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",				
		     }
)

public class TestRunner {
}