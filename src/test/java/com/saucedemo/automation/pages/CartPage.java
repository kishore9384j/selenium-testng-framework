package com.saucedemo.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class CartPage extends BasePage {

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(className = "title")
    private WebElement pageTitle;

    public CartPage(WebDriver driver) {
        super(driver);
        log.info("CartPage initialized");
    }

    // ---------------------------------------------------------------
    // VERIFICATION
    // ---------------------------------------------------------------
    public boolean isCartPageDisplayed() {
        wait.waitForVisibility(pageTitle);
        return pageTitle.getText().equalsIgnoreCase("Your Cart");
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public boolean isItemInCart(String itemName) {
        return cartItemNames.stream()
                .anyMatch(item -> item.getText().equalsIgnoreCase(itemName));
    }

    // ---------------------------------------------------------------
    // ACTIONS
    // ---------------------------------------------------------------
    public CheckoutPage proceedToCheckout() {
        wait.waitForClickability(checkoutButton);
        checkoutButton.click();
        log.info("Clicked Checkout button");
        return new CheckoutPage(driver);
    }

    public ProductsPage continueShopping() {
        wait.waitForClickability(continueShoppingButton);
        continueShoppingButton.click();
        log.info("Clicked Continue Shopping");
        return new ProductsPage(driver);
    }
}