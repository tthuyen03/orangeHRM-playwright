package driver;

import com.microsoft.playwright.*;
import configuration.Config;
import web.constants.Constants;

import java.nio.file.Paths;
import java.util.Map;

public class BrowserManager {
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;
    private static APIRequestContext apiRequestContext;



    //set up playwright, open browser and page
    public static void setUpBrowser() {
        playwright = Playwright.create();
        String browserName = Config.get("BROWSER").toLowerCase();
        boolean headless = Config.isHeadless();

        if (Config.isRemote()) {
            String gridUrl = Config.getGridUrl(); // Sử dụng phương thức mới từ Config class
            System.out.println("Connecting to Selenium Grid at: " + gridUrl);
            browser = connectToGrid(playwright, browserName, gridUrl);
        } else {
            browser = launchLocal(browserName, headless);
        }

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(null) // Cho phép responsive
        );
        page = context.newPage();
        page.onConsoleMessage(msg -> System.out.println("Console message: " + msg.text()));

        // Khởi tạo APIRequestContext nếu cần
        apiRequestContext = playwright.request().newContext();
    }

    private static Browser connectToGrid(Playwright playwright, String browserName, String gridUrl) {
        // Chuyển đổi URL từ http sang websocket và thêm path /session
        String cdpUrl = gridUrl.replace("http://", "ws://") + "/devtools/chromium";

        try {
            return switch (browserName.toLowerCase()) {
                case "chrome", "chromium" -> {
                    System.out.println("Connecting to Chrome via CDP: " + cdpUrl);
                    yield playwright.chromium().connectOverCDP(cdpUrl);
                }
                case "firefox" -> {
                    System.out.println("Connecting to Firefox via CDP: " + cdpUrl);
                    yield playwright.firefox().connectOverCDP(cdpUrl);
                }
                default -> throw new RuntimeException("Unsupported browser for Grid connection: " + browserName);
            };
        } catch (PlaywrightException e) {
            System.err.println("Failed to connect to Grid: " + e.getMessage());
            throw new RuntimeException("Grid connection failed", e);
        }
    }

    private static Browser launchLocal(String browserName, Boolean headless){
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);
        return switch (browserName.toLowerCase()){
            case "chrome", "chromium" -> playwright.chromium().launch(options);
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> throw new RuntimeException("Unsupported browser: " + browserName);
        };
    }
    public static Browser getBrowser() {
        return browser;
    }


    public static Page getPage(){
        return page;
    }

    public static BrowserContext getContext() {
        return context;
    }


    //set up with session
    public static void setUpWithSession(String sessionFile) {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(new Browser.NewContextOptions()
                .setStorageStatePath(Paths.get(sessionFile)));
        page = context.newPage();
    }


    //close playwright
    public static void closePlaywright(){
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }


    //set up api
    public static void initAPI(Map<String, String> headers){
        playwright = Playwright.create();
        APIRequest.NewContextOptions options = new APIRequest.NewContextOptions()
                .setBaseURL(Constants.baseApiURL);
        if (headers != null && !headers.isEmpty()) {
            options.setExtraHTTPHeaders(headers);
        }
        apiRequestContext = playwright.request().newContext(options);
    }

    public static APIRequestContext getApiRequestContext() {
        return apiRequestContext;
    }
}
