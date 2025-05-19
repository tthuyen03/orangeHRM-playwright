package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.microsoft.playwright.Page;
import factory.DriverFactory;
import utils.AllureUtils;

public class BaseTest {
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected Page page;

    @BeforeMethod
    public void setUp() {
        logger.info("Setting up test method");
        page = DriverFactory.getPage();
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Cleaning up after test method");
        if (page != null) {
            try {
                page.close();
            } catch (Exception e) {
                logger.error("Error closing page: " + e.getMessage());
                AllureUtils.takeScreenshot(page, "teardown_failure");
            }
        }
    }

    protected void logTestStep(String step) {
        logger.info("Test Step: {}", step);
    }

    protected void logTestData(String data) {
        logger.debug("Test Data: {}", data);
    }

    protected void logError(String error) {
        logger.error("Test Error: {}", error);
    }
} 