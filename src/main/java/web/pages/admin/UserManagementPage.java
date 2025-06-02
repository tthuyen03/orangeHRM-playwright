package web.pages.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import utils.CommonAction;
import utils.ElementUtils;
import web.base.BasePage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.testng.AssertJUnit.assertTrue;
import static utils.CommonAction.getValueOfColumnName;

/**
 * Page Object class representing the User Management page in OrangeHRM.
 * This class provides methods to interact with user management functionality including:
 * - Adding new users
 * - Searching users
 * - Deleting users
 * - Sorting user data
 * - Managing user roles and status
 */
public class UserManagementPage extends BasePage {
    // ================ Constants ================
    private static final String SORT_ORDER_ASC = "asc";
    private static final String SORT_ORDER_DESC = "desc";
    private static final int WAIT_TIMEOUT = 5000;
    private static final List<String> VALID_ROLES = List.of("Admin", "ESS");
    private static final List<String> VALID_STATUSES = List.of("Enabled", "Disabled");

    // ================ Locators ================
    private final Locator btnAdd = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(" Add "));
    private final Locator inputUsername = page.locator("input:below(label:has-text('Username'))").first();
    private final Locator yesDelete = page.locator("//div[@role='document']").locator("button:has-text(' Yes, Delete ')");
    private final Locator selectStatus = page.locator("div.oxd-select-wrapper:below(label:has-text('Status'))").first();
    private final Locator inputEmployeeName = page.getByPlaceholder("Type for hints...");
    private final Locator selectUserRole = page.locator("div.oxd-select-wrapper:below(label:has-text('User Role'))").first();
    private final Locator inputPassword = page.locator("input[type='password']:below(label:has-text('Password'))").first();
    private final Locator inputConfirmPassword = page.locator("input[type='password']:below(label:has-text('Confirm Password'))").first();
    private final Locator btnSubmit = page.locator("button:has-text('Save')");
    private final Locator passwordStrengthIndicator = page.locator("//div[contains(@class, 'password-strength')]");
    private final Locator passwordStrengthLevel = page.locator("//span[contains(@class,'orangehrm-password-chip')]");
    private final List<Locator> errorMessageList = page.locator("//span[contains(@class,'input-field-error-message')]").all();

    // ================ Constructor ================
    public UserManagementPage(Page page) {
        super(page);
    }


    private void waitAndClick(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.VISIBLE)
            .setTimeout(WAIT_TIMEOUT));
        locator.click();
    }


    private void waitAndFill(Locator locator, String text) {
        locator.waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.VISIBLE)
            .setTimeout(WAIT_TIMEOUT));
        locator.fill(text);
    }


    private void validateValue(String value, List<String> validValues, String fieldName) {
        if (!validValues.contains(value)) {
            throw new IllegalArgumentException(
                String.format("Invalid %s: %s. Valid values are: %s", 
                    fieldName, value, String.join(", ", validValues)));
        }
    }


    @Step("Click Add button")
    public void clickAdd() {
        btnAdd.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        btnAdd.click();
    }


    public void getEmployeeName(String empName) {
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(empName))
            .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(empName)).click();
    }


    public String randomValidEmployeeName() {
        Random rand = new Random();
        List<String> employeeNames = List.of(
               "John  Doe", "Jane  Doe");
        return employeeNames.get(rand.nextInt(employeeNames.size()));
    }

    public String randomInvalidEmployeeName() {
        Random rand = new Random();
        List<String> employeeNames = List.of(
                "Joe", "Mark", "Black");
        return employeeNames.get(rand.nextInt(employeeNames.size()));
    }

    public String randomExistingUsername() {
        Random rand = new Random();
        List<String> existingUsername = List.of(
                "FMLName", "johndoe", "johndoe1");
        return existingUsername.get(rand.nextInt(existingUsername.size()));
    }

    public String randomRole(){
        Random rand = new Random();
        List<String> roles = List.of("Admin", "ESS");
        return roles.get(rand.nextInt(roles.size()));
    }

    public String randomStatus(){
        Random rand = new Random();
        List<String> status = List.of("Enabled", "Disabled");
        return status.get(rand.nextInt(status.size()));
    }

    @Step("Verify user is created")
    public boolean isUserCreated(String username) {
        searchUserByUsername(username);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        List<String> userList = CommonAction.getValueOfColumnName(page, "Username");
        return userList.contains(username);
    }


    public void selectUserRole(String role) {
        selectUserRole.click();
        getValueInList(role);
    }


    public void selectStatus(String status) {
        selectStatus.click();
        getValueInList(status);
    }

    public void enterEmployeeName(String employeeName) {
        inputEmployeeName.fill(employeeName);
        getEmployeeName(employeeName);
    }


    public void enterUsername(String username) {
        waitAndFill(inputUsername, username);
    }


    public void enterPassword(String password) {
        waitAndFill(inputPassword, password);
        page.waitForTimeout(500);
    }


    public void enterConfirmPassword(String confirmPassword) {
        waitAndFill(inputConfirmPassword, confirmPassword);
    }

    @Step("Add user")
    public void addUser(String employeeName, String username, String role, String status, String password, String confirmPass) {
        selectUserRole(role);
        selectStatus(status);
        enterEmployeeName(employeeName);
        enterUsername(username);
        enterPassword(password);
        enterConfirmPassword(confirmPass);
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @Step("Click Save button")
    public void clickSave(){
        btnSubmit.click();
    }

    public boolean isErrorMessageDisplayed(){
        for (Locator locator : errorMessageList) {
            if (!locator.isVisible()) {
                return false;
            }
        }
        return true;
    }

    public void deleteUserByUsername(String username) {
        Locator btnDelete = ElementUtils.tableRow(page)
            .locator("//div[text()='" + username + "']//ancestor::div[@role='row']//i[contains(@class,'bi-trash')]");
        btnDelete.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        btnDelete.click();
        yesDelete.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        yesDelete.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public boolean verifyDeleteUserSuccess(String colName, String username){
        List<String> userList = getValueOfColumnName(page, colName);
        return !userList.contains(username);
    }


    public void searchUserByUsername(String username) {
        waitAndFill(inputUsername, username);
        ElementUtils.btnSearch(page).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public void searchByUserRole(String role){
        selectUserRole.click();
        getValueInList(role);
        ElementUtils.btnSearch(page).click();
    }

    public void searchByStatus(String status){
        selectUserRole.click();
        getValueInList(status);
        ElementUtils.btnSearch(page).click();
    }

    public void getValueInList(String text){
        Locator locator = ElementUtils.droplist(page).locator("//span[text()='" + text + "']");
        locator.waitFor();
        locator.click();
    }

    public void pressDeleteButtonOnTable(String username) {
        Locator btnDelete = ElementUtils.tableRow(page).locator("//div[text()='"+username+"']//ancestor::div[@role='row']//i[contains(@class,'bi-trash')]");
        btnDelete.click();
        yesDelete.click();
    }


    public Boolean sortByColName(String colName, String sortOrder)  {
        ElementUtils.table(page).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ArrayList<String> beforeSort = CommonAction.getValueOfColumnName(page,colName);
        System.out.println("-----Before sort-----");
        for (String value : beforeSort) {
            System.out.println(value);
        }

        ArrayList<String> expectedSort = new ArrayList<>(beforeSort);
        Locator headerSort = ElementUtils.tableRow(page).locator("//div[text()='" + colName + "']//div[@class='oxd-table-header-sort']");
        headerSort.click();
        if (sortOrder.equals("asc")) {
            Collections.sort(beforeSort, String.CASE_INSENSITIVE_ORDER);
            System.out.println("-----Expected sort-----");
            for (String value : expectedSort) {
                System.out.println(value);
            }
            Locator asc = page.locator("//div[@role='columnheader' and text()='" + colName + "']//span[text()='Ascending']");
            asc.click();

        }
        if(sortOrder.equals("desc")){
            expectedSort.sort(String.CASE_INSENSITIVE_ORDER.reversed());

            System.out.println("-----Expected sort-----");
            for (String value : expectedSort) {
                System.out.println(value);
            }
            Locator desc = page.locator("//div[@role='columnheader' and text()='" + colName + "']//span[text()='Descending']");
            desc.click();

        }

        ElementUtils.tableRow(page).first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        ArrayList<String> afterSort = CommonAction.getValueOfColumnName(page,colName);
        System.out.println("-----After sort-----");
        for (String value : afterSort) {
            System.out.println(value);
        }
        return afterSort.equals(expectedSort);
    }

    public String getPasswordStrengthLevel() {
        passwordStrengthLevel.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(WAIT_TIMEOUT));
        return passwordStrengthLevel.textContent().trim();
    }


    public boolean isUserListVisible() {
        return page.locator("//div[@class='oxd-table']").isVisible();
    }

    public void clickCancel() {
        page.locator("button:has-text('Cancel')").click();
    }



    public void clickEditForUsername(String username) {
        Locator editButton = ElementUtils.tableRow(page)
            .locator("//div[text()='" + username + "']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        waitAndClick(editButton);
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }






}
