package com.thoaikx;

import static com.thoaikx.config.ConfigurationManager.configuration;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.thoaikx.driver.DriverManager;
import com.thoaikx.driver.TargetFactory;
import com.thoaikx.report.AllureManager;

import io.qameta.allure.Allure;

import static org.junit.Assert.assertEquals;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

import org.testng.asserts.Assertion;

public class Test01 extends BaseWeb {
    private static final String GLOBAL_PARAMETER = "global value";

    @Test

    public void test01() {
        Allure.step("check brower " + DriverManager.getDriver().getClass().getSimpleName());

    }

    @Test
    public void lambdaStepTest() {

        System.out.println(DriverManager.getDriver().getClass().getSimpleName());
        final String localParameter = "parameter value";
        Allure.step(String.format("Parent lambda step with parameter [%s]", localParameter), (step) -> {
            step.parameter("parameter", localParameter);
            Allure.step(String.format("Nested lambda step with global parameter [%s]", GLOBAL_PARAMETER));
        });
    }

}
