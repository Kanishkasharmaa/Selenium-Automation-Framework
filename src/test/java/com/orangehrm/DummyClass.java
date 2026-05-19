package com.orangehrm;

import com.orangehrm.base.BaseClass;
import org.testng.annotations.Test;

public class DummyClass extends BaseClass {
    @Test
    public void dummyTest() {
        String title = getDriver().getTitle();
        assert title.equals("OrangeHRM") : "Test failed - Title is not matching";

        System.out.println("Tile is matching");
    }
}
