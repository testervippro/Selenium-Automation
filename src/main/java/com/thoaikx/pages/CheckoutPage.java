package com.thoaikx.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.thoaikx.utils.ExplicitWaitUtils.*;

public class CheckoutPage {
    private WebDriver driver;


    // Locators
    private By checkoutButtonBy = By.xpath("//*[contains(text(), 'Checkout')]");
    private By countryInputBy = By.id("country");
    private By countrySuggestionBy = By.cssSelector(".suggestions ul > li > a");
    private By termsCheckboxBy = By.cssSelector("#checkbox2");
    private By submitButtonBy = By.cssSelector("input[type='submit']");
    private By successAlertBy = By.cssSelector(".alert");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    public void proceedToFinalCheckout() {
        driver.findElement(checkoutButtonBy).click();
    }

    public void enterCountry(String country) {
        driver.findElement(countryInputBy).sendKeys(country);
    }

    public void selectCountryFromSuggestions() {
        waitForElementVisibleAndInDOM(countrySuggestionBy).click();
    }

    public void agreeToTerms() {
        waitForElementClickable(termsCheckboxBy,true);
    }

    public void submitOrder() {
        driver.findElement(submitButtonBy).click();
    }

    public String getSuccessMessage() {
        return waitForElementVisibleAndInDOM(successAlertBy).getText();
        //return wait.until(ExpectedConditions.visibilityOfElementLocated(successAlertBy)).getText();
    }
}