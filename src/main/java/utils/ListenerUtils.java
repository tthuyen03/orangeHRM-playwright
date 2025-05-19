package utils;


import com.microsoft.playwright.Page;
import factory.DriverFactory;
import org.testng.*;


public class ListenerUtils implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String browserType = result.getTestContext().getCurrentXmlTest().getParameter("browser");
        DriverFactory.getBrowser(browserType);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());
        DriverFactory.closePlaywright();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            Page page = DriverFactory.getPage();
            if (page != null) {
                ScreenShotUtils.takeScreenshot(page);
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
    }

}
