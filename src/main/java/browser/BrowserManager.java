package browser;

import com.microsoft.playwright.*;
import config.ConfigLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class BrowserManager {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;
    private static APIRequestContext apiRequestContext;

    public static void getBrowser(String browserName, boolean useStorageState) {
        playwright = Playwright.create();
        boolean isHeadless = ConfigLoader.getBooleanProperty("HEADLESS");
        String target = ConfigLoader.getProperty("TARGET");

        if ("remote".equals(target)) {
            browser = connectToRemoteBrowser(browserName, isHeadless);
        } else {
            browser = launchLocalBrowser(browserName, isHeadless);
        }
        Browser.NewContextOptions options = new Browser.NewContextOptions();
        if(useStorageState) {
            Path storagePath = Paths.get("storage","StorageState.json");
            options.setStorageStatePath(storagePath);
        }
        context = browser.newContext(options);
        page = context.newPage();
    }

    private static Browser launchLocalBrowser(String browserName, boolean isHeadless) {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(isHeadless);

        return switch (browserName.toLowerCase()) {
            case "chrome" -> playwright.chromium().launch(options);
            case "firefox" -> playwright.firefox().launch(options);
            case "edge" -> playwright.chromium().launch(options.setChannel("msedge"));
            default -> playwright.chromium().launch(options);
        };
    }

    private static Browser connectToRemoteBrowser(String browserName, boolean isHeadless) {
        String remoteUrl = ConfigLoader.getProperty("REMOTE_URL");
        String remotePort = ConfigLoader.getProperty("REMOTE_PORT");
        String wsEndpoint = String.format("ws://%s:%s", remoteUrl, remotePort);

        return switch (browserName.toLowerCase()) {
            case "chrome" -> playwright.chromium().connect(wsEndpoint);
            case "firefox" -> playwright.firefox().connect(wsEndpoint);
            case "edge" -> playwright.chromium().connect(wsEndpoint);
            default -> playwright.chromium().connect(wsEndpoint);
        };
    }


    public static Page getPage() {
        return page;
    }

    public static BrowserContext getContext() {
        return context;
    }


    public static void closePlaywright() {
        if (playwright != null) {
            playwright.close();
        }
    }

    public static void initAPI(Map<String, String> headers) {
        playwright = Playwright.create();
        APIRequest.NewContextOptions options = new APIRequest.NewContextOptions()
                .setBaseURL(ConfigLoader.getProperty("BASE_URL"));
        if (headers != null && !headers.isEmpty()) {
            options.setExtraHTTPHeaders(headers);
        }
        apiRequestContext = playwright.request().newContext(options);
    }

    public static APIRequestContext getApiRequestContext() {
        return apiRequestContext;
    }
}
