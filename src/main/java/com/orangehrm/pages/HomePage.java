package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    ActionDriver actionDriver;

    //Define Locators used By class
    private By adminTab=By.xpath("//span[text()='Admin']");
    private By userIdButton=By.className("oxd-userdropdown-name");
    private By logoutButton=By.xpath("//a[text()='Logout']");
    private By orangeHRMlogo=By.xpath("//div[@class='oxd-brand-banner']");

    //Initialize the action driver object by passing the webdriver instance
    public HomePage(WebDriver driver){
        this.actionDriver= BaseClass.getActionDriver();
    }

    //Method to verify if AdminTab is displayed
    public boolean adminTabIsDiplayed(){
        return actionDriver.isDisplayed(adminTab);
    }

    public boolean verifyHRMLogo(){
        return actionDriver.isDisplayed(orangeHRMlogo);
    }

    //method to perform logout operation
    public void clickLogout(){
        actionDriver.clickElement(logoutButton);
    }

}
