
package com.thoaikx.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();


    private DriverManager() {}

    public static WebDriver getDriver() {
        return driver.get();
    }


    public static void setDriver(WebDriver driver) {
        DriverManager.driver.set(driver);
    }

    public static void quit() {
        DriverManager.driver.get().quit();
        driver.remove();
    }


    public static String getInfo() {
        // RemoteWebDriver extends WebDriver
        /*
         java.lang.ClassCastException: class net.bytebuddy.renamed.java.lang.Object$ByteBuddy$WOcElIPs cannot be cast to class org.openqa.selenium.remote.RemoteWebDriver (net.bytebuddy.renamed.java.lang.Object$ByteBuddy$WOcElIPs is in unnamed module of loader net.bytebuddy.dynamic.loading.ByteArrayClassLoader @34780cd9;
         org.openqa.selenium.remote.RemoteWebDriver is in unnamed module of loader 'app')

          */
        Capabilities cap;

        if (getDriver() instanceof RemoteWebDriver) {
            cap = ((RemoteWebDriver) getDriver()).getCapabilities();
            System.out.println("Browser: " + cap.getBrowserName());
        } else {
            System.out.println("Driver is not a RemoteWebDriver");
            return "Unknown browser info";
        }

        String browserName = cap.getBrowserName();
        String platform = cap.getPlatformName().toString();
        String version = cap.getBrowserVersion();

        return String.format("browser: %s v: %s platform: %s", browserName, version, platform);
    }

}
