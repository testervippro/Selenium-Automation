package com.thoaikx.listener;

import com.thoaikx.driver.DriverManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.WebDriverListener;

import java.lang.reflect.Method;

@Log4j2
public class LoggingWebDriverListener implements WebDriverListener {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";

    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (Object arg : args) {
            sb.append(arg).append(", ");
        }
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("]");
        return sb.toString();
    }

    private String getDriverInfo(WebDriver driver) {
        if (driver instanceof RemoteWebDriver remoteDriver) {
            Capabilities caps = remoteDriver.getCapabilities();
            String sessionId = remoteDriver.getSessionId().toString();
            return String.format("Browser: %s, Version: %s, Platform: %s, SessionId: %s",
                    caps.getBrowserName(),
                    caps.getBrowserVersion(),
                    caps.getPlatformName(),
                    sessionId);
        } else {
            return "Unknown WebDriver instance";
        }
    }

    private String getElementInfo(WebElement element) {
        try {
            return element.toString().replaceAll(".*-> ", "").replaceAll("]", "");
        } catch (Exception e) {
            return "Unknown Element";
        }
    }

    // -- WebDriver call hooks --

    @Override
    public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args) {
        log.info("BEFORE: Driver: {}{}{}, Method: {}, Args: {}", GREEN, driver, RESET, method.getName(), formatArgs(args));
    }

    @Override
    public void afterAnyWebDriverCall(WebDriver driver, Method method, Object[] args, Object result) {
        log.info("AFTER: Driver: {}{}{}, Method: {}, Args: {}, Result: {}", GREEN, driver, RESET, method.getName(), formatArgs(args), result);
    }



    // -- Click hooks --

    @Override
    public void beforeClick(WebElement element) {
        log.info("BEFORE click -> Element: {}", getElementInfo(element));
    }

    @Override
    public void afterClick(WebElement element) {
        log.info("AFTER click -> Element: {}", getElementInfo(element));
    }

    // -- FindElement hooks --

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
        log.info("BEFORE findElement -> Locator: {}", locator);
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        log.info("AFTER findElement -> Locator: {}, Result: {}", locator, getElementInfo(result));
    }
}
