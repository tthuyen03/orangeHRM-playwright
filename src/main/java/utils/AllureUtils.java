package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Attachment;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AllureUtils {
    
    @Attachment(value = "{name}", type = "image/png")
    public static byte[] takeScreenshot(Page page, String name) {
        try {
            Path screenshotPath = Paths.get("screenshots", name + ".png");
            Files.createDirectories(screenshotPath.getParent());
            
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions()
                    .setPath(screenshotPath)
                    .setFullPage(true));
            
            return screenshot;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Attachment(value = "{name}", type = "text/plain")
    public static ByteArrayInputStream saveTextLog(String name, String content) {
        return new ByteArrayInputStream(content.getBytes());
    }
} 