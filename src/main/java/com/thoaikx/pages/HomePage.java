package com.thoaikx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class HomePage {

  private WebDriver driver ;

  // Locators for elements on the Home Page
  private By editBoxBy = By.cssSelector("input[name='name']:nth-child(2)");
  private By twoWayDataBindingBy = By.xpath("//*[contains(text(),'Two-way Data Binding example')]//following-sibling::input");
  private By genderBy = By.cssSelector("select");
  private By entrepreneurBy = By.cssSelector("#inlineRadio3");
  private By shopTabBy = By.cssSelector(":nth-child(2) > .nav-link");

  public HomePage(WebDriver driver) {
    this.driver = driver;
  }
  // Get the Edit Box element
  public WebElement getEditBox() {
    return driver.findElement(editBoxBy);
  }

  // Get the Two-Way Data Binding element
  public WebElement getTwoWayDataBinding() {
   // SeleniumUtils.explicitWaitElement(twoWayDataBindingBy);
    return driver.findElement(twoWayDataBindingBy);
  }
  // Get the Gender dropdown element
  public WebElement getGender() {
    return driver.findElement(genderBy);
  }

  // Get the Entrepreneur radio button element
  public WebElement getEntrepreneur() {
    return driver.findElement(entrepreneurBy);
  }

  // Get the Shop Tab element
  public WebElement getShopTab() {
    return driver.findElement(shopTabBy);
  }

  // Example method to click on Shop Tab
  public void clickShopTab() {
    driver.findElement(shopTabBy).click();
  }
}

