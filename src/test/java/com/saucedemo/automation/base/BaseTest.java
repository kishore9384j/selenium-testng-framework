package com.saucedemo.automation.base;

import com.saucedemo.automation.utils.ConfigReader;
import com.saucedemo.automation.utils.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;

public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    // ---------------------------------------------------------------
    // @BeforeMethod — runs before EVERY @Test method
    // @Parameters lets testng.xml pass browser name for cross-browser testing
    // ---------------------------------------------------------------
    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("false") String headless) {

        log.info("===== Starting Test Setup =====");
        log.info("Browser: " + browser + " | Headless: " + headless);

        // Initialize driver — stored in ThreadLocal inside DriverFactory
        DriverFactory.initDriver(browser, Boolean.parseBoolean(headless));

        // Set timeouts
        WebDriver driver = DriverFactory.getDriver();
        driver.manage().timeouts()
              .implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.manage().timeouts()
              .pageLoadTimeout(Duration.ofSeconds(30));

        // Navigate to base URL
        driver.get(ConfigReader.getBaseUrl());
        log.info("Navigated to: " + ConfigReader.getBaseUrl());
    }

    // ---------------------------------------------------------------
    // getDriver — used by test classes and page classes
    // ---------------------------------------------------------------
    public WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    // ---------------------------------------------------------------
    // @AfterMethod — runs after EVERY @Test method, even if test fails
    // ---------------------------------------------------------------
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        log.info("===== Starting Test Teardown =====");
        DriverFactory.quitDriver();
        log.info("===== Test Teardown Complete =====");
    }
}