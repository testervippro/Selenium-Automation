package com.thoaikx;

import com.thoaikx.BaseTest;
import com.thoaikx.pages.CheckoutPage;
import com.thoaikx.pages.HomePage;
import com.thoaikx.pages.ProductPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.thoaikx.driver.DriverManager.getDriver;

public class ShoppingFlowTest extends BaseTest {

    @Test
    public void completeShoppingFlow() {
        // Step 1: Interact with HomePage
        HomePage homePage = new HomePage(getDriver());
        homePage.enterName("bob");
        Assert.assertEquals(homePage.getTwoWayDataBindingValue(), "bob");

        homePage.selectGender("Female");
        Assert.assertTrue(homePage.isEntrepreneurDisabled(), "Entrepreneur radio should be disabled");

        homePage.clickShopTab();

        // Step 2: Select Products
        ProductPage productPage = new ProductPage(getDriver());
        productPage.addProductToCart("Blackberry");
        productPage.addProductToCart("Nokia Edge");

        // Step 3: Checkout Process
        productPage.clickCheckout();
        Assert.assertEquals(productPage.calculateItemsTotal(), productPage.getDisplayedTotal(),
                "Check sum of each product = Total");

        CheckoutPage checkoutPage = new CheckoutPage(getDriver());
        checkoutPage.proceedToFinalCheckout();
        checkoutPage.enterCountry("India");
        checkoutPage.selectCountryFromSuggestions();
        checkoutPage.agreeToTerms();
        checkoutPage.submitOrder();

        String successMessage = checkoutPage.getSuccessMessage();
        Assert.assertTrue(successMessage.contains("Success"), "Expected success message not found");
    }
}