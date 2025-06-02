package com.thoaikx;

import static com.thoaikx.driver.DriverManager.getDriver;
import static com.thoaikx.utils.ExplicitWaitUtils.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import com.google.common.io.Files;
import com.thoaikx.pages.HomePage;
import com.thoaikx.pages.ProductPage;

import io.qameta.allure.Allure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.thoaikx.record.RecorderManager;
import io.qameta.allure.Attachment;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


@Log4j2
public class HomePageTest extends BaseTest {

    @BeforeClass
    public void  start() throws Exception {
       //RecorderManager.startCaptureFrames();
    }

    @AfterClass
    public void  stop() throws Exception {
       // RecorderManager.convertImagesToVideo("Video01");
       //RecorderManager.stopVideoRecording(RecorderManager.RECORDTYPE.HEADLESS,true);
        //ecorderManager.convertImagesToVideo("Video01");

        attachVideo();
        attachLog();
    }

    private static void attachLog() {
        // Attach log file
        File logFile = Path.of( "target","test_automation.log").toFile();
        log.info("Log file path: {}", logFile.getAbsolutePath());

        if (logFile.exists() && logFile.isFile()) {
            try {
                Allure.addAttachment("Execution Log", "text/plain",
                        new FileInputStream(logFile), "log");
            } catch (IOException e) {
                log.error("Failed to attach log file: ", e);
            }
        } else {
            log.info("Log file does not exist: {}", logFile.getAbsolutePath());
        }
    }

    private static void attachVideo() {
        File videoPath = new File("videos", RecorderManager.nameVideo);
        log.info("Video path: {}", videoPath.getAbsolutePath());

        if (videoPath.exists() && videoPath.isFile()) {
            try {
                Allure.addAttachment("Test Video", "video/mp4",
                        Files.asByteSource(videoPath).openStream(), "mp4");
            } catch (IOException e) {
                log.error("Failed to attach video: ", e);
            }
        } else {
            log.info("Video file does not exist: {}", videoPath.getAbsolutePath());
        }
    }


    @Test(priority = 0)
    public void completeShoppingFlow() throws InterruptedException {
        // Step 1: Interact with HomePage
        HomePage homePage = new HomePage(getDriver());
        homePage.getEditBox().sendKeys("bob");
        String expectedValue = homePage.getTwoWayDataBinding().getAttribute("value");
        Assert.assertEquals(expectedValue, "bob");

        homePage.getGender().click();
        select.selectByVisibleText(homePage.getGender(), "Female");
        String expectEntre = homePage.getEntrepreneur().getAttribute("disabled");
        Assert.assertEquals(expectEntre, "true");

        homePage.getShopTab().click();

        // Step 2: Select Products

        ProductPage productPage = new ProductPage(getDriver());
        List.of("Blackberry", "Nokia Edge").forEach(productPage::selectProduct);

        // Step 3: Checkout Process
        productPage.getCheckOutButton().click();
        int sum = getDriver().findElements(By.cssSelector("tr td:nth-child(4) strong"))
                .stream()
                .mapToInt(element -> Integer.parseInt(element.getText().replaceAll("[^0-9]", "")))
                .sum();

        int total = Integer.parseInt(getDriver().findElement(By.cssSelector("h3 strong")).getText().replaceAll("[^0-9]", ""));
        Assert.assertEquals(sum, total, "Check sum of each product = Total");

        getDriver().findElement(By.xpath("//*[contains(text(), 'Checkout')]")).click();
        getDriver().findElement(By.id("country")).sendKeys("India");

        waitForElementVisibleAndInDOM(By.className("suggestions")).findElement(By.cssSelector("ul > li > a")).click();
        waitForElementClickable(By.cssSelector("#checkbox2"),true);

        // Submit the form
        getDriver().findElement(By.cssSelector("input[type='submit']")).click();
        var alert = waitForElementVisibleAndInDOM(By.cssSelector(".alert"));
        String actualText = alert.getText();
        Assert.assertTrue(actualText.contains("Success"));
    }
}