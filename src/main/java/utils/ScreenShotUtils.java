package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShotUtils {
    public static void takeScreenshot(Page page) {
        try {
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            String time = new SimpleDateFormat("ddMMyyyy").format(new Date());
            String file = methodName + "_" + time + ".png";
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshot/" + file))
                    .setFullPage(true));
            byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment(file, "img/png", new ByteArrayInputStream(screenshotBytes),".png");
            System.out.println("Screenshot saved & attached to Allure:: " + file);
        } catch (Exception e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
        }
    }

}
