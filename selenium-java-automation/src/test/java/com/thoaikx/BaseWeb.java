
package com.thoaikx;

import com.thoaikx.driver.DriverManager;
import com.thoaikx.driver.TargetFactory;
import com.thoaikx.report.AllureManager;

import lombok.extern.log4j.Log4j2;
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

import java.io.IOException;
@Log4j2
public abstract class BaseWeb {

    @BeforeTest
    public void beforeSuite() {
        AllureManager.setAllureEnvironmentInformation();

    }

    @BeforeMethod
    @Parameters("browser")
    public void preCondition(@Optional("chrome") String browser) {
        WebDriver driver = new TargetFactory().createInstance(browser);
        DriverManager.setDriver(driver);
        DriverManager.getDriver().get(configuration().url());
    }

    @AfterTest()
    public void postCondition(){
        DriverManager.quit();

    }

}
