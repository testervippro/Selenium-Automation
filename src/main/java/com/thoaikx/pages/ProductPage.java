package com.thoaikx.pages;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

public class ProductPage {

  private By checkoutButtonBy = By.cssSelector("#navbarResponsive > .navbar-nav > .nav-item > .nav-link");

  public ProductPage(WebDriver driver) {
    this.driver = driver;
  }

  private WebDriver driver;



  public void selectProduct(String productName) {
    // Find all elements with 'h4.card-title' and iterate through them
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    int index = 0;
    for (WebElement element : driver.findElements(By.cssSelector("h4.card-title"))) {
      if (element.getText().contains(productName)) {
        // Wait for the button to be clickable and then click
        WebElement button = driver.findElements(By.cssSelector("button.btn.btn-info")).get(index);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();
        break; // Exit after clicking
      }
      index++;
    }
  }


}
