package com.orangehrm;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.testng.annotations.Test;

public class DummyClass2 extends BaseClass {
    @Test
    public void dummyTest() {
        ExtentManager.startTest("Dummy test2");
        ExtentManager.logStep("Verifying the title 2");
        String title = getDriver().getTitle();
        assert title.equals("OrangeHRM") : "Test failed - Title is not matching";

        System.out.println("Tile is matching");
    }
}
