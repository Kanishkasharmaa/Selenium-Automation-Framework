package com.orangehrm.base;
import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
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
//    protected static WebDriver driver;
//    private static ActionDriver actionDriver;
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    public static final Logger logger= LoggerManager.getLogger(BaseClass.class);
    @BeforeSuite
    public void loadConfig() throws IOException {
        //load the configuration file
        prop= new Properties();
        FileInputStream fis=new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
        logger.info("config.properties file is loaded");

        //start the extent report
        ExtentManager.getReporter();
    }

    @BeforeMethod
    public synchronized void setup() throws IOException {
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
//        //initiallize the action driver only once
//        if(actionDriver==null){
//            actionDriver=new ActionDriver(driver);
//            logger.info("Action driver is created" + Thread.currentThread().getId());
//
//        }
        //Initialize Action Driver for current thread
        actionDriver.set(new ActionDriver(getDriver()));
        logger.info("ActionDriver initailized for thread" + Thread.currentThread().getId());
   }

    private synchronized void launchBrowser(){
        //Intialize the browser based on browser defined in config.properties file
        String browser= prop.getProperty("browser");
        if(browser.equalsIgnoreCase("chrome")){
//            driver=new ChromeDriver();
            driver.set(new ChromeDriver()); //new changes as per thread
            ExtentManager.registerDriver(getDriver());
            logger.info("Chrome Driver Instance is created");
        }else if(browser.equalsIgnoreCase("firefox")){
//            driver=new FirefoxDriver();
            driver.set(new FirefoxDriver()); //new changes as per thread
            ExtentManager.registerDriver(getDriver());
            logger.info("Firefox Driver Instance is created");
        }else if(browser.equalsIgnoreCase("edge")){
//            driver=new EdgeDriver();
            driver.set(new EdgeDriver()); //new changes as per thread
            ExtentManager.registerDriver(getDriver());
            logger.info("Edge Driver Instance is created");
        }else{
            throw new IllegalArgumentException("Browser Not Supported");
        }
    }

    //configure browser setting Implicit wait, Maximize Window and Url
    private void configureBrowser(){
        //Implicit Wait
        int implicitWait= Integer.parseInt(prop.getProperty("ImplicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        //maximize the browser
        getDriver().manage().window().maximize();

        //navigate the url
        try {
            getDriver().get(prop.getProperty("url"));
        } catch (Exception e) {
            System.out.println("Failed to navigate to url :" + e.getMessage());
        }
    }
   //Driver getter method
    public static WebDriver getDriver(){
        if (driver.get() == null){
            System.out.println("Web Driver is not initiallized");
            throw new IllegalStateException("Web Driver is not initiallized");
        }
        return driver.get();
    }

    //Action Driver getter method
    public static ActionDriver getActionDriver(){
        if (actionDriver.get() == null){
            System.out.println("Action Driver is not initiallized");
            throw new IllegalStateException("Action Driver is not initiallized");
        }
        return actionDriver.get();
    }

    //Driver Setter method
    public void setDriver(ThreadLocal<WebDriver> driver){
        this.driver=driver;
    }

    //get method for prop
    public static Properties getProp(){
        return prop;
    }
    @AfterMethod
    public synchronized void tearDown(){
        try {
            if(getDriver()!=null){
                getDriver().quit();
            }
            logger.info("Web driver is closed");
            driver.remove();
            actionDriver.remove();
            ExtentManager.endTest();
        } catch (Exception e) {
            System.out.println("Failed to quit the browser :" + e.getMessage());
        }
    }


    //static wait for wait
    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }


}


