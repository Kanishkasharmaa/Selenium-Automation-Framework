package com.orangehrm.actiondriver;

import com.orangehrm.base.BaseClass;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class ActionDriver {
    private WebDriver driver;
    private WebDriverWait wait;
    public static final Logger logger = BaseClass.logger;


    public ActionDriver(WebDriver driver){
        this.driver=driver;
        int explicitWait=Integer.parseInt(BaseClass.getProp().getProperty("ExplicitWait"));
       this.wait=new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }

    private void waitForElementToBeClickable(By by){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.info("Element is not clickable : " + e.getMessage());
        }
    }

    // Method to get discription of an element using by locator
    public String getElementDescription(By locator){
        //check for null driver or locator to avoid NULL pointr exception
        try {
            if(driver==null){
                return "driver is null";
            }
            if(locator==null){
                return "locator is null";
            }

            //find the element using the locator
            WebElement element=driver.findElement(locator);

            //get element Atrribute
            String name= element.getDomAttribute("name");
            String id=element.getDomAttribute("ïd");
            String text= element.getText();
            String className= element.getDomAttribute("class");
            String placeholder=element.getDomAttribute("placeholder");
            //return the attribute
            if(isNotEmpty(name)){
                return "Element with name  : " + name;
            }else if(isNotEmpty(id)){
                return "Element with id  : " + id;
            }else if(isNotEmpty(text)){
                return "Element with text  : " + truncate(text,50);
            }else if(isNotEmpty(className)){
                return "Element with class name  : " + className;
            }else if(isNotEmpty(placeholder)){
                return "Element with placeholder  : " + placeholder;
            }
        } catch (Exception e) {
            logger.error("unable to describe the element"+ e.getMessage());
        }
      return null;
    }

    //utitlity method to check if a check is not null or empty
    private boolean isNotEmpty(String value){
        return value!=null && !value.isEmpty();
    }

    //utility method to truncate long string
    private String truncate(String value,int maxLength){
        if(value == null && value.length()<=maxLength){
            return value;
        }
        return value.substring(0,maxLength);
    }
    private void waitForElementToBeVisible(By by){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Element is not visible : " + e.getMessage());
        }
    }

    // Method to click
    public void clickElement(By by){
        try {
            String elementDescription= getElementDescription(by);
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            logger.info("clicked an element" + elementDescription);
        } catch (Exception e) {
            logger.error("Unable to click element");
        }
    }

    //Method to add text into input field
    public void enterText(By by,String value){
        try {
            waitForElementToBeVisible(by);
            WebElement element= driver.findElement(by);
            element.click();
            element.sendKeys(value);
            logger.info("Entered text" + getElementDescription(by) + " --> " + value);
        } catch (Exception e) {
            logger.error("Unable to enter the value in input text" + e.getMessage());
        }
    }

    // method to get method from input field
    public String getText(By by){
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            logger.error("Unable to get text :" + e.getMessage());
            return "";
        }
    }

    //methods for text to be equals --- change return type
    public boolean compareText(By by, String expectedText){
        try {
            waitForElementToBeVisible(by);
            String actualText= driver.findElement(by).getText();
            if(actualText.equals(expectedText)){
                logger.info(" Texts are matching " + expectedText + " equals " + actualText);
                return true;
            }else{
                logger.info(" Texts are not matching " + expectedText + " not equals " + actualText);
                return false;
            }
        } catch (Exception e) {
            logger.error("Unable to compare text " + e.getMessage());

        }
        return false;
    }

    //check if element is displayed
    public boolean isDisplayed(By by){
        try {
            waitForElementToBeVisible(by);
            boolean isDisplayed= driver.findElement(by).isDisplayed();
            logger.info("Element is displayed" + getElementDescription(by));
            return isDisplayed;
        } catch (Exception e) {
            logger.error("Unable to find element : " +e.getMessage());
            return false;
        }


    }

    // scroll to an element
    public void scrollToAnElement(By by){
        try {
            JavascriptExecutor  js= (JavascriptExecutor) driver;
            WebElement element= driver.findElement(by);
            js.executeScript("arguments[0],scrollIntoView(true)",element);
        } catch (Exception e) {
            logger.error("Unable to scroll to element " + e.getMessage());
        }
    }

    //wait for page to load
    public void waitForPageToLoad(int timeOutInSecond){
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSecond)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
                    .executeScript("return document.readyState").equals("complete"));
            logger.info("Page Loaded Sucessfully");
        } catch (Exception e) {
            logger.error("Page not loaded within " + timeOutInSecond + " seconds " );
        }

    }
}
