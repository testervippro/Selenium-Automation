
package com.thoaikx;


import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.driver.DriverManager.getInfo;

import com.thoaikx.common.TestUtils;
import com.thoaikx.driver.DriverManager;
import static com.thoaikx.driver.DriverManager.getDriver;

import com.thoaikx.driver.TargetFactory;
import com.thoaikx.pages.commons.CustomSelectActions;

import java.io.IOException;
import java.io.OutputStream;
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

  private final int TIMEOUT = configuration().timeout();
  protected WebDriver driver;
  protected CustomSelectActions select;
  protected WebDriverWait wait;
  protected JavascriptExecutor jsExecutor;


  @BeforeSuite()
  public void setUp() throws IOException, InterruptedException {

    Thread screencastThread = new Thread(() -> {
      executeCMDInSilentMode("chmod +x ./screencastMacIntel");
      executeCMDInSilentMode("./screencastMacIntel -folder ./images");

    });

    screencastThread.setDaemon(true); // Doesn’t block JVM exit
    screencastThread.start();
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
    Thread.sleep(4000);
    DriverManager.quit();

    // Hardcoded FFmpeg command for mac
    String cmdConvert = "ffmpeg -y -framerate 25 -i images/screenshot_%06d.png "
            + "-vf scale=trunc(iw/2)*2:trunc(ih/2)*2 -c:v libx264 -pix_fmt yuv420p output.mp4";

    executeCMDInSilentMode(cmdConvert);

  }




  public static void executeCMDInSilentMode(String command) {
    try {
      CommandLine cmd = CommandLine.parse(command);

      OutputStream nullStream = OutputStream.nullOutputStream(); // Java 11+
      PumpStreamHandler silentHandler = new PumpStreamHandler(nullStream, nullStream);

      DefaultExecutor executor = new DefaultExecutor();
      executor.setStreamHandler(silentHandler);
      executor.setExitValues(null); // Don’t throw exception on non-zero exit

      executor.execute(cmd);

    } catch (Exception e) {
      e.printStackTrace(); // Or log it instead
    }
  }

}


