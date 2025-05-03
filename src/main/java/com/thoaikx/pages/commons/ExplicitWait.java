package com.thoaikx.pages.commons;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExplicitWait {
  // Reusable method to wait for an element to be visible
  public WebElement waitForElementVisible(WebDriverWait wait, By menuIconLocator) {
    return wait.until(ExpectedConditions.visibilityOfElementLocated(menuIconLocator));
  }

  // Reusable method to wait for an element to be clickable
  public WebElement waitForElementClickable(WebDriverWait wait,By locator) {
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
  }

  public void waitForTextToBePresentInElement(WebDriverWait wait,By locator, String text) {
    wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
  }

  // Reusable method to wait for an element to be invisible
  public void waitForInvisibilityOfElementLocated(WebDriverWait wait,By locator) {
    wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
  }


}
