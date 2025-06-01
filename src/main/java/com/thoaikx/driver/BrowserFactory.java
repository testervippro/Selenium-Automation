package com.thoaikx.driver;

import com.thoaikx.exceptions.HeadlessNotSupportedException;
import com.thoaikx.listener.LoggingWebDriverListener;
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
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;

import java.nio.file.Path;

import static com.sun.jna.Platform.isMac;
import static com.sun.jna.Platform.isWindows;
import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.data.changeless.BrowserData.*;
import static java.lang.Boolean.TRUE;

public enum BrowserFactory {

    CHROME {
        @Override
        public WebDriver createLocalDriver() {
            WebDriverListener listener = new LoggingWebDriverListener();

            WebDriver chromeDriver = new ChromeDriver(getOptions());

            WebDriver decoratedDriver = new EventFiringDecorator(listener).decorate(chromeDriver);

            return decoratedDriver;
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
            chromeOptions.addArguments("--disable-dev-shm-usage");

            if (configuration().headless()) {
                // uncomment if you want headless
                chromeOptions.addArguments(CHROME_HEADLESS);
                if (isWindows() && configuration().recordHeadless())
                {
                    chromeOptions.addArguments("--remote-debugging-port=9222");
                    chromeOptions.addArguments("--user-data-dir=C:\\chrome-data");
                }
                if (isMac() && configuration().recordHeadless()) {
                    String userDataDir = System.getProperty("user.home") + "/chrome-data";
                   chromeOptions.addArguments("--remote-debugging-port=9200");
                   chromeOptions.addArguments("--user-data-dir=" +userDataDir);
                }
            }

            if (configuration().gridVideo()) {
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
            WebDriverListener listener = new LoggingWebDriverListener();

            WebDriver chromeDriver = new FirefoxDriver(getOptions());

            WebDriver decoratedDriver = new EventFiringDecorator(listener).decorate(chromeDriver);

            return decoratedDriver;

        }

        @Override
        public FirefoxOptions getOptions() {
            var firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments(START_MAXIMIZED);

            if (configuration().headless()) {
                firefoxOptions.addArguments(GENERIC_HEADLESS);
            }

            if (configuration().gridVideo()) {
                firefoxOptions.setCapability("se:recordVideo", true);
                firefoxOptions.setCapability("se:screenResolution", "1920x1080");
                firefoxOptions.setCapability("se:name", "test_visit_basic_auth_secured_page (FireFox)");
            }

            return firefoxOptions;
        }
    },

    EDGE {
        @Override
        public WebDriver createLocalDriver() {
            WebDriverManager.edgedriver().setup();
            WebDriverListener listener = new LoggingWebDriverListener();

            WebDriver chromeDriver = new EdgeDriver(getOptions());

            WebDriver decoratedDriver = new EventFiringDecorator(listener).decorate(chromeDriver);

            return decoratedDriver;

        }

        @Override
        public EdgeOptions getOptions() {
            var edgeOptions = new EdgeOptions();
            edgeOptions.addArguments(START_MAXIMIZED);
            edgeOptions.addArguments("--no-sandbox");

            if (configuration().headless()) {
                edgeOptions.addArguments(GENERIC_HEADLESS);
            }

            if (configuration().gridVideo()) {
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
            WebDriverManager.safaridriver().setup();
            return new SafariDriver(getOptions());
        }

        @Override
        public SafariOptions getOptions() {
            var safariOptions = new SafariOptions();
            safariOptions.setAutomaticInspection(false);

            if (TRUE.equals(configuration().headless())) {
                throw new HeadlessNotSupportedException(safariOptions.getBrowserName());
            }

            return safariOptions;
        }
    };

    public abstract WebDriver createLocalDriver();

    public abstract AbstractDriverOptions<?> getOptions();

    static class OS {
        public static boolean isLinux() {
            String os = System.getProperty("os.name").toLowerCase();
            return os.contains("linux");
        }
    }
}
