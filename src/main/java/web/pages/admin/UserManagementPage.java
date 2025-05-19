package web.pages.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
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
    private final Locator passwordStrengthLevel = page.locator("//div[contains(@class, 'password-strength')]//span");
    private final List<Locator> errorMessageList = page.locator("//span[contains(@class,'input-field-error-message')]").all();

    // ================ Constructor ================
    public UserManagementPage(Page page) {
        super(page);
    }

    // ================ Utility Methods ================
    /**
     * Waits for an element to be visible and clicks it
     * @param locator The locator to wait for and click
     */
    private void waitAndClick(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.VISIBLE)
            .setTimeout(WAIT_TIMEOUT));
        locator.click();
    }

    /**
     * Waits for an element to be visible and fills it with text
     * @param locator The locator to wait for and fill
     * @param text The text to fill
     */
    private void waitAndFill(Locator locator, String text) {
        locator.waitFor(new Locator.WaitForOptions()
            .setState(WaitForSelectorState.VISIBLE)
            .setTimeout(WAIT_TIMEOUT));
        locator.fill(text);
    }

    /**
     * Validates that a given value is in the list of valid values
     * @param value The value to validate
     * @param validValues The list of valid values
     * @param fieldName The name of the field being validated (for error message)
     * @throws IllegalArgumentException if the value is not valid
     */
    private void validateValue(String value, List<String> validValues, String fieldName) {
        if (!validValues.contains(value)) {
            throw new IllegalArgumentException(
                String.format("Invalid %s: %s. Valid values are: %s", 
                    fieldName, value, String.join(", ", validValues)));
        }
    }

    // ================ User Management Actions ================
    /**
     * Clicks the Add button to create a new user
     */
    public void clickAdd() {
        btnAdd.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        btnAdd.click();
    }

    /**
     * Selects an employee name from the dropdown
     * @param empName The name of the employee to select
     */
    public void getEmployeeName(String empName) {
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(empName))
            .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(empName)).click();
    }

    /**
     * Generates a random valid employee name for testing
     * @return A randomly selected valid employee name
     */
    public String randomValidEmployeeName() {
        Random rand = new Random();
        List<String> employeeNames = List.of(
                "Qwerty Qwerty LName", "Sara Tencrady", "Jobin Mathew Sam");
        return employeeNames.get(rand.nextInt(employeeNames.size()));
    }

    public String randomInvalidEmployeeName() {
        Random rand = new Random();
        List<String> employeeNames = List.of(
                "Joe", "Mark", "Black");
        return employeeNames.get(rand.nextInt(employeeNames.size()));
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

    /**
     * Checks if a user exists in the system
     * @param username The username to check
     * @return true if the user exists, false otherwise
     */
    public boolean isUserCreated(String username) {
        searchUserByUsername(username);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        List<String> userList = CommonAction.getValueOfColumnName(page, "Username");
        return userList.contains(username);
    }

    /**
     * Selects a user role from the dropdown
     * @param role The role to select
     * @throws IllegalArgumentException if the role is invalid
     */
    public void selectUserRole(String role) {
        validateValue(role, VALID_ROLES, "role");
        selectUserRole.click();
        getValueInList(role);
    }

    /**
     * Selects a status from the dropdown
     * @param status The status to select
     * @throws IllegalArgumentException if the status is invalid
     */
    public void selectStatus(String status) {
        validateValue(status, VALID_STATUSES, "status");
        selectStatus.click();
        getValueInList(status);
    }

    public void enterEmployeeName(String employeeName) {
        inputEmployeeName.fill(employeeName);
        getEmployeeName(employeeName);
    }

    /**
     * Enters a username in the username field
     * @param username The username to enter
     */
    public void enterUsername(String username) {
        waitAndFill(inputUsername, username);
    }

    /**
     * Enters a password in the password field
     * @param password The password to enter
     */
    public void enterPassword(String password) {
        waitAndFill(inputPassword, password);
        // Wait for the strength indicator to update
        page.waitForTimeout(500);
    }

    /**
     * Enters a confirmation password
     * @param confirmPassword The confirmation password to enter
     */
    public void enterConfirmPassword(String confirmPassword) {
        waitAndFill(inputConfirmPassword, confirmPassword);
    }

    /**
     * Adds a new user to the system
     * @param employeeName The name of the employee
     * @param username The username for the new user
     * @param role The role to assign to the user
     * @param status The status of the user (Enabled/Disabled)
     * @param password The password for the new user
     * @param confirmPass The confirmation password
     * @throws RuntimeException if any required field is empty
     */
    public void addUser(String employeeName, String username, String role, String status, String password, String confirmPass) {
        if (employeeName == null || username == null || role == null || status == null || 
            password == null || confirmPass == null) {
            throw new RuntimeException("All fields are required to create a user");
        }
        
        selectUserRole(role);
        selectStatus(status);
        enterEmployeeName(employeeName);
        enterUsername(username);
        enterPassword(password);
        enterConfirmPassword(confirmPass);
        clickSave();
        
        // Wait for the operation to complete
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

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

    /**
     * Deletes a user by their username
     * @param username The username of the user to delete
     */
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

    /**
     * Searches for a user by username
     * @param username The username to search for
     */
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

    /**
     * Sorts the user table by a specific column
     * @param colName The name of the column to sort by
     * @param sortOrder The sort order ("asc" or "desc")
     * @return true if the sort was successful, false otherwise
     */
    public Boolean sortByColName(String colName, String sortOrder) {
        if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
            throw new IllegalArgumentException("Sort order must be either 'asc' or 'desc'");
        }

        ElementUtils.table(page).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ArrayList<String> beforeSort = CommonAction.getValueOfColumnName(page, colName);
        
        ArrayList<String> expectedSort = new ArrayList<>(beforeSort);
        Locator headerSort = ElementUtils.tableRow(page)
            .locator("//div[text()='" + colName + "']//div[@class='oxd-table-header-sort']");
        headerSort.click();

        if (sortOrder.equals("asc")) {
            Collections.sort(expectedSort, String.CASE_INSENSITIVE_ORDER);
            Locator asc = page.locator("//div[@role='columnheader' and text()='" + colName + "']//span[text()='Ascending']");
            asc.click();
        } else {
            expectedSort.sort(String.CASE_INSENSITIVE_ORDER.reversed());
            Locator desc = page.locator("//div[@role='columnheader' and text()='" + colName + "']//span[text()='Descending']");
            desc.click();
        }

        ElementUtils.tableRow(page).first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ArrayList<String> afterSort = CommonAction.getValueOfColumnName(page, colName);
        
        return afterSort.equals(expectedSort);
    }

    public String getErrorMessage() {
        return page.locator("//span[contains(@class, 'oxd-input-field-error-message')]").textContent();
    }

    /**
     * Gets the current password strength level
     * @return The password strength level text
     */
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

    /**
     * Checks if a username exists in the system (case-insensitive)
     * @param username The username to check
     * @return true if the username exists (case-insensitive), false otherwise
     */
    public boolean isUsernameExistsCaseInsensitive(String username) {
        searchUserByUsername(username);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        List<String> userList = CommonAction.getValueOfColumnName(page, "Username");
        return userList.stream()
                .anyMatch(existingUsername -> existingUsername.equalsIgnoreCase(username));
    }

    /**
     * Gets the error message for duplicate username
     * @return The error message text
     */
    public String getDuplicateUsernameErrorMessage() {
        return page.locator("//span[contains(@class, 'oxd-input-field-error-message')]")
                .filter(new Locator.FilterOptions().setHasText("Already exists"))
                .textContent();
    }

    /**
     * Clicks the edit button for a specific username
     * @param username The username of the user to edit
     */
    public void clickEditForUsername(String username) {
        Locator editButton = ElementUtils.tableRow(page)
            .locator("//div[text()='" + username + "']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        waitAndClick(editButton);
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    /**
     * Gets the current value of the username field
     * @return The username value
     */
    public String getUsernameValue() {
        return inputUsername.inputValue();
    }

    /**
     * Gets the current value of the employee name field
     * @return The employee name value
     */
    public String getEmployeeNameValue() {
        return inputEmployeeName.inputValue();
    }

    /**
     * Gets the current selected user role
     * @return The selected user role
     */
    public String getSelectedUserRole() {
        return selectUserRole.textContent().trim();
    }

    /**
     * Gets the current selected status
     * @return The selected status
     */
    public String getSelectedStatus() {
        return selectStatus.textContent().trim();
    }

    /**
     * Gets all user details from the edit form
     * @return A UserDetails object containing all user information
     */
    public UserDetails getUserDetails() {
        return new UserDetails(
            getUsernameValue(),
            getEmployeeNameValue(),
            getSelectedUserRole(),
            getSelectedStatus()
        );
    }

    /**
     * Inner class to hold user details
     */
    public static class UserDetails {
        private final String username;
        private final String employeeName;
        private final String userRole;
        private final String status;

        public UserDetails(String username, String employeeName, String userRole, String status) {
            this.username = username;
            this.employeeName = employeeName;
            this.userRole = userRole;
            this.status = status;
        }

        public String getUsername() { return username; }
        public String getEmployeeName() { return employeeName; }
        public String getUserRole() { return userRole; }
        public String getStatus() { return status; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserDetails that = (UserDetails) o;
            return username.equals(that.username) &&
                   employeeName.equals(that.employeeName) &&
                   userRole.equals(that.userRole) &&
                   status.equals(that.status);
        }

        @Override
        public String toString() {
            return "UserDetails{" +
                   "username='" + username + '\'' +
                   ", employeeName='" + employeeName + '\'' +
                   ", userRole='" + userRole + '\'' +
                   ", status='" + status + '\'' +
                   '}';
        }
    }
}
