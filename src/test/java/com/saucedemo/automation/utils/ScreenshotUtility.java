package com.saucedemo.automation.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtility {

    private static final Logger log = LogManager.getLogger(ScreenshotUtility.class);

    // Returns file path of saved screenshot — used by ExtentManager to embed it
    public static String captureScreenshot(WebDriver driver, String testName) {

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotName = testName + "_" + timestamp + ".png";
        String destPath = ConfigReader.getScreenshotsPath() + screenshotName;

        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(destPath);
            // Create screenshots directory if it doesn't exist
            destFile.getParentFile().mkdirs();
            FileUtils.copyFile(srcFile, destFile);
            log.info("Screenshot saved: " + destPath);
        } catch (IOException e) {
            log.error("Failed to capture screenshot: " + e.getMessage());
        }

        return destPath;
    }
}