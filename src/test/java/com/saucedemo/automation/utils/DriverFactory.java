package com.saucedemo.automation.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DriverFactory {

    // ThreadLocal ensures each parallel test thread gets its OWN driver
    // Without this, parallel tests would share one browser and crash each other
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    // ---------------------------------------------------------------
    // initDriver — call this in @BeforeMethod in BaseTest
    // ---------------------------------------------------------------
    public static void initDriver(String browser, boolean headless) {

        WebDriver driver = null;

        switch (browser.toLowerCase().trim()) {

        case "chrome":
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();

            // Window + notifications
            chromeOptions.addArguments("--start-maximized");
            chromeOptions.addArguments("--disable-notifications");
            chromeOptions.addArguments("--disable-popup-blocking");

            // CRITICAL — disables the "Change your password" popup
            chromeOptions.addArguments("--disable-save-password-bubble");
            chromeOptions.addArguments("--password-store=basic");
            chromeOptions.addArguments("--no-default-browser-check");
            chromeOptions.addArguments("--no-first-run");
            chromeOptions.addArguments("--disable-features=PasswordCheck");

            // Suppress CDP version warning in console
            chromeOptions.addArguments("--remote-allow-origins=*");

            // Disable password manager via Chrome preferences
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("profile.password_manager_leak_detection", false);
            prefs.put("profile.default_content_setting_values.notifications", 2);
            chromeOptions.setExperimentalOption("prefs", prefs);

            // Suppress "Chrome is being controlled" infobar
            chromeOptions.setExperimentalOption("excludeSwitches",
                    Arrays.asList("enable-automation", "enable-logging"));
            chromeOptions.setExperimentalOption("useAutomationExtension", false);

            if (headless) {
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--window-size=1920,1080");
                log.info("Running Chrome in headless mode");
            }

            driver = new ChromeDriver(chromeOptions);
            log.info("Chrome browser launched successfully");
            break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                    log.info("Running Firefox in headless mode");
                }
                driver = new FirefoxDriver(firefoxOptions);
                log.info("Firefox browser launched successfully");
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                    log.info("Running Edge in headless mode");
                }
                driver = new EdgeDriver(edgeOptions);
                log.info("Edge browser launched successfully");
                break;

            default:
                log.error("Browser not supported: " + browser + ". Defaulting to Chrome.");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
        }

        // Store driver in ThreadLocal for this test thread
        tlDriver.set(driver);
    }

    // ---------------------------------------------------------------
    // getDriver — call this anywhere you need the WebDriver instance
    // ---------------------------------------------------------------
    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    // ---------------------------------------------------------------
    // quitDriver — call this in @AfterMethod in BaseTest
    // ---------------------------------------------------------------
    public static void quitDriver() {
        if (tlDriver.get() != null) {
            tlDriver.get().quit();
            tlDriver.remove(); // Prevent memory leaks in parallel runs
            log.info("Browser closed and driver removed from ThreadLocal");
        }
    }
}