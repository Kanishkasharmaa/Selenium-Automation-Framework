package com.orangehrm.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExtentManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test= new ThreadLocal<>();
    private static Map<Long,WebDriver> driverMap=new HashMap<>();

    //Initiallize the extent report
    public synchronized static ExtentReports getReporter(){
        if(extent==null){
            String reportPath=System.getProperty("user.dir")+"/src/test/resources/ExtentReports/ExtentReport.html";
            ExtentSparkReporter spark= new ExtentSparkReporter(reportPath);
            spark.config().setReportName("Automation Test Report");
            spark.config().setDocumentTitle("Orange HRM");
            spark.config().setTheme(Theme.DARK);

            extent=new ExtentReports();
            extent.attachReporter(spark);
            //Adding System Information
            extent.setSystemInfo("Operating System",System.getProperty("os.name"));
            extent.setSystemInfo("Java Version",System.getProperty("java.name"));
            extent.setSystemInfo("User Name",System.getProperty("user.name"));
        }

        return extent;

    }

    //start the test
    public synchronized static ExtentTest startTest(String testName){
        ExtentTest extentTest= getReporter().createTest(testName);
        test.set(extentTest);
        return extentTest;

    }

    //End the test
    public synchronized static void endTest(){
        getReporter().flush();
    }


    //Get Current Thread test
    public synchronized static ExtentTest getTest(){
        return test.get();
    }

    //Method to get the name of the current test
    public static String getTestName(){
       ExtentTest currentTest=getTest();
       if(currentTest!=null){
           return currentTest.getModel().getName();
       } else {
           return "No test is currently active for the thread";
       }
    }

    //log a step
    public static void logStep(String logMessage){
        getTest().info(logMessage);
    }

    //log a step validation with screenshot
    public  static void logStepsWithScreenshots(WebDriver driver,String logMessage, String screenShotMessage){
        getTest().pass(logMessage);
        attachScreenshot(driver,screenShotMessage);


    }

    //log a failure
    public static void logFailure(WebDriver driver,String logMessage, String screenShotMessage){

        getTest().fail(logMessage);
        attachScreenshot(driver,screenShotMessage);
    }

    //log a skip
    public static void logSkip(String logMessage){

        getTest().skip(logMessage);

    }

    //Take a screenshot with date and time
    public synchronized static String takeScreenshot(WebDriver driver,String ScreenshotName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        //Formate date and time for file name
        String timeStamp = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss").format(new Date());

        //save screenshot to the file
        String desPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReports/screenshots/" + ScreenshotName + "_" + timeStamp + ".png";
        File finalPath = new File(desPath);
        try {
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //convert Screenshot to base64 for embedding in the report
        String base64format = convertToBase64(src);
        return base64format;
    }

    //convert the screenshot to base64 format
    public static String convertToBase64(File screenShotfile){
        try {
            String base64format="";
            //read the file content to byte array
            byte[] fileContent=FileUtils.readFileToByteArray(screenShotfile);
            //convert the byte array to base64 string
            return base64format=Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //attach screenshot to report using base 64
    public synchronized static void attachScreenshot(WebDriver driver,String message){
        try {
            String screenShotbase64=takeScreenshot(driver,getTestName());
            getTest().info(message,com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenShotbase64).build());
        } catch (Exception e) {
            getTest().fail("Fail to attah screenshot in report");
            throw new RuntimeException(e);
        }
    }
    //Register Webdriver for current thread

    public static void registerDriver(WebDriver driver){
        driverMap.put(Thread.currentThread().getId(),driver);
    }
}
