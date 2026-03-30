package com.saucedemo.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    // ---------------------------------------------------------------
    // LOCATORS — @FindBy is the PageFactory way to declare locators
    // Rule: always use ID first, then name, then CSS — avoid XPath
    // ---------------------------------------------------------------
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // ---------------------------------------------------------------
    // CONSTRUCTOR — must call super(driver) to init PageFactory
    // ---------------------------------------------------------------
    public LoginPage(WebDriver driver) {
        super(driver);
        log.info("LoginPage initialized");
    }

    // ---------------------------------------------------------------
    // PAGE ACTIONS — one method per user action
    // ---------------------------------------------------------------
    public void enterUsername(String username) {
        wait.waitForVisibility(usernameField);
        usernameField.clear();
        usernameField.sendKeys(username);
        log.info("Entered username: " + username);
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
        log.info("Entered password");
    }

    public void clickLoginButton() {
        wait.waitForClickability(loginButton);
        loginButton.click();
        log.info("Clicked login button");
    }

    // ---------------------------------------------------------------
    // KEY METHOD — returns ProductsPage after successful login
    // This is the "method chaining" pattern interviewers love
    // ---------------------------------------------------------------
    public ProductsPage loginWithValidCredentials(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        log.info("Login attempted with user: " + username);
        return new ProductsPage(driver);
    }

    // ---------------------------------------------------------------
    // VERIFICATION METHODS — used in assertions in test classes
    // ---------------------------------------------------------------
    public boolean isLoginPageDisplayed() {
        return loginButton.isDisplayed();
    }

    public String getErrorMessage() {
        wait.waitForVisibility(errorMessage);
        return errorMessage.getText();
    }

    public boolean isErrorDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}