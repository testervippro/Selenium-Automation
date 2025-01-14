package com.thoaikx;

import static com.thoaikx.utils.RecordUtils.startRecordATU;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

import static org.testng.AssertJUnit.assertEquals;

import com.thoaikx.pages.HomePage;
import com.thoaikx.pages.ProductPage;
import java.util.Iterator;
import java.util.List;

import com.thoaikx.utils.RecordUtils;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


@Log4j2
public class HomePageTest extends BaseWeb {


    @BeforeClass
    public void  start() throws Exception {
        // Check the operating system
        String osName = System.getProperty("os.name").toLowerCase();
        // Check if the system is in a headless environment (no GUI available)
        boolean isHeadless = java.awt.GraphicsEnvironment.isHeadless();

        // If the OS is not Linux and the system is not headless, execute startRecordATU
        if (!osName.contains("linux") && !isHeadless) {
            startRecordATU("video-01");
        } else if (osName.contains("linux") && isHeadless) {
            System.out.println("Video recording is disabled on Linux due to headless environment.");
        } else {
            System.out.println("Video recording is disabled on a headless environment.");
        }

    }

    @AfterClass
    public void  stop() throws Exception {
        // Check the operating system
        String osName = System.getProperty("os.name").toLowerCase();
        // Check if the system is in a headless environment (no GUI available)
        boolean isHeadless = java.awt.GraphicsEnvironment.isHeadless();

        // If the OS is not Linux and the system is not headless, execute startRecordATU
        if (!osName.contains("linux") && !isHeadless) {
            RecordUtils.stopRecordATU();
        } else if (osName.contains("linux") && isHeadless) {
            System.out.println("Video recording is disabled on Linux due to headless environment.");
        } else {
            System.out.println("Video recording is disabled on a headless environment.");
        }


    }

    @Test(priority = 0)
    public void homePage()  {
        HomePage homePage = new HomePage(driver);
        homePage.getEditBox().sendKeys("bob");
        var expectValue  = homePage.getTwoWayDataBinding().getAttribute("value");
        Assert.assertEquals(expectValue,"bob");

        homePage.getGender().click();
        select.selectByVisibleText(homePage.getGender(),"Female");
        var expectEntre = homePage.getEntrepreneur().getAttribute("disabled");
        Assert.assertEquals(expectEntre,"true");

        homePage.getShopTab().click();


       // var expectValue  = homePage.getTwoWayDataBinding().getDomAttribute("value"); returm null ??


    }

    @DataProvider(name = "product-name")
    public Iterator<String> createData() {
      return List.of("Blackberry", "Nokia Edge").iterator();
    }


    @Test (dataProvider = "product-name",priority = 1)
    public  void  selectProduct (String name) {
        ProductPage productPage = new ProductPage(driver);
        productPage.selectProduct(name);

    }

    @Test(priority = 2)
    public  void  checkOut() throws InterruptedException {
        ProductPage productPage = new ProductPage(driver);
        productPage.getCheckOutButton().click();
        // Locate elements and sum their values
        int sum = driver.findElements(By.cssSelector("tr td:nth-child(4) strong")).stream()
            .mapToInt(element -> Integer.parseInt(element.getText().replaceAll("[^0-9]", ""))) // Parse text to integer
            .reduce(0, Integer::sum);


        int total = Integer.parseInt(driver.findElement(By.cssSelector("h3 strong")).getText().replaceAll("[^0-9]",""));

        Assert.assertEquals(sum, total,"Check sum of each proudct = Total ");

        driver.findElement(By.xpath("//*[contains(text() ,'Checkout')]")).click();

        driver.findElement(By.id("country")).sendKeys("India");
        WebElement suggestions  = wait.until(visibilityOfElementLocated(By.className("suggestions")));
        suggestions.findElement(By.cssSelector("ul > li > a")).click();

        WebElement checkBox2 = driver.findElement(By.xpath("//*[@id ='checkbox2']"));
        jsExecutor.executeScript("arguments[0].click();",checkBox2 );


        // Submit the form
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        WebElement alert = wait.until(visibilityOfElementLocated(By.cssSelector(".alert")));
        String actualText = alert.getText();

        Assert.assertTrue(actualText.contains("Success"));
    }



//    @Test(dataProvider = "csv",dataProviderClass = Fixture.class)
//    public  void  testDataProvider(String name , String age , String city) {
//        System.out.println("Paremeter is" + name + age + city);
//    }
}
