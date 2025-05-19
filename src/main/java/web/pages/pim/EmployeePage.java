package web.pages.pim;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import utils.CommonAction;
import utils.ElementUtils;
import web.base.BasePage;

import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class EmployeePage extends BasePage {

    private final Locator inputFirstName = page.getByPlaceholder("First Name");
    private final Locator inputMiddleName = page.getByPlaceholder("Middle Name");
    private final Locator inputLastName = page.getByPlaceholder("Last Name");
    private final Locator inputEmployeeId = page.getByLabel("Employee Id");
    private final Locator inputUsername = page.getByLabel("Username");
    private final Locator inputPassword = page.getByLabel("Password");
    private final Locator inputConfirmPassword = page.getByLabel("Confirm Password");
    private final Locator btnSubmit = page.locator("button:has-text('Save')");
    private final Locator btnCancel = page.locator("button:has-text(' Cancel ')");
    private final Locator uploadImage = page.locator("//div[contains(@class,'file-div')]//button[contains(@class,'icon-button')]");
    private final Locator toggleLogin = page.locator("//span[contains(@class,'switch-input')]");
    private final Locator rdoEnabled = page.locator("//label[text()='Enabled']");
    private final Locator rdoDisabled = page.locator("//label[text()='Disabled']");


    public EmployeePage (Page page){
        super(page);
    }

    public boolean isToggleLoginEnable(){
        toggleLogin.click();
        return toggleLogin.isEnabled();
    }

    public String randomStatus() {
        Random rand = new Random();
        List<String> statusList = List.of("Enabled", "Disabled");
        return statusList.get(rand.nextInt(statusList.size()));
    }

    public void selectStatus(){
        if(randomStatus().equals("Enabled")){
            rdoEnabled.click();
        }
        else{
            rdoDisabled.click();
        }
    }
    public void addEmployee(String firstName, String middleName, String lastName, String empId, String file,
                            String username, String password, String confirmPass){
        inputFirstName.fill(firstName);
        inputMiddleName.fill(middleName);
        inputLastName.fill(lastName);
        inputEmployeeId.fill(empId);
        FileChooser fileChooser = page.waitForFileChooser(() -> uploadImage.click());
        fileChooser.setFiles(Paths.get(file));
        if(isToggleLoginEnable()){
            inputUsername.fill(username);
            selectStatus();
            inputPassword.fill(password);
            inputConfirmPassword.fill(confirmPass);
            clickSave();
        }
        clickSave();
    }

    public void clickSave(){
        btnSubmit.click();
    }

    public boolean isEmployeeCreated(String id) {
        searchEmployeeById(id);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        List<String> userList = CommonAction.getValueOfColumnName(page, "Id");
        return userList.contains(id);
    }

    public void searchEmployeeById(String id){
        inputEmployeeId.fill(id);
        ElementUtils.btnSearch(page).click();
    }

}
