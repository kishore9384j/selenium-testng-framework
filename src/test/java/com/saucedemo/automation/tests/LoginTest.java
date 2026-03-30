package com.saucedemo.automation.tests;

import com.saucedemo.automation.base.BaseTest;
import com.saucedemo.automation.pages.LoginPage;
import com.saucedemo.automation.pages.ProductsPage;
import com.saucedemo.automation.utils.ExcelUtility;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    // ---------------------------------------------------------------
    // TC001 — Valid login with credentials from config.properties
    // Priority 1 = runs first
    // ---------------------------------------------------------------
    @Test(priority = 1,
          description = "Verify successful login with valid credentials",
          retryAnalyzer = com.saucedemo.automation.listeners.RetryAnalyzer.class)
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(getDriver());

        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Login page should be displayed");

        ProductsPage productsPage = loginPage.loginWithValidCredentials(
                com.saucedemo.automation.utils.ConfigReader.getUsername(),
                com.saucedemo.automation.utils.ConfigReader.getPassword()
        );

        Assert.assertTrue(productsPage.isProductsPageDisplayed(),
                "Products page should be displayed after login");

        Assert.assertEquals(productsPage.getPageHeading(), "Products",
                "Page heading should be 'Products'");
    }

    // ---------------------------------------------------------------
    // TC002 — Invalid login — verifies error message appears
    // ---------------------------------------------------------------
    @Test(priority = 2,
          description = "Verify error message on invalid login")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.enterUsername("invalid_user");
        loginPage.enterPassword("wrong_password");
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error message should be displayed for invalid credentials");

        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Epic sadface"),
                "Error should contain 'Epic sadface' but was: " + errorMsg);
    }

    // ---------------------------------------------------------------
    // TC003 — Locked out user test
    // ---------------------------------------------------------------
    @Test(priority = 3,
          description = "Verify locked out user cannot login")
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.enterUsername("locked_out_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.isErrorDisplayed(),
                "Error should be shown for locked out user");

        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "Error message should mention 'locked out'");
    }

    // ---------------------------------------------------------------
    // TC004 — Data-driven login test using Excel
    // DataProvider feeds rows from LoginData sheet one at a time
    // ---------------------------------------------------------------
    @Test(priority = 4,
          description = "Data driven login test from Excel",
          dataProvider = "loginDataProvider")
    public void testLoginWithExcelData(String username,
                                       String password,
                                       String expectedResult) {
        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        if (expectedResult.equalsIgnoreCase("Products")) {
            ProductsPage productsPage = new ProductsPage(getDriver());
            Assert.assertTrue(productsPage.isProductsPageDisplayed(),
                    "Expected Products page for user: " + username);
        } else {
            Assert.assertTrue(loginPage.isErrorDisplayed(),
                    "Expected error for user: " + username);
        }
    }

    // ---------------------------------------------------------------
    // DataProvider — reads from Excel and returns 2D array to @Test
    // ---------------------------------------------------------------
    @DataProvider(name = "loginDataProvider")
    public Object[][] getLoginData() {
        return ExcelUtility.getTestData("LoginData");
    }
}