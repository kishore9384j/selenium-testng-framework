package com.saucedemo.automation.tests;

import com.saucedemo.automation.base.BaseTest;
import com.saucedemo.automation.pages.*;
import com.saucedemo.automation.utils.ConfigReader;
import com.saucedemo.automation.utils.ExcelUtility;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class E2ECheckoutTest extends BaseTest {

    // ---------------------------------------------------------------
    // TC005 — Full E2E: Login → Add to Cart → Checkout → Confirm
    // This is the STAR test of your framework — demo this in interviews
    // ---------------------------------------------------------------
    @Test(priority = 1,
          description = "E2E: Login, add product to cart, checkout and verify confirmation",
          retryAnalyzer = com.saucedemo.automation.listeners.RetryAnalyzer.class)
    public void testCompleteCheckoutFlow() {

        // STEP 1 — Login
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page must be visible before test starts");

        ProductsPage productsPage = loginPage.loginWithValidCredentials(
                ConfigReader.getUsername(),
                ConfigReader.getPassword()
        );

        // STEP 2 — Verify on Products page
        Assert.assertTrue(productsPage.isProductsPageDisplayed(),
                "Should land on Products page after login");
        Assert.assertTrue(productsPage.getProductCount() > 0,
                "Products page should have at least one product");

        // STEP 3 — Add product to cart
        productsPage.addBackpackToCart();
        Assert.assertEquals(productsPage.getCartCount(), "1",
                "Cart should show 1 item after adding backpack");

        // STEP 4 — Go to Cart
        CartPage cartPage = productsPage.goToCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(),
                "Cart page should be displayed");
        Assert.assertEquals(cartPage.getCartItemCount(), 1,
                "Cart should contain exactly 1 item");
        Assert.assertTrue(
                cartPage.isItemInCart("Sauce Labs Backpack"),
                "Backpack should be in cart"
        );

        // STEP 5 — Checkout
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        Assert.assertTrue(checkoutPage.isCheckoutStepOneDisplayed(),
                "Checkout step 1 should be displayed");

        // STEP 6 — Fill shipping info and place order
        ConfirmationPage confirmationPage = checkoutPage.fillShippingAndContinue(
                "Rahul", "Sharma", "600001"
        );

        // STEP 7 — Verify order confirmed
        Assert.assertTrue(confirmationPage.isOrderConfirmed(),
                "Order confirmation should be displayed");
        Assert.assertEquals(
                confirmationPage.getConfirmationHeader(),
                "Thank you for your order!",
                "Confirmation header text mismatch"
        );
        Assert.assertTrue(confirmationPage.isConfirmationImageDisplayed(),
                "Confirmation image should be visible"
        );
    }

    // ---------------------------------------------------------------
    // TC006 — Add multiple products and verify cart count
    // ---------------------------------------------------------------
    @Test(priority = 2,
          description = "Verify multiple products can be added to cart")
    public void testAddMultipleProductsToCart() {

        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage.loginWithValidCredentials(
                ConfigReader.getUsername(),
                ConfigReader.getPassword()
        );

        productsPage.addBackpackToCart();
        productsPage.addBikeLightToCart();
        productsPage.addBoltTshirtToCart();

        Assert.assertEquals(productsPage.getCartCount(), "3",
                "Cart should show 3 items");

        CartPage cartPage = productsPage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 3,
                "Cart page should show 3 items");
    }

    // ---------------------------------------------------------------
    // TC007 — Data driven checkout using Excel
    // ---------------------------------------------------------------
    @Test(priority = 3,
          description = "Data driven checkout with multiple shipping details",
          dataProvider = "checkoutDataProvider")
    public void testCheckoutWithExcelData(String firstName,
                                          String lastName,
                                          String postalCode) {

        LoginPage loginPage     = new LoginPage(getDriver());
        ProductsPage products   = loginPage.loginWithValidCredentials(
                ConfigReader.getUsername(), ConfigReader.getPassword());

        products.addBackpackToCart();
        CartPage cart           = products.goToCart();
        CheckoutPage checkout   = cart.proceedToCheckout();
        ConfirmationPage confirm = checkout.fillShippingAndContinue(
                firstName, lastName, postalCode);

        Assert.assertTrue(confirm.isOrderConfirmed(),
                "Order should be confirmed for: " + firstName + " " + lastName);
    }

    // ---------------------------------------------------------------
    // TC008 — Logout test
    // ---------------------------------------------------------------
    @Test(priority = 4,
          description = "Verify user can logout successfully")
    public void testLogout() {

        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage.loginWithValidCredentials(
                ConfigReader.getUsername(), ConfigReader.getPassword());

        Assert.assertTrue(productsPage.isProductsPageDisplayed(),
                "Must be on Products page before logout");

        LoginPage loginPageAfterLogout = productsPage.logout();

        Assert.assertTrue(loginPageAfterLogout.isLoginPageDisplayed(),
                "Should be back on Login page after logout");
    }

    @DataProvider(name = "checkoutDataProvider")
    public Object[][] getCheckoutData() {
        return ExcelUtility.getTestData("CheckoutData");
    }
}