package com.thoaikx.listener;

import com.thoaikx.utils.ScreenshotUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;

import static com.thoaikx.driver.DriverManager.getDriver;

public class TestFailureListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = null;
        try {
            driver = getDriver();
        } catch (Exception e) {
            System.err.println("Could not access WebDriver from test class: " + e.getMessage());
        }

        if (driver != null) {
            ScreenshotUtils.takeScreenshot(driver, result.getName());
        }
    }
}

