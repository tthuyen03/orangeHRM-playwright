package utils;


import com.microsoft.playwright.Page;
import config.ConfigLoader;
import browser.BrowserManager;
import org.testng.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;


public class ListenerUtils implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String browserType = ConfigLoader.getProperty("BROWSER");
        boolean isLogin = Arrays.asList(result.getMethod().getGroups()).contains("login");
        BrowserManager.getBrowser(browserType, !isLogin);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());

    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            Page page = BrowserManager.getPage();
            if (page != null) {
                AllureUtils.takeScreenshot(page, result.getName());
            } else {
                System.out.println("Page is null, cannot take screenshot");
            }
        } catch (Exception e) {
            System.out.println("Screenshot failed in listener: " + e.getMessage());
        }
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test Suite Started: " + context.getName());
        File storageFile = Paths.get("storage", "storageState.json").toFile();
        if (!storageFile.exists()) {
            System.out.println(" Session not found. Creating new login session...");
            AuthUtils.generateStorageState("Admin", "admin123");
        }
        else{
            System.out.println("Reusing existing session from storageState.json");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite Finished: " + context.getName());
        BrowserManager.closePlaywright();
    }
}
