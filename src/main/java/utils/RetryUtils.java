package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryUtils implements IRetryAnalyzer {

    private int retryCount = 0;
    private final int retryMax = 3;
    @Override
    public boolean retry(ITestResult iTestResult) {
       if(retryCount < retryMax){
           retryCount++;
           return true;
       }
        return false;
    }
}
