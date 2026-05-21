package com.orangehrm;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class DummyClass extends BaseClass {
    @Test
    public void dummyTest() {
        String title = getDriver().getTitle();
        ExtentManager.startTest("dummyTest");
        ExtentManager.logStep("Verifying the title");
        assert title.equals("OrangeHRM") : "Test failed - Title is not matching";

        System.out.println("Tile is matching");
        throw new SkipException("Skiping the test as a part of testing");
    }
}
