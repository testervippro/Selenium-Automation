
package com.thoaikx;


import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.driver.DriverManager.getInfo;

import com.thoaikx.driver.DriverManager;
import com.thoaikx.driver.TargetFactory;
import com.thoaikx.pages.commons.CustomSelectActions;
import com.thoaikx.report.AllureManager;

import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
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
  public void startGrid() throws IOException {

    AllureManager.setAllureEnvironmentInformation();

    Thread thread = new Thread(() -> {
      try {
        CommandLine runGrid = CommandLine.parse("java -jar grid/selenium-server-4.30.0.jar standalone");

        DefaultExecutor executorRunGrid = new DefaultExecutor();
        executorRunGrid.setStreamHandler(new PumpStreamHandler(System.out));
        executorRunGrid.execute(runGrid);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    thread.setDaemon(true);
    thread.start();

    // Optional wait to let the grid boot up before tests start
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
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


  @AfterSuite ()
  public void genReport() throws IOException {
    DriverManager.quit();

  }
  private String generateVideoName(String browserName) {
    String randomPart = UUID.randomUUID().toString().replace("-", "");
    return browserName + "_" + randomPart;
  }

}