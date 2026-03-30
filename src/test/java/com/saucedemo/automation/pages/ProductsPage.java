package com.saucedemo.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class ProductsPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item")
    private List<WebElement> productList;

    // Add first item to cart button
    @FindBy(id = "add-to-cart-sauce-labs-backpack")
    private WebElement addBackpackToCart;

    @FindBy(id = "add-to-cart-sauce-labs-bike-light")
    private WebElement addBikeLightToCart;

    @FindBy(id = "add-to-cart-sauce-labs-bolt-t-shirt")
    private WebElement addBoltTshirtToCart;

    // Cart icon with item count badge
    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;

    // Hamburger menu
    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    public ProductsPage(WebDriver driver) {
        super(driver);
        log.info("ProductsPage initialized");
    }

    // ---------------------------------------------------------------
    // VERIFICATION
    // ---------------------------------------------------------------
    public boolean isProductsPageDisplayed() {
        wait.waitForVisibility(pageTitle);
        return pageTitle.getText().equalsIgnoreCase("Products");
    }

    public String getPageHeading() {
        wait.waitForVisibility(pageTitle);
        return pageTitle.getText();
    }

    public int getProductCount() {
        return productList.size();
    }

    // ---------------------------------------------------------------
    // ACTIONS
    // ---------------------------------------------------------------
    public void addBackpackToCart() {
        wait.waitForClickability(addBackpackToCart);
        addBackpackToCart.click();
        log.info("Added Backpack to cart");
    }

    public void addBikeLightToCart() {
        wait.waitForClickability(addBikeLightToCart);
        addBikeLightToCart.click();
        log.info("Added Bike Light to cart");
    }

    public void addBoltTshirtToCart() {
        wait.waitForClickability(addBoltTshirtToCart);
        addBoltTshirtToCart.click();
        log.info("Added Bolt T-Shirt to cart");
    }

    public String getCartCount() {
        try {
            return cartBadge.getText();
        } catch (Exception e) {
            return "0";
        }
    }

    // Returns CartPage — method chaining
    public CartPage goToCart() {
        wait.waitForClickability(cartIcon);
        cartIcon.click();
        log.info("Navigated to Cart");
        return new CartPage(driver);
    }

    // Logout flow
    public LoginPage logout() {
        wait.waitForClickability(menuButton);
        menuButton.click();
        wait.waitForClickability(logoutLink);
        logoutLink.click();
        log.info("Logged out successfully");
        return new LoginPage(driver);
    }
}