
package com.thoaikx;


import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.driver.DriverManager.getInfo;

import com.thoaikx.driver.DriverManager;
import com.thoaikx.driver.TargetFactory;
import com.thoaikx.pages.commons.CustomSelectActions;
import com.thoaikx.record.RecordVideo;
import com.thoaikx.report.AllureManager;

import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

@Log4j2
public abstract class BaseTest {


   private  int TIMEOUT = configuration().timeout();
   protected WebDriver driver;
   protected CustomSelectActions select ;
   protected WebDriverWait wait;
    protected JavascriptExecutor jsExecutor ;
  @BeforeSuite
    public void beforeSuite() throws IOException {
      AllureManager.deleteOldReport();
      AllureManager.setAllureEnvironmentInformation();
    //  DockerManager.executeCommandAndWaitForStart(commandUp,getCommandUpUntik);

    }

    @BeforeTest
    @Parameters("browser")
    public void preCondition(@Optional("chrome") String browser) {
      driver = new TargetFactory().createInstance(browser);

      DriverManager.setDriver(driver);
      select = new CustomSelectActions(DriverManager.getDriver());
      wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(TIMEOUT));
      jsExecutor = (JavascriptExecutor) DriverManager.getDriver();
      log.info("Infor brower " + getInfo());

      DriverManager.getDriver().get(configuration().url());

    }


    @AfterTest()
    public void postCondition()  {
    DriverManager.quit();
    }
    @AfterSuite ()
    public void genReport() throws IOException {

    if ((configuration().autoOpenReport()) & !GraphicsEnvironment.isHeadless()){
        AllureManager.allureOpen();
      }
    }
  private String generateVideoName(String browserName) {
    String randomPart = UUID.randomUUID().toString().replace("-", "");
    return browserName + "_" + randomPart;
  }

}
