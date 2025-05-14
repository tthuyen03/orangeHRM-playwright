package test.admin;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import driver.BrowserManager;
import org.slf4j.Logger;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import utils.CommonAction;
import utils.ElementUtils;
import utils.RandomUtils;
import web.constants.Constants;
import web.pages.admin.UserManagementPage;
import web.pages.dashboard.DashboardPage;
import web.pages.login.LoginPage;

import java.util.Locale;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.fail;

@Epic("User Management")
@Feature("User CRUD Operations")
public class UserManagementTest {
    private static final Logger logger = LoggerFactory.getLogger(UserManagementTest.class);
    private Page page;
    private LoginPage login;
    private DashboardPage dashboard;
    private UserManagementPage userManagementPage;


    public void setUp(){
        page = BrowserManager.getPage();
        login = new LoginPage(page);
        dashboard = new DashboardPage(page);
        userManagementPage = new UserManagementPage(page);
        userManagementPage.navigateToPage();
    }

    //test search
   /* @DataProvider(name = "searchUserData")
    public Object[][] getSearchUserData() {
        return DataDrivenUtils.readDataFromExcel("TestData.xlsx", "SearchUsers");
    }

    @Test(description = "Verify search",dataProvider = "searchUserData", retryAnalyzer = RetryUtils.class)
    @Step("Verify search with Username: {0}, UserRole: {1}, EmployeeName: {2}, Status: {3} ")
    public void searchByUsername(String username, String role, String employeeName, String status, String expectedResult){
        setUp();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        if (username != null && !username.isBlank()) {
            userManagementPage.searchUserByUsername(username);
        }
        if (role != null && !role.isBlank()) {
            userManagementPage.searchByUserRole(role);
        }
        if (employeeName != null && !employeeName.isBlank()) {
            userManagementPage.getEmployeeName(employeeName);
        }

        if (status != null && !status.isBlank()) {
            userManagementPage.searchByStatus(status);
        }

        boolean actualResult = CommonAction.hasSearchResults(page);
        if(expectedResult.equals("found")){
            assertTrue(actualResult, "Expected results to be found but none were shown");
        }
        else{
            assertFalse(actualResult, "Expected no result, but some records appeared");
        }
    }

    @Test(description = "Clear search fields")
    public void clearSearchFields(){

    }*/




    //test add
  @Test(description = "Verify user is created successfully when input all field with valid value")
    public void createUserWithValidValue() {
        setUp();
        logger.info("Prepare data");
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);

        logger.info("Login and navigate to dashboard");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        logger.info("Click Admin ");
        ElementUtils.getLinkInSideNav(page,"Admin").click();

