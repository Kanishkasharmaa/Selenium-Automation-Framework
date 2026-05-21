package com.orangehrm;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

public class HomePageTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    public void verifyHRMPageLogo(){
        ExtentManager.startTest("Home page verify logo");
        ExtentManager.logStep("Entering usrname and password");
        loginPage.login("Admin","admin123");
        ExtentManager.logStep("Verify logo is diplayed or not");
        Assert.assertTrue(homePage.verifyHRMLogo(),"Logo is not diplayed");
        ExtentManager.logStep("Logo displayed sucessfully");
    }
}
