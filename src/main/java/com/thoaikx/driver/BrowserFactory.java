
package com.thoaikx.driver;

import com.thoaikx.exceptions.HeadlessNotSupportedException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;


import java.nio.file.Path;

import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.data.changeless.BrowserData.*;
import static java.lang.Boolean.TRUE;

public enum BrowserFactory {



    CHROME {
        @Override
        public WebDriver createLocalDriver() {

            WebDriver driver;

            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(getOptions());
            return  driver;

        }

        @Override
        public ChromeOptions getOptions() {
            var chromeOptions = new ChromeOptions();
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            chromeOptions.addArguments(START_MAXIMIZED);
            chromeOptions.addArguments(DISABLE_INFOBARS);
            chromeOptions.addArguments(DISABLE_NOTIFICATIONS);
            chromeOptions.addArguments(REMOTE_ALLOW_ORIGINS);
            chromeOptions.addArguments("--no-sandbox");
            //https://github.com/SeleniumHQ/seleniumhq.github.io/pull/2139


            if (configuration().headless())
                chromeOptions.addArguments(CHROME_HEADLESS);


            if(configuration().gridVideo()) {
                chromeOptions.setCapability("se:recordVideo", true);
                chromeOptions.setCapability("se:screenResolution", "1920x1080");
                chromeOptions.setCapability("se:name", "test_visit_basic_auth_secured_page (ChromeTests)");
            }


            return chromeOptions;
        }

    },
    FIREFOX {
        @Override
        public WebDriver createLocalDriver() {

            WebDriver driver;

            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver(getOptions());
            return  driver;

        }

        @Override
        public FirefoxOptions getOptions() {
            var firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments(START_MAXIMIZED);

            if (configuration().headless())
                firefoxOptions.addArguments(GENERIC_HEADLESS);

            if(configuration().gridVideo()) {
                firefoxOptions.setCapability("se:recordVideo", true);
                firefoxOptions.setCapability("se:screenResolution", "1920x1080");
                firefoxOptions.setCapability("se:name", "test_visit_basic_auth_secured_page (FireFox)");
            }

            return firefoxOptions;
        }
    },
    EDGE {
        @Override
        public WebDriver createLocalDriver()  {

            WebDriver driver;

            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver(getOptions());
            return  driver;

        }

        @Override
        public EdgeOptions getOptions() {
            var edgeOptions = new EdgeOptions();
            edgeOptions.addArguments(START_MAXIMIZED);
            edgeOptions.addArguments("--no-sandbox");
            //

            if (configuration().headless())
                edgeOptions.addArguments(GENERIC_HEADLESS);


            if(configuration().gridVideo()) {
                edgeOptions.setCapability("se:recordVideo", true);
                edgeOptions.setCapability("se:screenResolution", "1920x1080");
                edgeOptions.setCapability("se:name", "test_visit_basic_auth_secured_page (EdgeTest)");
            }

            return edgeOptions;
        }
    },
    SAFARI {
        @Override
        public WebDriver createLocalDriver() {
            WebDriver driver;

            WebDriverManager.safaridriver().setup();
            driver = new SafariDriver(getOptions());
            return  driver;

        }

        @Override
        public SafariOptions getOptions() {
            var safariOptions = new SafariOptions();
            safariOptions.setAutomaticInspection(false);

            if (TRUE.equals(configuration().headless()))
                throw new HeadlessNotSupportedException(safariOptions.getBrowserName());

            return safariOptions;
        }
    };

    /**
     * Used to run local tests where the WebDriverManager will take care of the
     * driver
     *
     * @return a new WebDriver instance based on the browser set
     */
    public abstract WebDriver createLocalDriver() ;

    /**
     * @return a new AbstractDriverOptions instance based on the browser set
     */
    public abstract AbstractDriverOptions<?> getOptions();

    static class OS {
        public static boolean isLinux() {
            String os = System.getProperty("os.name").toLowerCase();
            return os.contains("linux");
        }
    }
}