        logger.info("Click add");
        userManagementPage.clickAdd();
        logger.info("Add user with valid data");
        userManagementPage.addUser(employeeName,username,role,status,password,password );
        userManagementPage.clickSave();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        logger.info("Verify toast is displayed and new user appears in user list");
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));

        assertTrue(userManagementPage.isUserCreated(username), "User DOEST NOT created");
    }


   /*@Test(description = "Verify error message is displayed when add user with invalid employee name ")
    public void createUserWithInvalidEmpName() {
        setUp();
        String employeeName = RandomUtils.generateRandomString(8);
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when leaving mandatory fields empty")
    public void createUserWithEmptyMandatoryField() {
        setUp();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.clickSave();
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify user is created successfully when add user with special characters/number in username")
    public void createUserWithSpecialCharacterInUsername() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomStringWithSpecialChars(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(userManagementPage.isUserCreated(username), "User DOEST NOT created");
    }

    @Test(description = "Verify error message is displayed when add user with only spaces in username")
    public void createUserWithOnlySpaceInUsername() {
        setUp();
        String employeeName = RandomUtils.generateStringWithOnlySpaces();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when add user with a username less than 5 characters")
    public void createUserWithUsernameLessThanFiveChars() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(5);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when add user with a username more than 40 characters")
    public void createUserWithUsernameMoreThanFortyChars() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(41);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify user is created successfully when add user using only lowercase letters in username")
    public void createUserWithOnlyLowercaseInUsername() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(8).toLowerCase();
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(userManagementPage.isUserCreated(username), "User DOES NOT created");
    }

    @Test(description = "Verify user is created successfully when add user using only uppercase letters in username")
    public void createUserWithOnlyUppercaseInUsername() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(8).toUpperCase();
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(userManagementPage.isUserCreated(username), "User DOEST NOT created");
    }

    @Test(description = "Verify error message is displayed when enter password less than 7 characters")
    public void createUserWithPasswordLessThanSevenChars() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(5);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when enter password more than 64 characters")
    public void createUserWithPasswordMoreThanSixtyFourChars() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(65);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when enter only the password and leaves the confirm password field blank")
    public void createUserWithBlankConfirmPassword() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password, null);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when enter mismatched Password and Confirm Password")
    public void createUserWithBlankConfirmPassword() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password, confirmPassword);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when add user with leading spaces in username")
    public void createUserWithLeadingSpaceInUsername() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateStringWithLeadingSpaces(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password, confirmPassword);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(userManagementPage.isUserCreated(username), "User DOEST NOT created");
    }

    @Test(description = "Verify error message is displayed when add user with leading spaces in username")
    public void createUserWithTrailingSpaceInUsername() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateStringWithTrailingSpaces(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password, confirmPassword);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(userManagementPage.isUserCreated(username), "User DOEST NOT created");
    }

    @Test(description = "Verify error message is displayed when add user with leading and trailing spaces in username")
    public void createUserWithLeadingTrailingSpaceInUsername() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateStringWithLeadingTrailingSpaces(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password, confirmPassword);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(userManagementPage.isUserCreated(username), "User DOEST NOT created");
    }

    @Test(description = "Verify system behavior when adding the same username simultaneously", groups = {"create", "concurrency"})
    @Severity(SeverityLevel.CRITICAL)
    public void addSameUsernameSimultaneously(){
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateStringWithLeadingTrailingSpaces(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);

        //Login in tab 1
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();

        //Login in tab 2
        Page page2 = BrowserManager.getBrowser().newPage();
        page2.navigate(Constants.URL);
        LoginPage loginPage2 = new LoginPage(page2);
        DashboardPage dashboardPage2 = new DashboardPage(page2);
        UserManagementPage userMangementPage2 = new UserManagementPage(page2);
        loginPage2.login("Admin", "admin123");
        page2.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page2, "Admin").click();

        Thread threadPage1 = new Thread(()->{
            try{
                userManagementPage.clickAdd();
                userManagementPage.addUser(employeeName, username, role, status, password, confirmPassword);
                assertTrue(CommonAction.isToastDisplayed(page, "Successfully Saved"),
                        "Expected success message in first tab");

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });

        Thread threadPage2 = new Thread(()->{
            try{
                userManagementPage.clickAdd();
                userManagementPage.addUser(employeeName, username, role, status, password, confirmPassword);
                assertTrue(CommonAction.isToastDisplayed(page, "Successfully Saved"),
                        "Expected error message in second tab");

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });
        threadPage1.start();
        threadPage2.start();
        try{
            threadPage1.join();
            threadPage2.join();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Threads were interrupted");
        }
        // Verify final state
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(userManagementPage.isUserCreated(username),
                "User should be created successfully in first attempt");

        // Clean up
        page2.close();

    }

    @Test(description = "Verify canceling user creation returns to user list without creating user", groups = {"create", "negative"})
    @Severity(SeverityLevel.NORMAL)
    public void cancelUserCreation(){
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateStringWithLeadingTrailingSpaces(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickAdd();

        // Fill in user details
        userManagementPage.addUser(employeeName, username, role, status, password, password);

        // Click cancel instead of save
        userManagementPage.clickCancel();

        // Verify back at the user list
        assertTrue(userManagementPage.isUserListVisible(), "Should return to user list view");

        // Verify user was not created
        assertFalse(userManagementPage.isUserCreated(username),
                "User should not be created when canceling");

    }

    @Test(description = "Verify password strength indicator functionality")
    @Severity(SeverityLevel.NORMAL)
    public void verifyPasswordStrengthIndicator(){
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateStringWithLeadingTrailingSpaces(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickAdd();

        // Test Case 1: Very Weak Password (only lowercase letters)
        userManagementPage.enterPassword("password");
        assertTrue(userManagementPage.getPasswordStrengthLevel().equals("Very Weak"),
                "Password strength should be 'Very Weak' for lowercase only password");

        // Test Case 2: Weak Password (lowercase + numbers)
        userManagementPage.enterPassword("password123");
        assertTrue(userManagementPage.getPasswordStrengthLevel().equals("Very Weak"),
                "Password strength should be 'Very Weak' for lowercase + numbers only password");

        // Test Case 3: Weak Password (lowercase + uppercase + numbers)
        userManagementPage.enterPassword("Password123");
        assertTrue(userManagementPage.getPasswordStrengthLevel().equals("Weak"),
                "Password strength should be 'Weak' for lowercase + uppercase + numbers password");


        // Test Case 4: Better Password (lowercase + uppercase + numbers + special chars)
        String strongPassword = "Password123!@#";
        userManagementPage.enterPassword(strongPassword);
        assertTrue(userManagementPage.getPasswordStrengthLevel().equals("Better"),
                "Password strength should be 'Strong' for lowercase + uppercase + numbers + special chars password");

        // Test Case 5: Very Strong Password (longer complex password)
        String veryStrongPassword = "P@ssw0rd123!@#$%^&*";
        userManagementPage.enterPassword(veryStrongPassword);
        assertTrue(userManagementPage.getPasswordStrengthLevel().equals("Strong"),
                "Password strength should be 'Very Strong' for longer complex password");
    }

    @Test(description = "Verify error message is displayed when add user with existing username")
    @Severity(SeverityLevel.NORMAL)
    public void addUserWithExistingUsername(){
        setUp();
        String username = userManagementPage.randomExistingUsername();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.enterUsername(username);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message 'Already exists'");
    }

    @Test(description = "Verify error message is displayed when add user with existing username in uppercase")
    @Severity(SeverityLevel.NORMAL)
    public void addUserWithExistingUsernameInUppercase(){
        setUp();
        String username = userManagementPage.randomExistingUsername().toUpperCase();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.enterUsername(username);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message 'Already exists'");
    }

    @Test(description = "Verify error message is displayed when add user with existing username in uppercase")
    @Severity(SeverityLevel.NORMAL)
    public void addUserWithExistingUsernameInLowercase(){
        setUp();
        String username = userManagementPage.randomExistingUsername().toLowerCase();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.enterUsername(username);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message 'Already exists'");
    }


    //test update
    @Test(description = "Verify update user role successfully")
    @Severity(SeverityLevel.NORMAL)
    public void editUserRole(){
        setUp();
        String userRole = userManagementPage.randomRole();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(userRole);
        userManagementPage.enterUsername(userRole);
        userManagementPage.clickSave();
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Updated"),
                "Expected message 'Successfully Updated' display");
    }

    @Test(description = "Verify update employee name successfully")
    @Severity(SeverityLevel.NORMAL)
    public void editEmployeeName(){
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(employeeName);
        userManagementPage.enterUsername(employeeName);
        userManagementPage.clickSave();
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Updated"),
                "Expected message 'Successfully Updated' display");
    }

    @Test(description = "Verify update status successfully")
    @Severity(SeverityLevel.NORMAL)
    public void editStatus(){
        setUp();
        String status = userManagementPage.randomStatus();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(status);
        userManagementPage.enterUsername(status);
        userManagementPage.clickSave();
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Updated"),
                "Expected message 'Successfully Updated' display");
    }

    @Test(description = "Verify update status successfully")
    @Severity(SeverityLevel.NORMAL)
    public void ediUsername(){
        setUp();
        String username = RandomUtils.generateRandomString(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.enterUsername(username);
        userManagementPage.clickSave();
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Updated"),
                "Expected message 'Successfully Updated' display");
    }

    @Test(description = "Verify update user successfully without making any changes")
    @Severity(SeverityLevel.NORMAL)
    public void ediUsernameWithoutAnyChanges(){
        setUp();
        String username = RandomUtils.generateRandomString(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.clickSave();
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Updated"),
                "Expected message 'Successfully Updated' display");
    }

    @Test(description = "Cancel while updating user")
    @Severity(SeverityLevel.NORMAL)
    public void cancelWhileUpdatingUser(){
        setUp();
        String username = RandomUtils.generateRandomString(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.enterUsername(username);
        userManagementPage.clickCancel();
        assertTrue(userManagementPage.isUserListVisible(),
                "Expected user list display");
    }

    @Test(description = "Verify error message is displayed when update with empty mandatory fields")
    @Severity(SeverityLevel.NORMAL)
    public void eidtUserWithEmptyMandatoryFields(){
        setUp();
        String username = RandomUtils.generateRandomString(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.editUser(null,null,null,null,null,null);
        userManagementPage.clickCancel();
        assertTrue(userManagementPage.isErrorMessageDisplayed(),
                "Expected error message is displayed");
    }

    @Test(description = "Verify error message is displayed when update user with an existing username")
    @Severity(SeverityLevel.NORMAL)
    public void editUserWithExistingUsername(){
        setUp();
        String username = userManagementPage.randomValidEmployeeName();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.enterUsername(username);
        userManagementPage.clickCancel();
        assertTrue(userManagementPage.isErrorMessageDisplayed(),
                "Expected error message is displayed");
    }

    @Test(description = "Verify update successfully with leading spaces in username")
    @Severity(SeverityLevel.NORMAL)
    public void editUserWithLeadingUsername(){
        setUp();
        String username = RandomUtils.generateStringWithLeadingSpaces(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.enterUsername(username);
        userManagementPage.clickCancel();
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Updated"),
                "Expected toast message is displayed");
    }

    @Test(description = "Verify update successfully with leading spaces in username")
    @Severity(SeverityLevel.NORMAL)
    public void editUserWithTrailingUsername(){
        setUp();
        String username = RandomUtils.generateStringWithTrailingSpaces(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.enterUsername(username);
        userManagementPage.clickCancel();
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Updated"),
                "Expected toast message is displayed");
    }

    @Test(description = "Verify update successfully with leading spaces in username")
    @Severity(SeverityLevel.NORMAL)
    public void editUserWithLeadingTrailingUsername(){
        setUp();
        String username = RandomUtils.generateStringWithLeadingTrailingSpaces(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.enterUsername(username);
        userManagementPage.clickCancel();
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Updated"),
                "Expected toast message is displayed");
    }

    @Test(description = "Verify update successfully with special characters/number in username")
    @Severity(SeverityLevel.NORMAL)
    public void editUserWithSpecialCharacterInUsername(){
        setUp();
        String username = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.enterUsername(username);
        userManagementPage.clickCancel();
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Updated"),
                "Expected toast message is displayed");
    }

    @Test(description = "Verify error message is displayed when edit with mismatched Password and Confirm Password")
    public void editUserWithMismatchPassword() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = userManagementPage.randomExistingUsername();
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.editUser(employeeName,username,role,status,password, confirmPassword);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when update user with a username less than 5 characters")
    public void editUserWithUsernameLessThanFiveChars() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String oldUsername = userManagementPage.randomExistingUsername();
        String newUsername = RandomUtils.generateRandomStringWithSpecialChars(4);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickEdit(oldUsername);
        userManagementPage.editUser(employeeName,newUsername,role,status,password, confirmPassword);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when update user with a username more than 40 characters")
    public void editUserWithUsernameMoreThanFourtyChars() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String oldUsername = userManagementPage.randomExistingUsername();
        String newUsername = RandomUtils.generateRandomStringWithSpecialChars(41);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickEdit(oldUsername);
        userManagementPage.editUser(employeeName,newUsername,role,status,password, confirmPassword);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when enter password less than 7 characters")
    public void editUserWithPasswordLessThanSevenChars() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = userManagementPage.randomExistingUsername();
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(6);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.editUser(employeeName,username,role,status,password,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when enter password more than 64 characters")
    public void editUserWithPasswordMoreThanSixtyFourChars() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = userManagementPage.randomExistingUsername();
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(65);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickEdit(username);
        userManagementPage.editUser(employeeName,username,role,status,password,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify update user successfully when update user using only lowercase letters in username")
    public void editUserWithLowercaseInUsername() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String oldUsername = userManagementPage.randomExistingUsername();
        String newUsername = RandomUtils.generateRandomStringWithSpecialChars(8).toLowerCase();
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickEdit(oldUsername);
        userManagementPage.editUser(employeeName,newUsername,role,status,password, confirmPassword);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify update user successfully when update user using only uppercase letters in username")
    public void editUserWithUppercaseInUsername() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String oldUsername = userManagementPage.randomExistingUsername();
        String newUsername = RandomUtils.generateRandomStringWithSpecialChars(8).toUpperCase();
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickEdit(oldUsername);
        userManagementPage.editUser(employeeName,newUsername,role,status,password, confirmPassword);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify system behavior when editing the same username simultaneously")
    @Severity(SeverityLevel.CRITICAL)
    public void editSameUsernameSimultaneously(){
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String oldUsername = userManagementPage.randomExistingUsername();
        String newUsername = RandomUtils.generateStringWithLeadingTrailingSpaces(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);

        //Login in tab 1
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page, "Admin").click();

        //Login in tab 2
        Page page2 = BrowserManager.getBrowser().newContext().newPage();
        page2.navigate(Constants.URL);
        LoginPage loginPage2 = new LoginPage(page2);
        DashboardPage dashboardPage2 = new DashboardPage(page2);
        UserManagementPage userMangementPage2 = new UserManagementPage(page2);
        loginPage2.login("Admin", "admin123");
        page2.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page2, "Admin").click();

        Thread threadPage1 = new Thread(()->{
            try{
                userManagementPage.clickAdd();
                userManagementPage.editUser(employeeName, newUsername, role, status, password, confirmPassword);
                assertTrue(CommonAction.isToastDisplayed(page, "Successfully Saved"),
                        "Expected success message in first tab");

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });

        Thread threadPage2 = new Thread(()->{
            try{
                userManagementPage.clickAdd();
                userManagementPage.addUser(employeeName, newUsername, role, status, password, confirmPassword);
                assertTrue(CommonAction.isToastDisplayed(page, "Successfully Saved"),
                        "Expected error message in second tab");

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });
        threadPage1.start();
        threadPage2.start();
        try{
            threadPage1.join();
            threadPage2.join();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Threads were interrupted");
        }
        // Verify final state
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(userManagementPage.isUserCreated(newUsername),
                "User should be created successfully in first attempt");

        // Clean up
        page2.close();

    }*/







 /*   public void checkSortDescending(){
        setUp();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        boolean result = userManagementPage.sortByColName("Employee Name", "desc");
        assertTrue(result, "Sort failed");
        //ScreenShotUtils.takeScreenshot(page);
    }







        //test delete
         @Test(description = "Delete a user")
        public void deleteSingleUser(){
            setUp();
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            ElementUtils.getLinkInSideNav(page,"Admin").click();
            CommonAction.navigateToOptionOfTopPage(page, "User Management","Users");
            List<String> userList = List.of( "FMLName");
            CommonAction.deleteSingleOrMultiple(page,"Username", userList);
        }

        @Test(description = "Delete multiple users")
        public void deleteMultipleUsers(){
            setUp();
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            ElementUtils.getLinkInSideNav(page,"Admin").click();
            CommonAction.navigateToOptionOfTopPage(page, "User Management","Users");
            List<String> userList = List.of( "FMLName1","Hermann-Simonis");
            CommonAction.deleteSingleOrMultiple(page,"Username", userList);
        }

        @Test(description = "Delete all users")
        public void deleteAllUsers(){
            setUp();
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            ElementUtils.getLinkInSideNav(page,"Admin").click();
            CommonAction.navigateToOptionOfTopPage(page, "User Management","Users");
            CommonAction.deleteAll(page);
        }

        @Test(description = "Delete a user and then try to login with that user")
        public void loginAfterDelete(){
            setUp();
            //login and delete
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            ElementUtils.getLinkInSideNav(page,"Admin").click();
            CommonAction.navigateToOptionOfTopPage(page, "User Management","Users");
            List<String> userList = List.of( "test user");
            CommonAction.deleteSingleOrMultiple(page,"Username", userList);

            //logout
            login.clickLogout();
            //login again with deleted account
            login.login("test user", "Minh2025@");
            assertTrue(login.isInvalidCredentialsVisible(), "Expected invalid credentials message");
        }

        @Test(description = "Ensure system prevents deleting critical/admin account if not allowed")
        public void checkPreventDeleteCriticalAccount(){
            setUp();
            //login
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            //navigate to User page
            ElementUtils.getLinkInSideNav(page,"Admin").click();
            CommonAction.navigateToOptionOfTopPage(page, "User Management","Users");
            //press delete Admin account
            userManagementPage.pressDeleteButtonOnTable("Admin");
            //wait toast display
            Locator toast = ElementUtils.toastCannotDelete(page);
            toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            assertTrue(toast.isVisible(), "Expected a toast message indicating deletion is not allowed");
        }


        @Test(description = "Delete the same username simultaneously")
        public void deleteSameUserSimultaneously(){
            setUp();
            //tab 1
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            ElementUtils.getLinkInSideNav(page,"Admin").click();
            CommonAction.navigateToOptionOfTopPage(page, "User Management","Users");

            //create tab 2
            Page page2 = DriverFactory.getContext().newPage();
            page2.navigate(Constants.URL);
            LoginPage login2 = new LoginPage(page2);
            login2.login("Admin", "admin123");
            page2.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Expected dashboard to be visible");
            ElementUtils.getLinkInSideNav(page,"Admin").click();
            CommonAction.navigateToOptionOfTopPage(page, "User Management","Users");

            //delete in both tabs simultaneously
            String username = "minh22222";
            Thread thread1 = new Thread(()->{
                userManagementPage.pressDeleteButtonOnTable(username);
                Locator toastInTab1 = ElementUtils.toastMessage(page,"Successfully Deleted");
                toastInTab1.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                assertTrue(toastInTab1.isVisible(), "Expected toast in tab 1 displayed");
            });

            Thread thread2 = new Thread(()->{
                new UserManagementPage(page2).pressDeleteButtonOnTable(username);
                Locator toastInTab2 = ElementUtils.toastMessage(page,"Records Not Found");
                toastInTab2.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                assertTrue(toastInTab2.isVisible(), "Expected toast in tab 2 displayed");
            });

            //run thread 1 and thread 2 simultaneously
            thread1.start();
            thread2.start();

            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Threads were interrupted");
            }

        }*/

    }
