package com.thoaikx.utils;




import static com.thoaikx.driver.DriverManager.getDriver;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utils {

  private Utils(){}
  private  static Duration timeout  = Duration.ofSeconds(10000);

  public static boolean explicitWaitElement( WebElement locator ) {

    WebDriver driver = getDriver();
    WebDriverWait wait = new WebDriverWait(driver,timeout);
    // Defining individual conditions
    ExpectedCondition<WebElement> condition2 = ExpectedConditions.elementToBeClickable(locator);
   // ExpectedCondition<Boolean> isNameHtmlElementStale = ExpectedConditions.not(stalenessOf(locator));
    ExpectedCondition<Boolean> condition3 = ExpectedConditions.not(ExpectedConditions.attributeToBe(locator, "disabled", "true"));
    ExpectedCondition<Boolean> condition4 = ExpectedConditions.not(ExpectedConditions.attributeToBe(locator, "hidden", "true"));

    // Combining conditions with logical AND
    ExpectedCondition<Boolean> combinedCondition = ExpectedConditions.and( condition2, condition3, condition4);

    return wait.until(combinedCondition);
  }


}
