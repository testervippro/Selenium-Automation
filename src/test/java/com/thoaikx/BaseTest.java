
package com.thoaikx;


import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.driver.DriverManager.getInfo;

import com.thoaikx.common.TestUtils;
import com.thoaikx.driver.DriverManager;
import static com.thoaikx.driver.DriverManager.getDriver;

import com.thoaikx.driver.TargetFactory;
import com.thoaikx.pages.commons.CustomSelectActions;

import java.io.IOException;
import java.time.Duration;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

@Log4j2
public abstract class BaseTest {

  private int TIMEOUT = configuration().timeout();
  protected WebDriver driver;
  protected CustomSelectActions select;
  protected WebDriverWait wait;
  protected JavascriptExecutor jsExecutor;


  @BeforeSuite()
  public void startGrid() throws IOException, InterruptedException {

    //TestUtils.startSeleniumGrid();
  }


  @BeforeTest
  @Parameters("browser")
  public void preCondition(@Optional("chrome") String browser) {

    driver = new TargetFactory().createInstance(browser);
    DriverManager.setDriver(driver);
    select = new CustomSelectActions(getDriver());
    wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TIMEOUT));
    jsExecutor = (JavascriptExecutor) getDriver();
    log.info("Infor brower " + getInfo());

    getDriver().get(configuration().url());
    //getDriver().manage().window().fullscreen();

  }

  @AfterTest()
  public void tearDownAll() throws IOException, InterruptedException {
    TestUtils.attachLog();
    TestUtils.attachVideo();
    DriverManager.quit();

  }
}


