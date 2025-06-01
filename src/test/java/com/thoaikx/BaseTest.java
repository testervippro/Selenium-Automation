
package com.thoaikx;


import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.driver.DriverManager.getInfo;

import com.thoaikx.driver.DriverManager;
import static com.thoaikx.driver.DriverManager.getDriver;
import static com.thoaikx.record.RecorderManager.HeadlessUtils.startChromeInHeadlessModeMAC;

import com.thoaikx.driver.TargetFactory;
import com.thoaikx.pages.commons.CustomSelectActions;
import com.thoaikx.report.AllureManager;

import java.io.IOException;
import java.time.Duration;

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

  private int TIMEOUT = configuration().timeout();
  protected WebDriver driver;
  protected CustomSelectActions select;
  protected WebDriverWait wait;
  protected JavascriptExecutor jsExecutor;


  @BeforeSuite()
  public void startGrid() throws IOException, InterruptedException {

    //startChromeInHeadlessModeMAC();

    if (configuration().target().equalsIgnoreCase("selenium-grid -")) {
      killProcessOnPort(4444);

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
    DriverManager.quit();

  }

  private void killProcessOnPort(int port) {
    String os = System.getProperty("os.name").toLowerCase();

    try {
      if (os.contains("win")) {
        // For Windows
        String command = String.format("cmd /c for /f \"tokens=5\" %%a in ('netstat -aon ^| findstr :%d') do taskkill /f /pid %%a", port);
        Runtime.getRuntime().exec(command);
      } else {
        // For Unix/Linux/Mac
        String[] command = {"/bin/sh", "-c", String.format("lsof -ti:%d | xargs kill -9", port)};
        Runtime.getRuntime().exec(command);
      }

      // Wait a moment to ensure the port is freed
      Thread.sleep(1000);
    } catch (Exception e) {
      System.err.println("Failed to kill process on port " + port);
      e.printStackTrace();
    }
  }
}


