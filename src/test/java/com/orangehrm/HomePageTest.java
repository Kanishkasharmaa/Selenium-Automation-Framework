package com.orangehrm;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
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
        loginPage.login("Admin","admin123");
        Assert.assertTrue(homePage.verifyHRMLogo(),"Logo is not diplayed");
    }
}
