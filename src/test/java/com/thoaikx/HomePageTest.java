package com.thoaikx;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import static org.testng.AssertJUnit.assertEquals;

import com.google.common.io.Files;
import com.thoaikx.pages.HomePage;
import com.thoaikx.pages.ProductPage;

import com.thoaikx.report.AllureManager;
import io.qameta.allure.Allure;
import java.awt.*;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.thoaikx.record.RecorderManager;
import com.video.VideoRecord;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import selenium.SeleniumServerManager;


@Log4j2
public class HomePageTest extends BaseTest {


    @BeforeClass
    public void  start() throws Exception {
        RecorderManager.startVideoRecording(RecorderManager.RECORDTYPE.MONTE,"Video01");
    }

    @AfterClass
    public void  stop() throws Exception {
        RecorderManager.stopVideoRecording(RecorderManager.RECORDTYPE.MONTE,true);
        File videoPath = new File("videos", RecorderManager.nameVideo);
        log.info(videoPath);

        if (videoPath.exists() && videoPath.isFile()) { // Check if file exists and is a file
            Allure.addAttachment("Video", "video/mp4",
                Files.asByteSource(videoPath).openStream(), "mp4");
        } else {
            log.info("Video file does not exist: " + videoPath.getAbsolutePath());
        }
    }

    @Test(priority = 0)
    public void completeShoppingFlow() throws InterruptedException {
        // Step 1: Interact with HomePage
        HomePage homePage = new HomePage(driver);
        homePage.getEditBox().sendKeys("bob");
        String expectedValue = homePage.getTwoWayDataBinding().getAttribute("value");
        Assert.assertEquals(expectedValue, "bob");

        homePage.getGender().click();
        select.selectByVisibleText(homePage.getGender(), "Female");
        String expectEntre = homePage.getEntrepreneur().getAttribute("disabled");
        Assert.assertEquals(expectEntre, "true");

        homePage.getShopTab().click();

        // Step 2: Select Products
        ProductPage productPage = new ProductPage(driver);
        List.of("Blackberry", "Nokia Edge").forEach(productPage::selectProduct);

        // Step 3: Checkout Process
        productPage.getCheckOutButton().click();
        int sum = driver.findElements(By.cssSelector("tr td:nth-child(4) strong"))
                .stream()
                .mapToInt(element -> Integer.parseInt(element.getText().replaceAll("[^0-9]", "")))
                .sum();

        int total = Integer.parseInt(driver.findElement(By.cssSelector("h3 strong")).getText().replaceAll("[^0-9]", ""));
        Assert.assertEquals(sum, total, "Check sum of each product = Total");

        driver.findElement(By.xpath("//*[contains(text(), 'Checkout')]")).click();
        driver.findElement(By.id("country")).sendKeys("India");
        WebElement suggestions = wait.until(visibilityOfElementLocated(By.className("suggestions")));
        suggestions.findElement(By.cssSelector("ul > li > a")).click();

        WebElement checkBox2 = driver.findElement(By.xpath("//*[@id ='checkbox2']"));
        jsExecutor.executeScript("arguments[0].click();", checkBox2);

        // Submit the form
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        WebElement alert = wait.until(visibilityOfElementLocated(By.cssSelector(".alert")));
        String actualText = alert.getText();
        Assert.assertTrue(actualText.contains("Success"));
    }
}
