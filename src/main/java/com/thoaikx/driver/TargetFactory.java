
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
//                if (configuration().separatePort()) {
//                    // Use separate ports for each browser
//                    yield createRemoteInstanceSepratePortInEachBrower(valueOf(browser.toUpperCase()).getOptions(), browser);
//                } else {
                    // Use the default Grid URL from configuration
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

    //each brower use diff port

    private   static RemoteWebDriver createRemoteInstanceSepratePortInEachBrower(MutableCapabilities capability, String browser) {
        try {
            // Define the Selenium Grid URL based on the browser
            String gridURL = switch (browser.toLowerCase()) {
                case "chrome" -> "http://localhost:4444"; // Chrome standalone
                case "firefox" -> "http://localhost:4445"; // Firefox standalone
                case "edge" -> "http://localhost:4446";   // Edge standalone
                default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
            };

            // Create and return the RemoteWebDriver instance
            return new RemoteWebDriver(new URL(gridURL), capability);
        } catch (MalformedURLException e) {
            System.err.println("Grid URL is invalid or Grid is not available for browser: " + capability.getBrowserName());
            e.printStackTrace();
            return null;
        }
    }

    private static BrowserFactory ValueOf(String browser) {
        return switch (browser) {
            case "CHROME" -> CHROME;
            case "FIREFOX" -> FIREFOX;
            case "EDGE" -> EDGE;
            case "SAFARI" -> SAFARI;
            default -> throw new IllegalArgumentException("Unknown browser: " + browser);
        };
    }

}
