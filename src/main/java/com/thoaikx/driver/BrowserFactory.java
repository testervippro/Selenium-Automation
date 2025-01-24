
package com.thoaikx.driver;

import com.thoaikx.exceptions.HeadlessNotSupportedException;
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

              return new ChromeDriver(getOptions());


        }

        @Override
        public ChromeOptions getOptions() {
            var chromeOptions = new ChromeOptions();
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            chromeOptions.addArguments(START_MAXIMIZED);
            chromeOptions.addArguments(DISABLE_INFOBARS);
            chromeOptions.addArguments(DISABLE_NOTIFICATIONS);
            chromeOptions.addArguments(REMOTE_ALLOW_ORIGINS);
            chromeOptions.addArguments("--incognito");
            chromeOptions.addArguments("--no-sandbox");


            if (configuration().headless())
                chromeOptions.addArguments(CHROME_HEADLESS);


            return chromeOptions;
        }

    },
    FIREFOX {
        @Override
        public WebDriver createLocalDriver() {
            return new FirefoxDriver(getOptions());
        }

        @Override
        public FirefoxOptions getOptions() {
            var firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments(START_MAXIMIZED);

            if (configuration().headless())
                firefoxOptions.addArguments(GENERIC_HEADLESS);

            return firefoxOptions;
        }
    },
    EDGE {
        @Override
        public WebDriver createLocalDriver()  {

            // Set the path to the Edge WebDriver binary (if not in PATH)
          //  System.setProperty("webdriver.edge.driver", "/usr/local/bin/msedgedriver");

            // Initialize EdgeDriver
            WebDriver driver = new EdgeDriver(getOptions());

            return driver;

        }

        @Override
        public EdgeOptions getOptions() {
            var edgeOptions = new EdgeOptions();
            edgeOptions.addArguments(START_MAXIMIZED);
            edgeOptions.addArguments("--incognito");
            edgeOptions.addArguments("--no-sandbox");



            if (configuration().headless())
                edgeOptions.addArguments(GENERIC_HEADLESS);

            return edgeOptions;
        }
    },
    SAFARI {
        @Override
        public WebDriver createLocalDriver() {
            return new SafariDriver(getOptions());
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

}

