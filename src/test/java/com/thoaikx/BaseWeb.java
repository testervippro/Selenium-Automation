
package com.thoaikx;


import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.driver.DriverManager.getInfo;

import com.thoaikx.driver.DriverManager;
import com.thoaikx.driver.TargetFactory;
import com.thoaikx.pages.commons.CustomSelectActions;
import com.thoaikx.processsbuilder.DockerManager;
import com.thoaikx.report.AllureManager;
import java.io.IOException;
import java.time.Duration;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

@Log4j2
public abstract class BaseWeb {


  String composeFile = "docker-compose-standalone-chrome.yml"; // Path to your docker-compose file
  String commandUp = "docker-compose -f " + composeFile + " up"; // Command to start the Selenium Chrome container
  String commandDown = "docker-compose -f " + composeFile + " down"; //

  String getCommandUpUntik = "Started Selenium Standalone";

  private   int TIMEOUT = configuration().timeout();
   protected WebDriver driver;
   protected CustomSelectActions select ;
   protected WebDriverWait wait;
  JavascriptExecutor jsExecutor ;


  @BeforeSuite
    public void beforeSuite() throws IOException {
      AllureManager.deleteOldReport();
      AllureManager.setAllureEnvironmentInformation();
      DockerManager.executeCommandAndWaitForStart(commandUp,getCommandUpUntik);

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
    DockerManager.executeCommand(commandDown);
    AllureManager.allureOpen();
    }



}
