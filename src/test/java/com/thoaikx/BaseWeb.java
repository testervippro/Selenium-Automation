
package com.thoaikx;


import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.driver.DriverManager.getInfo;

import com.thoaikx.driver.DriverManager;
import com.thoaikx.driver.TargetFactory;
import com.thoaikx.pages.commons.CustomSelectActions;
import com.thoaikx.report.AllureManager;
import java.io.IOException;
import java.time.Duration;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Log4j2
public abstract class BaseWeb {
   protected WebDriver driver;
   protected CustomSelectActions select ;
   protected WebDriverWait wait;
  JavascriptExecutor jsExecutor ;


  @BeforeSuite
    public void beforeSuite() throws IOException {
      AllureManager.deleteOldReport();
      AllureManager.setAllureEnvironmentInformation();
      System.out.println("before suite called");
    }

    @BeforeTest
    @Parameters("browser")
    public void preCondition(@Optional("chrome") String browser) {
      driver = new TargetFactory().createInstance(browser);
      DriverManager.setDriver(driver);
      select = new CustomSelectActions(DriverManager.getDriver());
      wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(60_000));
      jsExecutor = (JavascriptExecutor) DriverManager.getDriver();
      log.info("Infor brower " + getInfo());

      DriverManager.getDriver().get(configuration().url());

    }


    @AfterTest()
    public void postCondition()  {

    DriverManager.quit();

    }
    @AfterSuite ()
    public void genReport()  {
    AllureManager.allureOpen();
    }

}
