package com.thoaikx.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    public static void takeScreenshot(WebDriver driver, String methodName) {
        if (driver instanceof TakesScreenshot) {
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String folder = "images";
                new File(folder).mkdirs(); // Ensure folder exists
                String fileName = folder + File.separator + "screenshot_error_" + methodName + "_" + timestamp + ".png";
                Files.copy(screenshot.toPath(), Paths.get(fileName));
                System.out.println("Screenshot saved: " + fileName);
            } catch (IOException e) {
                System.err.println("Failed to save screenshot: " + e.getMessage());
            }
        }
    }
}

