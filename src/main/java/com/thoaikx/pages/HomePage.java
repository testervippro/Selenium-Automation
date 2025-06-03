package com.thoaikx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class HomePage {
  private WebDriver driver;

  // Locators
  private By editBoxBy = By.cssSelector("input[name='name']:nth-child(2)");
  private By twoWayDataBindingBy = By.xpath("//*[contains(text(),'Two-way Data Binding example')]//following-sibling::input");
  private By genderDropdownBy = By.cssSelector("select");
  private By entrepreneurRadioBy = By.cssSelector("#inlineRadio3");
  private By shopTabBy = By.cssSelector(":nth-child(2) > .nav-link");

  public HomePage(WebDriver driver) {
    this.driver = driver;
  }

  public void enterName(String name) {
    driver.findElement(editBoxBy).sendKeys(name);
  }

  public String getTwoWayDataBindingValue() {
    return driver.findElement(twoWayDataBindingBy).getAttribute("value");
  }

  public void selectGender(String gender) {
    Select genderDropdown = new Select(driver.findElement(genderDropdownBy));
    genderDropdown.selectByVisibleText(gender);
  }

  public boolean isEntrepreneurDisabled() {
    return Boolean.parseBoolean(driver.findElement(entrepreneurRadioBy).getAttribute("disabled"));
  }

  public void clickShopTab() {
    driver.findElement(shopTabBy).click();
  }
}