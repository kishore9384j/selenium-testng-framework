package com.saucedemo.automation.utils;

import com.saucedemo.automation.utils.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtility {

    private static final Logger log = LogManager.getLogger(WaitUtility.class);
    private WebDriverWait wait;

    public WaitUtility(WebDriver driver) {
        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    // Wait until element is visible on screen
    public WebElement waitForVisibility(WebElement element) {
        log.info("Waiting for element to be visible");
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // Wait until element is clickable
    public WebElement waitForClickability(WebElement element) {
        log.info("Waiting for element to be clickable");
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // Wait until element is visible by locator
    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Wait for URL to contain a specific text — useful after navigation
    public boolean waitForUrlContains(String urlFragment) {
        log.info("Waiting for URL to contain: " + urlFragment);
        return wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    // Wait for element to disappear — useful for loading spinners
    public boolean waitForInvisibility(WebElement element) {
        log.info("Waiting for element to disappear");
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }
}