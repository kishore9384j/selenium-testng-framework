package com.saucedemo.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {

    // Step 1 — Enter customer info
    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement postalCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // Step 2 — Order summary
    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(className = "title")
    private WebElement pageTitle;

    public CheckoutPage(WebDriver driver) {
        super(driver);
        log.info("CheckoutPage initialized");
    }

    // ---------------------------------------------------------------
    // VERIFICATION
    // ---------------------------------------------------------------
    public boolean isCheckoutStepOneDisplayed() {
        wait.waitForVisibility(firstNameField);
        return firstNameField.isDisplayed();
    }

    public String getErrorMessage() {
        wait.waitForVisibility(errorMessage);
        return errorMessage.getText();
    }

    public String getOrderTotal() {
        wait.waitForVisibility(totalLabel);
        return totalLabel.getText();
    }

    // ---------------------------------------------------------------
    // ACTIONS — Step 1
    // ---------------------------------------------------------------
    public void enterFirstName(String firstName) {
        wait.waitForVisibility(firstNameField);
        firstNameField.clear();
        firstNameField.sendKeys(firstName);
        log.info("Entered first name: " + firstName);
    }

    public void enterLastName(String lastName) {
        lastNameField.clear();
        lastNameField.sendKeys(lastName);
        log.info("Entered last name: " + lastName);
    }

    public void enterPostalCode(String postalCode) {
        postalCodeField.clear();
        postalCodeField.sendKeys(postalCode);
        log.info("Entered postal code: " + postalCode);
    }

    public ConfirmationPage fillShippingAndContinue(
            String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        wait.waitForClickability(continueButton);
        continueButton.click();
        log.info("Checkout Step 1 completed");
        // Click Finish on Step 2
        wait.waitForClickability(finishButton);
        finishButton.click();
        log.info("Clicked Finish — order placed");
        return new ConfirmationPage(driver);
    }
}