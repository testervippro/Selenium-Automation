package com.thoaikx;

import static com.thoaikx.ultis.RecordUtils.startRecordATU;
import static org.testng.AssertJUnit.assertEquals;

import com.thoaikx.dataprovider.Fixture;
import com.thoaikx.pages.HomePage;
import com.thoaikx.pages.ProductPage;
import com.thoaikx.pages.commons.CustomSelectActions;
import com.thoaikx.ultis.RecordUtils;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


@Log4j2
public class HomePageTest extends BaseWeb {


    @BeforeClass
    public void  start() throws Exception {

        startRecordATU("video-01");
    }

    @AfterClass
    public void  stop() throws Exception {
        RecordUtils.stopRecordATU();
    }

    @Test(priority = 0)
    public void test02()  {
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

    @Test(dataProvider = "csv",dataProviderClass = Fixture.class)
    public  void  testDataProvider(String name , String age , String city) {
        System.out.println("Paremeter is" + name + age + city);
    }
}
