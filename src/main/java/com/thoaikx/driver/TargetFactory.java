
package com.thoaikx.driver;

import com.thoaikx.enums.Target;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.driver.BrowserFactory.CHROME;
import static com.thoaikx.driver.BrowserFactory.EDGE;
import static com.thoaikx.driver.BrowserFactory.FIREFOX;
import static com.thoaikx.driver.BrowserFactory.SAFARI;
import static com.thoaikx.driver.BrowserFactory.valueOf;
import static com.thoaikx.driver.DriverManager.getInfo;
import static com.thoaikx.enums.Target.LOCAL_SUITE;
import static java.lang.String.format;
import static java.util.Arrays.sort;


@Log4j2
public class TargetFactory {

    public WebDriver createInstance(String browser) {
        Target target = Target.get(configuration().target().toUpperCase());
        log.info("Target is " + target);

        // Return the appropriate WebDriver instance based on the target
        return switch (target) {
            case LOCAL -> valueOf(configuration().browser().toUpperCase()).createLocalDriver();
            case LOCAL_SUITE ->valueOf(browser.toUpperCase()).createLocalDriver();

           case SELENIUM_GRID ->
                     createRemoteInstance(valueOf(browser.toUpperCase()).getOptions());


        };
    }

    private RemoteWebDriver createRemoteInstance(MutableCapabilities capability) {
        RemoteWebDriver remoteWebDriver = null;

        try {
            String gridURL = format("http://%s:%s", configuration().gridUrl(), configuration().gridPort());

            remoteWebDriver = new RemoteWebDriver(URI.create(gridURL).toURL(), capability);
        } catch (java.net.MalformedURLException e) {
            log.error("Grid URL is invalid or Grid is not available");
            log.error(format("Browser: %s", capability.getBrowserName()), e);
        } catch (IllegalArgumentException e) {
            log.error(format("Browser %s is not valid or recognized", capability.getBrowserName()), e);
        }

        return remoteWebDriver;
    }

}
