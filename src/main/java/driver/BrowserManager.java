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
    public static Browser getBrowser(){
        playwright = Playwright.create();
        String broswerName = Config.get("BROWSER").toLowerCase();
        boolean headless = Config.isHeadless();
        if(Config.isRemote()){
            String url = Config.get("BROWSER").toLowerCase();
            return connectToRemote(broswerName,url);
        }
        else{
            return launchLocal(broswerName,headless);
        }
    }

    private static Browser connectToRemote(String browserName, String url){
        switch (browserName){
            case "chrome":
            case "chromium":
                return playwright.chromium().connect(url);
            case "firefox":
                return playwright.firefox().connect(url);
            case "webkit":
                return playwright.webkit().connect(url);
            default:
                throw new RuntimeException("Unsupported browser: " + browserName);
        }
    }

    private static Browser launchLocal(String browserName, Boolean headless){
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless);
        switch (browserName){
            case "chrome":
            case "chromium":
                return playwright.chromium().launch(options);
            case "firefox":
                return playwright.firefox().launch(options);
            case "webkit":
                return playwright.webkit().launch(options);
            default:
                throw new RuntimeException("Unsupported browser: " + browserName);
        }
    }


    public static void getBrowser(String browserName){
        playwright = Playwright.create();
        switch(browserName.toLowerCase()){
            case "chrome":
                browser = openChrome();
                break;
            case "firefox":
                browser =openFirefox();
                break;
            case "edge":
                browser = openEdge();
                break;
            default:
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
                break;
        }

        context = browser.newContext();
        page = context.newPage();

    }

    private static Browser openChrome(){
        return browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    private static Browser openFirefox(){
        return browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    private static Browser openEdge(){
        return playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge")
                        .setHeadless(false));
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
