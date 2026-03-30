package com.saucedemo.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    private static final Logger log = LogManager.getLogger(ConfigReader.class);

    // Static block — loads config.properties ONCE when class is first used
    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            String configPath = "src/test/resources/config.properties";
            FileInputStream fis = new FileInputStream(configPath);
            properties = new Properties();
            properties.load(fis);
            fis.close();
            log.info("config.properties loaded successfully");
        } catch (IOException e) {
            log.error("Failed to load config.properties: " + e.getMessage());
            throw new RuntimeException("Cannot load config.properties. Check file path.", e);
        }
    }

    // ---------------------------------------------------------------
    // get — returns value for a key, throws clear error if missing
    // ---------------------------------------------------------------
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            log.error("Key not found in config.properties: " + key);
            throw new RuntimeException("Missing key in config.properties: " + key);
        }
        return value.trim();
    }

    // ---------------------------------------------------------------
    // Convenience methods — used by BaseTest directly
    // ---------------------------------------------------------------
    public static String getBrowser()       { return get("browser"); }
    public static String getBaseUrl()       { return get("baseUrl"); }
    public static String getUsername()      { return get("username"); }
    public static String getPassword()      { return get("password"); }
    public static boolean isHeadless()      { return Boolean.parseBoolean(get("headless")); }
    public static int getImplicitWait()     { return Integer.parseInt(get("implicitWait")); }
    public static int getExplicitWait()     { return Integer.parseInt(get("explicitWait")); }
    public static String getScreenshotsPath() { return get("screenshotsPath"); }
    public static String getReportsPath()   { return get("reportsPath"); }
}