package com.thoaikx;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class TestLabNG {

    private WebDriver driver;
    private Process ffmpegProcess;

    // Method to get the root directory of the project
    public static String getProjectRoot() {
        // Get the current working directory (assumed to be a child or the root)
        String projectRoot = System.getProperty("user.dir");

        // Check if pom.xml exists in the current directory
        File pomFile = new File(projectRoot, "pom.xml");
        if (pomFile.exists() && pomFile.isFile()) {
            return projectRoot;  // This is the root of the project (Maven project)
        }

        // Optionally, if you need to search up the directory chain for pom.xml
        // You can walk up directories if the file is not found in the current directory.
        File parentDir = new File(projectRoot).getParentFile();
        while (parentDir != null) {
            pomFile = new File(parentDir, "pom.xml");
            if (pomFile.exists() && pomFile.isFile()) {
                return parentDir.getAbsolutePath();  // Found the root
            }
            parentDir = parentDir.getParentFile();  // Move one level up
        }

        // If no pom.xml is found, return the current directory
        return projectRoot;
    }

    @BeforeClass
    public void setUp() throws IOException {
        // Start Xvfb (virtual display) if it's not running
        String display = ":99"; // You can use another available display number
        ProcessBuilder xvfbStart = new ProcessBuilder("Xvfb", display, "-screen", "0", "1280x1024x24");
        Process xvfbProcess = xvfbStart.start();

        // Set DISPLAY environment variable to use the virtual screen created by Xvfb
        System.setProperty("DISPLAY", display);

        // Use WebDriverManager to automatically download and set up the correct ChromeDriver version
        WebDriverManager.chromedriver().setup();

        // Set Chrome options for headless browsing and other configurations
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1280x720");
        options.addArguments("--headless");

        // Initialize the WebDriver
        driver = new ChromeDriver(options);

        // Get the project root directory
        String projectRoot = getProjectRoot();

        // Path where the video will be saved in the root project
        String recordingPath = new File(projectRoot, "browser_recording.mp4").getAbsolutePath();

        // Start FFmpeg process to record the screen using Xvfb
        try {
            ffmpegProcess = new ProcessBuilder(
                    "ffmpeg",
                    "-f", "x11grab",
                    "-video_size", "1280x720",
                    "-framerate", "25",
                    "-i", display + ".0",  // Using the virtual display number
                    "-c:v", "libx264",
                    "-preset", "ultrafast",
                    "-crf", "0",
                    recordingPath
            ).start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to start FFmpeg process.");
        }

        // Give FFmpeg a moment to start recording
        try {
            Thread.sleep(1000); // Allow some time for FFmpeg to initialize
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGooglePageTitle() {
        // Navigate to Google
        driver.get("https://www.google.com");

        // Print the title of the page
        String pageTitle = driver.getTitle();
        System.out.println("Page Title: " + pageTitle);

        // Assert that the page title contains "Google"
        Assert.assertTrue(pageTitle.contains("Google"), "Page title does not contain 'Google'");
    }

    @AfterClass
    public void tearDown() {
        // Stop the screen recording
        if (ffmpegProcess != null) {
            ffmpegProcess.destroy();
        }

        // Close the browser
        if (driver != null) {
            driver.quit();
        }

        System.out.println("Recording saved as browser_recording.mp4");
    }
}
