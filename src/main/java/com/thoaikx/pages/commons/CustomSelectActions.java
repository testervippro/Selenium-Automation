package com.thoaikx.pages.commons;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class CustomSelectActions {

  protected  WebDriver driver;

  public CustomSelectActions(WebDriver driver) {
    this.driver = driver;
  }

  // Select by value using a WebElement
  public void selectByValue(WebElement selectElement, String value) {
    Select select = new Select(selectElement);
    select.selectByValue(value);
  }

  public void selectByIndex(WebElement selectElement, int value) {
    Select select = new Select(selectElement);
    select.selectByIndex(value);
  }

  public void selectByVisibleText(WebElement selectElement, String value) {
    Select select = new Select(selectElement);
    select.selectByVisibleText(value);
  }




}
