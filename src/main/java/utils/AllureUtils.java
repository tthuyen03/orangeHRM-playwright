package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Attachment;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AllureUtils {
    public static void takeScreenshot(Page page, String name) {
        attachScreenshot(name, page.screenshot(new Page.ScreenshotOptions().setFullPage(true)));
    }

    @Attachment(value = "{name}", type = "image/png")
    private static byte[] attachScreenshot(String name, byte[] screenshot) {
        return screenshot;
    }

} 