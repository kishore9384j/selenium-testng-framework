package com.saucedemo.automation.pages;

import com.saucedemo.automation.utils.WaitUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class BasePage {

    protected WebDriver driver;
    protected WaitUtility wait;
    protected static final Logger log = LogManager.getLogger(BasePage.class);

    // Every page class calls super(driver) — this runs automatically
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WaitUtility(driver);
        // PageFactory initializes all @FindBy elements in the child class
        PageFactory.initElements(driver, this);
    }

    // Common utility methods available to ALL page classes
    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}