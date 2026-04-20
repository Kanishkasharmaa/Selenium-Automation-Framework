package com.orangehrm;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
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
        loginPage.login("Admin","admin123");
        Assert.assertTrue(homePage.adminTabIsDiplayed(),"Admin Tab should be visible after login");
        homePage.clickLogout();
        staticWait(2);
    }

    @Test
    public void invalidLoginTest(){
        loginPage.login("Admin","ad");
        String expectedErrorMessage="Invalid credentials";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage));
    }
}
