package com.thoaikx;

import lombok.extern.log4j.Log4j2;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.thoaikx.driver.DriverManager;
import com.thoaikx.driver.TargetFactory;
import com.thoaikx.report.AllureManager;

import io.qameta.allure.Allure;



import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

import org.testng.asserts.Assertion;


@Log4j2
public class Test01 extends BaseWeb {
    private static final String GLOBAL_PARAMETER = "global value";

    @Test
    public void test01() {

    }



}
