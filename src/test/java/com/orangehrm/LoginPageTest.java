package com.orangehrm;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }
    @Test
    public void verifyLoginTest(){
        ExtentManager.startTest("Valid login");
        ExtentManager.logStep("Entering usrname and password");
        loginPage.login("Admin","admin123");

        ExtentManager.logStep("Verifying admin tab is displayed");
        Assert.assertTrue(homePage.adminTabIsDiplayed(),"Admin Tab should be visible after login");
        ExtentManager.logStep("Validation sucessful");
        homePage.clickLogout();
        ExtentManager.logStep("Logged out sucessfully");
        staticWait(2);
    }

    @Test
    public void invalidLoginTest(){
        ExtentManager.startTest("InValid login");
        ExtentManager.logStep("Entering usrname and password");
        loginPage.login("Admin","ad");

        String expectedErrorMessage="Invalid credentials";

        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage));
        ExtentManager.logStep("Validation sucessful");
        ExtentManager.logStep("Logged out sucessfully");
    }
}
