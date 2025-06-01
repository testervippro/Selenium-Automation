//package com.thoaikx;
//
//
//import io.github.sudharsan_selvaraj.autowait.SeleniumWaitOptions;
//import io.github.sudharsan_selvaraj.autowait.SeleniumWaitPlugin;
//import io.github.sudharsan_selvaraj.autowait.annotations.IgnoreWait;
//import io.github.sudharsan_selvaraj.autowait.annotations.WaitProperties;
//import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.annotations.Test;
//
//import java.time.Duration;
//
//public class WaitTest {
//
//    public WebDriver getDriver() {
//        // Optional: Use ChromeOptions to configure browser settings
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--start-maximized");
//
//        // Initialize ChromeDriver (Selenium Manager handles driver binaries automatically)
//        ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
//
//        // Configure SeleniumWaitOptions
//        SeleniumWaitOptions waitOptions = SeleniumWaitOptions.builder()
//                .parseAnnotations(true)
//                .defaultWaitTime(Duration.ofSeconds(60))
//                .build();
//
//        // Wrap the driver with SeleniumWaitPlugin
//        SeleniumWaitPlugin<ChromeDriver> waitPlugin = new SeleniumWaitPlugin<>(chromeDriver, waitOptions);
//
//        // Return wrapped WebDriver
//        return waitPlugin.getDriver();
//    }
//
//    @Test
//    public void test() {
//        var driver = getDriver();
//        searchAmazon(driver);
//       // driver.get("https://docs.aws.amazon.com/eks/latest/userguide/network-reqs.html");
//       // searchAmazonWithoutWait(driver);
//        //searchAmazonWithCustomWait(driver);
//
//        driver.quit();
//    }
//
//    public void searchAmazon(WebDriver driver) {
//        driver.get("https://www.amazon.in");
//        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("oneplus 7");
//        driver.findElement(By.id("twotabsearchtextbox")).sendKeys(Keys.ENTER);
//        driver.findElement(By.partialLinkText("OnePlus 7 Pro")).click();
//        driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[1]);
//        driver.findElement(By.id("add-to-cart-button")).click();
//        driver.findElement(By.id("attach-view-cart-button-form")).click();
//    }
//
//    @IgnoreWait // will not automatically wait for any element interaction
//    public void searchAmazonWithoutWait(WebDriver driver) {
//        driver.get("https://www.amazon.in");
//        new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.presenceOfElementLocated(By.id("twotabsearchtextbox")));
//        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("oneplus 7", Keys.ENTER);
//        new WebDriverWait(driver, Duration.ofSeconds(30))
//                .until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("OnePlus 7 Pro")));
//        driver.findElement(By.partialLinkText("OnePlus 7 Pro")).click();
//        driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[1]);
//        new WebDriverWait(driver, Duration.ofSeconds(30))
//                .until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-button")));
//        driver.findElement(By.id("add-to-cart-button")).click();
//        new WebDriverWait(driver, Duration.ofSeconds(30))
//                .until(ExpectedConditions.elementToBeClickable(By.id("attach-view-cart-button-form")));
//        driver.findElement(By.id("attach-view-cart-button-form")).click();
//    }
//
//    @WaitProperties(
//            timeout = 10, //custom wait time in seconds
//            exclude = {"sendKeys"} // will not automatically wait for sendKeys method
//    )
//    public void searchAmazonWithCustomWait(WebDriver driver) {
//        driver.get("https://www.amazon.in");
//        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("oneplus 7");
//        driver.findElement(By.id("twotabsearchtextbox")).sendKeys(Keys.ENTER);
//        driver.findElement(By.partialLinkText("OnePlus 7 Pro")).click();
//        driver.switchTo().window(driver.getWindowHandles().toArray(new String[]{})[1]);
//        driver.findElement(By.id("add-to-cart-button")).click();
//        driver.findElement(By.id("attach-view-cart-button-form")).click();
//    }
//}
