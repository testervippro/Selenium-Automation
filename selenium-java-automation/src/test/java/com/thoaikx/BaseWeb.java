
package com.thoaikx;

import com.thoaikx.driver.DriverManager;
import com.thoaikx.driver.TargetFactory;
import com.thoaikx.report.AllureManager;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.driver.DriverManager.getInfo;

import java.io.IOException;
import ru.yandex.qatools.allure.report.AllureReportBuilder;
import ru.yandex.qatools.allure.report.AllureReportBuilderException;

@Log4j2
public abstract class BaseWeb {
   public WebDriver driver;
    @BeforeSuite
    public void beforeSuite() throws IOException {
        AllureManager.deleteOldReport();
        AllureManager.setAllureEnvironmentInformation();
    }

    @BeforeTest
    @Parameters("browser")
    public void preCondition(String browser) {
        driver = new TargetFactory().createInstance(browser);
        DriverManager.setDriver(driver);
        log.info("Infor brower " +getInfo());
        DriverManager.getDriver().get(configuration().url());
    }

    @AfterTest()
    public void postCondition() {
        DriverManager.quit();

    }
    @AfterSuite ()
    public void genReport() throws IOException, InterruptedException {
      AllureManager.allureOpen();
    }


}
