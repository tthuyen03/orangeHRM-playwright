package utils;


import com.microsoft.playwright.Page;
import config.ConfigLoader;
import factory.DriverFactory;
import org.testng.*;


public class ListenerUtils implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String browserType = ConfigLoader.getProperty("BROWSER");
        DriverFactory.getBrowser(browserType);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            Page page = DriverFactory.getPage();
            if (page != null) {
                ScreenShotUtils.takeScreenshot(page);
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
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite Finished: " + context.getName());
        DriverFactory.closePlaywright(); // Cleanup sau toàn bộ suite
    }
}
