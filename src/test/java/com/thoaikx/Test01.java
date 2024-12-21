package com.thoaikx;

import static com.thoaikx.ultis.SeleniumUtils.explicitWaitElement;

import com.thoaikx.report.AllureManager;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


@Log4j2
public class Test01 extends BaseWeb {


    @Test
    public void test02() throws InterruptedException {
        Thread.sleep(3000);

    }
}
