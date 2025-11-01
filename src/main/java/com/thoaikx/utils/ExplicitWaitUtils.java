package com.thoaikx.utils;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.driver.DriverManager.getDriver;


@Log4j2
public class ExplicitWaitUtils {

    // Method to wait for an element to be visible
   public static WebElement waitForElementVisibleAndInDOM(By locator) {
       // findElement(locator);
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }


    public static boolean waitForElementHaveTextToBe(By locator,String textToBe) {
       // findElement(locator);
        return  getWait().until(ExpectedConditions.textToBe(locator,textToBe));

    }
   public static boolean waitForElementInvisibleOrInDOM(By locator) {
        //findElement(locator);
       return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));

    }
    public static boolean waitForElementToBeRemovedFromDOM(By locator) {
        var webElement = getDriver().findElement(locator);
        return getWait().until(ExpectedConditions.stalenessOf(webElement));
    }

    // Method to wait for an element to be clickable
   public static WebElement waitForElementClickable(By locator) {
        return waitForElementClickable(locator,false);
    }
    public static WebElement waitForElementClickable(By locator,boolean forceClickByJS) {
       if (forceClickByJS){
           var webElement =  findElement(locator);
           ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", webElement);
           return  webElement;
       }
       return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Method to wait for specific text to be present in an element
    public static void waitForTextToBePresentInElement( By locator, String text) {
      //findElement(locator);
       getWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }



    public  static void waitForURLToBe( String url) {
        getWait().until(ExpectedConditions.urlToBe(url));
    }

    public  static void waitForURLContains( String url) {
        getWait().until(ExpectedConditions.urlContains(url));
    }

    public  static void waitForTitleContains( String url) {
        getWait().until(ExpectedConditions.titleContains(url));
    }

    private static WebDriverWait getWait() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(configuration().timeout()));
        // Log information about the WebDriverWait instance and the current thread
        log.info("Created WebDriverWait instance: {} | Thread: {}", wait, Thread.currentThread().getName());

        return wait;
    }

    private  static  WebElement  findElement(By locator){
       return   getDriver().findElement(locator);
    }

}

