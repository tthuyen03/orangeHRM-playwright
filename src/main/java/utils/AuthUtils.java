package utils;

import browser.BrowserManager;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import config.ConfigLoader;
import io.qameta.allure.Step;
import web.pages.login.LoginPage;

import java.nio.file.Paths;

public class AuthUtils {
    @Step("1. Login and save session")
    public static void generateStorageState(String username, String password) {
        String browserType = ConfigLoader.getProperty("BROWSER");
        BrowserManager.getBrowser(browserType, false);
        Page page = BrowserManager.getPage();
        LoginPage loginPage;
        loginPage = new LoginPage(page);
        loginPage.navigateToPage();
        loginPage.login(username, password);
        page.waitForLoadState();
        BrowserContext context = page.context();
        context.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("storage", "storageState.json")));
        System.out.println("Session saved to storage/storageState.json");
    }
}
