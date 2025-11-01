package com.thoaikx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductPage {
  private WebDriver driver;
  private WebDriverWait wait;

  // Locators
  private By productTitlesBy = By.cssSelector("h4.card-title");
  private By addToCartButtonsBy = By.cssSelector("button.btn.btn-info");
  private By checkoutButtonBy = By.cssSelector("#navbarResponsive > .navbar-nav > .nav-item > .nav-link");
  private By priceCellsBy = By.cssSelector("tr td:nth-child(4) strong");
  private By totalAmountBy = By.cssSelector("h3 strong");

  public ProductPage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  public void addProductToCart(String productName) {
    List<WebElement> products = driver.findElements(productTitlesBy);
    List<WebElement> addButtons = driver.findElements(addToCartButtonsBy);

    for (int i = 0; i < products.size(); i++) {
      if (products.get(i).getText().contains(productName)) {
        WebElement button = addButtons.get(i);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();
        break;
      }
    }
  }

  public void clickCheckout() {
    driver.findElement(checkoutButtonBy).click();
  }

  public int calculateItemsTotal() {
    return driver.findElements(priceCellsBy)
            .stream()
            .mapToInt(element -> Integer.parseInt(element.getText().replaceAll("[^0-9]", "")))
            .sum();
  }

  public int getDisplayedTotal() {
    return Integer.parseInt(driver.findElement(totalAmountBy).getText().replaceAll("[^0-9]", ""));
  }
}