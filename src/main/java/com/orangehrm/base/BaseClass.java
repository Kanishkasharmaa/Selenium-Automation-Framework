package com.orangehrm.base;
import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.LoggerManager;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeSuite;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {

    protected static Properties prop;
    protected static WebDriver driver;
    private static ActionDriver actionDriver;
    public static final Logger logger= LoggerManager.getLogger(BaseClass.class);
    @BeforeSuite
    public void loadConfig() throws IOException {
        //load the configuration file
        prop= new Properties();
        FileInputStream fis=new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
        logger.info("config.properties file is loaded");
    }

    @BeforeMethod
    public void setup() throws IOException {
        System.out.println("Setting up the driver for " + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);
        logger.info("Web browser initialized and browser maximized");
        logger.trace("This is a Trace Message");
        logger.error("This is a Error Message");
        logger.debug("This is a Debug Message");
        logger.fatal("This is a fatal message");
        logger.warn("This a warn message");
        //initiallize the action driver only once
        if(actionDriver==null){
            actionDriver=new ActionDriver(driver);
            logger.info("Action driver is created");

        }
    }

    private void launchBrowser(){
        //Intialize the browser based on browser defined in config.properties file
        String browser= prop.getProperty("browser");
        if(browser.equalsIgnoreCase("chrome")){
            driver=new ChromeDriver();
            logger.info("Chrome Driver Instance is created");
        }else if(browser.equalsIgnoreCase("firefox")){
            driver=new FirefoxDriver();
            logger.info("Firefox Driver Instance is created");
        }else if(browser.equalsIgnoreCase("edge")){
            driver=new EdgeDriver();
        }else{
            throw new IllegalArgumentException("Browser Not Supported");
        }
    }

    //configure browser setting Implicit wait, Maximize Window and Url
    private void configureBrowser(){
        //Implicit Wait
        int implicitWait= Integer.parseInt(prop.getProperty("ImplicitWait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        //maximize the browser
        driver.manage().window().maximize();

        //navigate the url
        try {
            driver.get(prop.getProperty("url"));
        } catch (Exception e) {
            System.out.println("Failed to navigate to url :" + e.getMessage());
        }
    }
   //Driver getter method
    public static WebDriver getDriver(){
        if (driver == null){
            System.out.println("Web Driver is not initiallized");
            throw new IllegalStateException("Web Driver is not initiallized");
        }
        return driver;
    }

    //Action Driver getter method
    public static ActionDriver getActionDriver(){
        if (actionDriver == null){
            System.out.println("Action Driver is not initiallized");
            throw new IllegalStateException("Action Driver is not initiallized");
        }
        return actionDriver;
    }

    //Driver Setter method
    public void setDriver(WebDriver driver){
        this.driver=driver;
    }

    //get method for prop
    public static Properties getProp(){
        return prop;
    }
    @AfterMethod
    public void tearDown(){
        try {
            if(driver!=null){
                driver.quit();
            }
            logger.info("Web driver is closed");
            driver=null;
            actionDriver=null;
        } catch (Exception e) {
            System.out.println("Failed to quit the browser :" + e.getMessage());
        }
    }


    //static wait for wait
    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }


}


