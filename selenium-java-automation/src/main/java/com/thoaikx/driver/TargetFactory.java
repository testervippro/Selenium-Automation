
package com.thoaikx.driver;

import com.thoaikx.enums.Target;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

import static com.thoaikx.config.ConfigurationManager.configuration;
import static com.thoaikx.driver.BrowserFactory.valueOf;
import static java.lang.String.format;
import static java.util.Arrays.sort;

public class TargetFactory {

    private static final Logger logger = LogManager.getLogger(TargetFactory.class);

    public WebDriver createInstance(String browser) {
        // read targer in properties file

        Target target = Target.get(configuration().target().toUpperCase());
        System.out.println(target);

        // return base on target in properties file
        return switch (target) {
            case LOCAL -> valueOf(configuration().browser().toUpperCase()).createLocalDriver();
            case LOCAL_SUITE -> valueOf(browser.toUpperCase()).createLocalDriver();
            case SELENIUM_GRID -> createRemoteInstance(valueOf(browser.toUpperCase()).getOptions());
        };

    }

    private RemoteWebDriver createRemoteInstance(MutableCapabilities capability) {
        RemoteWebDriver remoteWebDriver = null;
        try {
            String gridURL = format("http://%s:%s", configuration().gridUrl(), configuration().gridPort());

            remoteWebDriver = new RemoteWebDriver(URI.create(gridURL).toURL(), capability);
        } catch (java.net.MalformedURLException e) {
            logger.error("Grid URL is invalid or Grid is not available");
            logger.error(format("Browser: %s", capability.getBrowserName()), e);
        } catch (IllegalArgumentException e) {
            logger.error(format("Browser %s is not valid or recognized", capability.getBrowserName()), e);
        }

        return remoteWebDriver;
    }
}
