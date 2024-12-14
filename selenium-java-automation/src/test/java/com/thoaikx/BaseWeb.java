
package com.thoaikx;

import com.thoaikx.driver.DriverManager;
import com.thoaikx.driver.TargetFactory;
import com.thoaikx.report.AllureManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import static com.thoaikx.config.ConfigurationManager.configuration;

import java.io.IOException;

public abstract class BaseWeb {

    @BeforeClass
    public void beforeSuite() {
        AllureManager.setAllureEnvironmentInformation();

    }

    @BeforeMethod
    @Parameters("{browser}")
    public void preCondition(@Optional("edge") String browser) {
        var _brower = browser.toUpperCase();
        System.out.println("Browser parameter received: " + browser); // Debug output
        WebDriver driver = new TargetFactory().createInstance(_brower);
        DriverManager.setDriver(driver);
        DriverManager.getDriver().get(configuration().url());
    }

    @AfterSuite()
    public void postCondition() throws IOException, InterruptedException {
        DriverManager.quit();
        Thread.sleep(5000);
        AllureManager.generateReport();

    }

}
