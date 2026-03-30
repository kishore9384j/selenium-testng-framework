package com.saucedemo.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.saucedemo.automation.reports.ExtentManager;
import com.saucedemo.automation.utils.DriverFactory;
import com.saucedemo.automation.utils.ScreenshotUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);
    private static ExtentReports extent;

    // ---------------------------------------------------------------
    // Suite level — runs once before ALL tests start
    // ---------------------------------------------------------------
    @Override
    public void onStart(ISuite suite) {
        log.info("========== SUITE STARTED: " + suite.getName() + " ==========");
        extent = ExtentManager.createInstance();
    }

    // ---------------------------------------------------------------
    // Suite level — runs once after ALL tests finish
    // ---------------------------------------------------------------
    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flushReports();
        log.info("========== SUITE FINISHED: " + suite.getName() + " ==========");
    }

    // ---------------------------------------------------------------
    // Test level events
    // ---------------------------------------------------------------
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();
        log.info("----- TEST STARTED: " + testName + " -----");

        ExtentTest test = extent.createTest(testName,
                result.getMethod().getDescription());
        test.assignCategory(className);
        ExtentManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("TEST PASSED: " + result.getMethod().getMethodName());
        ExtentManager.getTest().log(Status.PASS,
                "Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.error("TEST FAILED: " + testName);
        log.error("Failure reason: " + result.getThrowable().getMessage());

        // Capture screenshot and embed in report
        try {
            String screenshotPath = ScreenshotUtility.captureScreenshot(
                    DriverFactory.getDriver(), testName);

            ExtentManager.getTest().fail(result.getThrowable(),
                    MediaEntityBuilder.createScreenCaptureFromPath(
                            screenshotPath, testName).build());

        } catch (Exception e) {
            log.error("Could not attach screenshot: " + e.getMessage());
            ExtentManager.getTest().fail(result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("TEST SKIPPED: " + result.getMethod().getMethodName());
        ExtentManager.getTest().log(Status.SKIP,
                "Test Skipped: " + result.getThrowable());
    }
}