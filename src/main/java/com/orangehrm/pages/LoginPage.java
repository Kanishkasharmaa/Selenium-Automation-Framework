package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private ActionDriver actionDriver;

    //Define Locators using By class
    private By userNameField= By.name("username");
    private By passwordField=By.cssSelector("input[type='password']");
    private By loginButton=By.xpath("//button[text()=' Login ']");
    private By errorMessage=By.xpath("//p[text()='Invalid credentials']");

    public LoginPage(WebDriver driver){
        this.actionDriver= BaseClass.getActionDriver();
    }

    //Method to perform login
    public void login(String username,String password){
        actionDriver.enterText(userNameField,username);
        actionDriver.enterText(passwordField,password);
        actionDriver.clickElement(loginButton);
    }



    //method to check if error message is displayed
    public boolean errorMessageIsDisplayed(){
        return actionDriver.isDisplayed(errorMessage);

    }

    //method to get Text from error Message
    public String getErrorMessage(){
        return actionDriver.getText(errorMessage);
    }

    //verify if error message is correct or not
    public boolean verifyErrorMessage(String expectedText){
        return actionDriver.compareText(errorMessage,expectedText);
    }

}
