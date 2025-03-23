//package com.thoaikx;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.testng.Assert;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import java.io.File;
//import java.io.IOException;
//
//public class TestLabNG {
//
//    private WebDriver driver;
//    private Process xvfbProcess;
//    private Process ffmpegProcess;
//
//    // Method to get the root directory of the project
//    public static String getProjectRoot() {
//        String projectRoot = System.getProperty("user.dir");
//
//        // Check if pom.xml exists in the current directory (Maven project check)
//        File pomFile = new File(projectRoot, "pom.xml");
//        if (pomFile.exists() && pomFile.isFile()) {
//            return projectRoot;
//        }
//
//        // Walk up directory chain if pom.xml is not found
//        File parentDir = new File(projectRoot).getParentFile();
//        while (parentDir != null) {
//            pomFile = new File(parentDir, "pom.xml");
//            if (pomFile.exists() && pomFile.isFile()) {
//                return parentDir.getAbsolutePath();
//            }
//            parentDir = parentDir.getParentFile();
//        }
//
//        return projectRoot;
//    }
//
//    @BeforeClass
//    public void setUp() throws IOException, InterruptedException {
//        // Start Xvfb (virtual display)
//        String display = ":99"; // Display number for Xvfb
//        ProcessBuilder xvfbStart = new ProcessBuilder("Xvfb", display, "-screen", "0", "1280x1024x24");
//        xvfbProcess = xvfbStart.start();
//
//        // Set DISPLAY environment variable to use the virtual screen created by Xvfb
//        System.setProperty("DISPLAY", display);
//
//        // Initialize WebDriverManager to download the correct version of ChromeDriver
//        WebDriverManager.chromedriver().setup();
//
//        // Set Chrome options for headless browsing
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--no-sandbox");
//        options.addArguments("--disable-gpu");
//        options.addArguments("--window-size=1280x720");
//        options.addArguments("--headless");
//
//        // Initialize the WebDriver with the specified options
//        driver = new ChromeDriver(options);
//
//        // Get the project root directory
//        String projectRoot = getProjectRoot();
//
//        // Path where the screen recording will be saved
//        String recordingPath = new File(projectRoot, "browser_recording.mp4").getAbsolutePath();
//
//        // Start FFmpeg process to record the screen from the virtual display
//        try {
//            ffmpegProcess = new ProcessBuilder(
//                    "ffmpeg",
//                    "-f", "x11grab",
//                    "-video_size", "1280x720",
//                    "-framerate", "25",
//                    "-i", display + ".0",  // Capture from the virtual display
//                    "-c:v", "libx264",
//                    "-preset", "ultrafast",
//                    "-crf", "0",
//                    recordingPath
//            ).start();
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new IOException("Failed to start FFmpeg process.");
//        }
//
//        // Allow FFmpeg some time to start recording
//        Thread.sleep(1000);
//    }
//
//    @Test
//    public void testGooglePageTitle() {
//        // Navigate to Google and capture the page title
//        driver.get("https://www.google.com");
//        String pageTitle = driver.getTitle();
//        System.out.println("Page Title: " + pageTitle);
//
//        // Assert that the page title contains "Google"
//        Assert.assertTrue(pageTitle.contains("Google"), "Page title does not contain 'Google'");
//    }
//
//    @AfterClass
//    public void tearDown() {
//        // Stop the FFmpeg process (screen recording)
//        if (ffmpegProcess != null) {
//            ffmpegProcess.destroy();
//        }
//
//        // Stop the Xvfb process
//        if (xvfbProcess != null) {
//            xvfbProcess.destroy();
//        }
//
//        // Close the WebDriver
//        if (driver != null) {
//            driver.quit();
//        }
//
//        System.out.println("Recording saved as browser_recording.mp4");
//    }
//}
