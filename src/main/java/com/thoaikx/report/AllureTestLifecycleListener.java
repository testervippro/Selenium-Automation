package com.thoaikx.report;

import com.thoaikx.driver.DriverManager;
import io.qameta.allure.Attachment;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;

import java.io.File;
import java.io.FileInputStream;
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
//    @Attachment(value = "Test Video", type = "video/mp4")
//    public byte[] saveVideo(WebDriver driver) {
//
//        File videoFile = new File("path/to/video.mp4");
//        try (FileInputStream fis = new FileInputStream(videoFile)) {
//            byte[] videoBytes = new byte[(int) videoFile.length()];
//            fis.read(videoBytes);
//            return videoBytes;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new byte[0];
//        }
//    }

    @Override
    public void beforeTestStop(TestResult result) {
        if (FAILED == result.getStatus() || BROKEN == result.getStatus()) {
            WebDriver driver = DriverManager.getDriver(); // Retrieve the driver instance
            saveScreenshot(driver); // Pass the driver to the screenshot method
        }
    }



}