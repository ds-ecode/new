package factory;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BaseClass 
{
    static WebDriver driver;
    static Properties p;
    static Logger logger;

    public static WebDriver initilizeBrowser() throws IOException {
        p = getProperties();
        String browser = p.getProperty("browser").toLowerCase();

        // Direct switch for local browser instantiation
        switch (browser) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            default:
                System.out.println("No matching browser found in config.properties");
                return null;
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10)); // Increased slightly for stability
        driver.manage().window().maximize();

        return driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static Properties getProperties() throws IOException {
        FileReader file = new FileReader(System.getProperty("user.dir") + "\\src\\test\\resources\\config.properties");
        p = new Properties();
        p.load(file);
        return p;
    }

    public static Logger getLogger() 
    {   
        logger = LogManager.getLogger(BaseClass.class); 
        return logger;
    }
}