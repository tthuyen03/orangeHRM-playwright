package web.pages.login;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.Cookie;
import browser.BrowserManager;
import io.qameta.allure.Step;
import web.base.BasePage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.testng.Assert.assertTrue;


public class LoginPage extends BasePage {
    public LoginPage(Page page){
        super(page);
    }

    private Locator inputUsername = page.locator("input[name='username']");
    private Locator inputPassword = page.locator("input[name='password']");
    private Locator buttonLogin = page.getByRole(AriaRole.BUTTON);
    private Locator textMessage = page.getByText("Invalid credentials");
    private Locator linkForgotPassword = page.getByText("Forgot your password? ");
    private Locator textResetPassword = page.getByText("Reset Password");
    private Locator textLogin = page.locator("h5:has-text('Login')");
    private Locator logout = page.locator("//ul[@role='menu']//a[text()='Logout']");
    List<Locator> textRequiredList = page.locator("//span[text()='Required']").all();



    @Step("Login with username: {0}, password: {1}")
    public void login(String username, String password){
        inputUsername.fill(username);
        inputPassword.fill(password);
        buttonLogin.click();
    }


    public boolean isInvalidCredentialsVisible(){
        return textMessage.isVisible();
    }

    public boolean isRequiredVisible() {
        for (Locator locator : textRequiredList) {
            if (!locator.isVisible()) {
                return false;
            }
        }
        return true;
    }

    public boolean isTextLoginVisible(){
        return textLogin.isVisible();
    }


    public boolean checkLinkForgotPassword(){
        linkForgotPassword.click();
        return textResetPassword.isVisible();
    }

    @Step("Press Tab button")
    public boolean checkTab(){
        inputUsername.click();
        inputPassword.press("Tab");
        Locator activeInputPassword = page.locator(":focus");
        if(!activeInputPassword.equals(inputPassword)){
            return false;
        }
        page.keyboard().press("Tab");
        Locator activeButtonLogin = page.locator(":focus");
        if(!activeButtonLogin.equals(buttonLogin)){
            return false;
        }
        return true;
    }


    public void checkSession(){
        Path sessionPath = Paths.get("auth.json");
        BrowserManager.getContext().storageState(
                new BrowserContext.StorageStateOptions().setPath(sessionPath)
        );
        assertTrue(Files.exists(sessionPath), "Expected session file (auth.json) to be created after login");
    }


    public Cookie getCookieByName(BrowserContext context){
        return context.cookies().stream()
                .filter(c -> c.name.equals("orangehrm"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Session cookie not found"));
    }

    public void clickLogout(){
        logout.click();
    }

}
