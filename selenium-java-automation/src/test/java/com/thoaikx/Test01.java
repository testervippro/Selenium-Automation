package com.thoaikx;

import static com.thoaikx.ultis.SeleniumUtils.explicitWaitElement;

import com.thoaikx.report.AllureManager;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;


@Log4j2
public class Test01 extends BaseWeb {

//mvn clean install; mvn allure:serve
    @Test
    public void test02() {
//       var element = driver.findElement(By.xpath("/html/body/div/header/div/div[2]/nav/div/ul/li[5]/a"));;
//        explicitWaitElement(element);
//        element.click();
//        log.info(element.getText());
    }
}
