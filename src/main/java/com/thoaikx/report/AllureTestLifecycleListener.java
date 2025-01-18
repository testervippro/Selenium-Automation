package com.thoaikx.report;

import com.thoaikx.driver.DriverManager;
import io.qameta.allure.Attachment;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import static io.qameta.allure.model.Status.BROKEN;
import static io.qameta.allure.model.Status.FAILED;

/*
 * Approach implemented using the https://github.com/biczomate/allure-testng7.5-attachment-example as reference
 */
public class AllureTestLifecycleListener implements TestLifecycleListener {

    public AllureTestLifecycleListener() {
        // Default constructor
    }

    @Attachment(value = "Page Screenshot", type = "image/png")
    public byte[] saveScreenshot(WebDriver driver) {
        if (driver != null) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } else {
            System.err.println("Driver is null. Cannot take screenshot.");
            return new byte[0]; // Return empty byte array if driver is null
        }
    }

    @Override
    public void beforeTestStop(TestResult result) {
        if (FAILED == result.getStatus() || BROKEN == result.getStatus()) {
            WebDriver driver = DriverManager.getDriver(); // Retrieve the driver instance
            saveScreenshot(driver); // Pass the driver to the screenshot method
        }
    }

    @Override
    public void afterTestStop(TestResult result) {
        // Stop Selenium Grid after all tests finish
       // stopSeleniumGrid();
    }

    private void stopSeleniumGrid() {
        try {
            // Execute the `docker-compose down` command
            Process process = Runtime.getRuntime().exec("docker-compose -f docker-compose-grid.yml down");
            process.waitFor();  // Wait for the command to complete
            System.out.println("Selenium Grid stopped successfully.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to stop Selenium Grid: " + e.getMessage());
        }
    }
}