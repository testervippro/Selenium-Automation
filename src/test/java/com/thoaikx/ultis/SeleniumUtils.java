package com.thoaikx.ultis;




import static com.thoaikx.driver.DriverManager.getDriver;
import static org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf;

import java.time.Duration;
import java.util.stream.Stream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumUtils {

  private SeleniumUtils(){}
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
