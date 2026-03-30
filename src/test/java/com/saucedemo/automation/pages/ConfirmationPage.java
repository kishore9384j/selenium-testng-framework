package com.saucedemo.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ConfirmationPage extends BasePage {

    @FindBy(className = "complete-header")
    private WebElement confirmationHeader;

    @FindBy(className = "complete-text")
    private WebElement confirmationText;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    @FindBy(className = "pony_express")
    private WebElement confirmationImage;

    public ConfirmationPage(WebDriver driver) {
        super(driver);
        log.info("ConfirmationPage initialized");
    }

    // ---------------------------------------------------------------
    // VERIFICATION — these are what your @Test assertions will call
    // ---------------------------------------------------------------
    public boolean isOrderConfirmed() {
        wait.waitForVisibility(confirmationHeader);
        return confirmationHeader.isDisplayed();
    }

    public String getConfirmationHeader() {
        wait.waitForVisibility(confirmationHeader);
        String text = confirmationHeader.getText();
        log.info("Confirmation header: " + text);
        return text;
    }

    public String getConfirmationText() {
        return confirmationText.getText();
    }

    public boolean isConfirmationImageDisplayed() {
        return confirmationImage.isDisplayed();
    }

    // ---------------------------------------------------------------
    // ACTIONS
    // ---------------------------------------------------------------
    public ProductsPage backToProducts() {
        wait.waitForClickability(backToProductsButton);
        backToProductsButton.click();
        log.info("Clicked Back to Products");
        return new ProductsPage(driver);
    }
}
