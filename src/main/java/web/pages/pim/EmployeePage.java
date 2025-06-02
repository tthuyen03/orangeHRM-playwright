package web.pages.pim;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CommonAction;
import utils.ElementUtils;
import web.base.BasePage;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class EmployeePage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(EmployeePage.class);

    private final Locator btnAdd = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(" Add "));
    private final Locator inputFirstName = page.getByPlaceholder("First Name");
    private final Locator inputMiddleName = page.getByPlaceholder("Middle Name");
    private final Locator inputLastName = page.getByPlaceholder("Last Name");
    private final Locator inputEmployeeId = page.locator("input:below(label:has-text('Employee Id'))").first();
    private final Locator inputUsername = page.locator("input:below(label:has-text('Username'))").first();
    private final Locator inputPassword = page.locator("input:below(label:has-text('Password'))").first();
    private final Locator inputConfirmPassword = page.locator("input:below(label:has-text('Confirm Password'))").first();
    private final Locator btnSubmit = page.locator("button:has-text('Save')");
    private final Locator btnCancel = page.locator("button:has-text(' Cancel ')");
    private final Locator uploadImage = page.locator("input[type='file']");
    private final Locator toggleLogin = page.locator("//span[contains(@class,'switch-input')]");
    private final Locator rdoEnabled = page.locator("//label[text()='Enabled']");
    private final Locator rdoDisabled = page.locator("//label[text()='Disabled']");
    private final Locator txtPersonalDetails = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName("Personal Details"));
    List<Locator> errorMessageList = page.locator("//span[contains(@class,'input-field-error-message')]").all();


    public EmployeePage (Page page){
        super(page);
    }

    public boolean isToggleLoginEnable(){
        return toggleLogin.isEnabled();
    }

    public String randomStatus() {
        Random rand = new Random();
        List<String> statusList = List.of("Enabled", "Disabled");
        return statusList.get(rand.nextInt(statusList.size()));
    }

    public String randomExistingEmployeeId() {
        Random rand = new Random();
        List<String> employeeId = List.of("0295", "13224","0367");
        return employeeId.get(rand.nextInt(employeeId.size()));
    }

    public String randomExistingUsername() {
        Random rand = new Random();
        List<String> usernameList = List.of("Ramaarao", "lakshmana2");
        return usernameList.get(rand.nextInt(usernameList.size()));
    }

    public void selectStatus(){
        if(randomStatus().equals("Enabled")){
            rdoEnabled.click();
        }
        else{
            rdoDisabled.click();
        }
    }

    public void clickToggleLogin(){
        toggleLogin.click();
    }

    public void clickAdd(){
        btnAdd.click();
    }

    public void uploadImage(String file){
        if(file!=null && !file.trim().isEmpty()){
            uploadImage.setInputFiles(Paths.get(file));
        }

    }

    public void addEmployeeWithoutLoginDetail(String firstName, String middleName, String lastName, String empId, String file){
        logger.info("- Fill first name: {}", firstName);
        if (firstName != null) {
            inputFirstName.fill(firstName);
        }

        logger.info("- Fill middle name: {}", middleName);
        if (middleName != null) {
            inputMiddleName.fill(middleName);
        }

        logger.info("- Fill last name: {}", lastName);
        if (lastName != null) {
            inputLastName.fill(lastName);
        }

        logger.info("- Fill employee id: {}", empId);
        if (empId != null && !empId.trim().isEmpty()) {
            inputEmployeeId.click();
            inputEmployeeId.press("Control+a");
            inputEmployeeId.press("Delete");
            inputEmployeeId.fill(empId);
        }

        logger.info("- Upload image: {}", file);
        if (file != null && !file.trim().isEmpty()) {
            uploadImage(file);
        }
    }

    public void addEmployeeWithLoginDetail(String firstName, String middleName, String lastName,
                                           String empId, String file,
                                           String username, String password, String confirmPass) {
        logger.info("- Fill first name: {}", firstName);
        if (firstName != null) {
            inputFirstName.fill(firstName);
        }

        logger.info("- Fill middle name: {}", middleName);
        if (middleName != null) {
            inputMiddleName.fill(middleName);
        }

        logger.info("- Fill last name: {}", lastName);
        if (lastName != null) {
            inputLastName.fill(lastName);
        }

        logger.info("- Fill employee id: {}", empId);
        if (empId != null && !empId.trim().isEmpty()) {
            inputEmployeeId.click();
            inputEmployeeId.press("Control+a");
            inputEmployeeId.press("Delete");
            inputEmployeeId.fill(empId);
        }

        logger.info("- Upload image: {}", file);
        if (file != null && !file.trim().isEmpty()) {
            uploadImage(file);
        }

        if (isToggleLoginEnable()) {
            if (username != null) {
                inputUsername.fill(username);
            }
            selectStatus();
            if (password != null) {
                inputPassword.fill(password);
            }
            if (confirmPass != null) {
                inputConfirmPassword.fill(confirmPass);
            }
        }
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

    public boolean redirectToPersonalDetail(){
        page.waitForTimeout(10000);
        return txtPersonalDetails.isVisible();
    }

    public boolean isErrorMessageDisplayed(){
        for (Locator locator : errorMessageList) {
            if (!locator.isVisible()) {
                return false;
            }
        }
        return true;
    }

    public void searchEmployeeById(String id){
        inputEmployeeId.fill(id);
        ElementUtils.btnSearch(page).click();
    }

}
