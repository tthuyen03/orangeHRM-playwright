package web.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import config.ConfigLoader;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BasePage {
    public Page page;


    public BasePage(Page page){
        this.page = page;
    }

    public void navigateToPage() {
        page.navigate(ConfigLoader.getProperty("URL"),
                new Page.NavigateOptions()
                        .setTimeout(60000)
                        .setWaitUntil(WaitUntilState.DOMCONTENTLOADED));
    }






}
