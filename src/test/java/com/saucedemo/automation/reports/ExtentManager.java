package com.saucedemo.automation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.saucedemo.automation.utils.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {

    private static final Logger log = LogManager.getLogger(ExtentManager.class);
    private static ExtentReports extent;

    // ThreadLocal so each parallel thread has its own ExtentTest node
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    // ---------------------------------------------------------------
    // createInstance — call once in @BeforeSuite (via TestListener)
    // ---------------------------------------------------------------
    public static ExtentReports createInstance() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportPath = ConfigReader.getReportsPath()
                          + "TestReport_" + timestamp + ".html";

        // Create report directory if missing
        new java.io.File(ConfigReader.getReportsPath()).mkdirs();

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("SauceDemo E2E Test Results");
        sparkReporter.config().setEncoding("utf-8");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Project",     "SauceDemo Automation");
        extent.setSystemInfo("Tester",      System.getProperty("user.name"));
        extent.setSystemInfo("OS",          System.getProperty("os.name"));
        extent.setSystemInfo("Java Version",System.getProperty("java.version"));
        extent.setSystemInfo("Browser",     ConfigReader.getBrowser());
        extent.setSystemInfo("Environment", ConfigReader.get("environment"));

        log.info("Extent Report created at: " + reportPath);
        return extent;
    }

    public static ExtentReports getExtent()          { return extent; }
    public static ExtentTest   getTest()             { return extentTest.get(); }
    public static void         setTest(ExtentTest t) { extentTest.set(t); }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            log.info("Extent Report flushed and saved.");
        }
    }
}