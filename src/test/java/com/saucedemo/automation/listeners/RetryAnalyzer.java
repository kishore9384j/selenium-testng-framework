package com.saucedemo.automation.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;
    private static final int MAX_RETRY = 2; // Retry failed test up to 2 times

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            log.warn("Retrying test: " + result.getName()
                   + " | Attempt: " + retryCount + " of " + MAX_RETRY);
            return true;  // true = retry the test
        }
        return false;     // false = give up, mark as failed
    }
}