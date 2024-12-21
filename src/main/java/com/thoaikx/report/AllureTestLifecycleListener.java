
package com.thoaikx.report;

import com.thoaikx.driver.DriverManager;
import io.qameta.allure.Attachment;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import static io.qameta.allure.model.Status.BROKEN;
import static io.qameta.allure.model.Status.FAILED;

/*
 * Approach implemented using the https://github.com/biczomate/allure-testng7.5-attachment-example as reference
 */
public class AllureTestLifecycleListener implements TestLifecycleListener {

    public AllureTestLifecycleListener() {
    }

    @Attachment(value = "Page Screenshot", type = "image/png")
    public byte[] saveScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void beforeTestStop(TestResult result) {
        if (FAILED == result.getStatus() || BROKEN == result.getStatus()) {
            saveScreenshot(DriverManager.getDriver());
        }
    }
}
