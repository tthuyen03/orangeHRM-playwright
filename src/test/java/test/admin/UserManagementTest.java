package test.admin;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import browser.BrowserManager;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;
import utils.*;

import web.pages.admin.UserManagementPage;
import web.pages.dashboard.DashboardPage;
import web.pages.login.LoginPage;

import static org.testng.Assert.*;

@Epic("User Management")
@Feature("User CRUD Operations")
public class UserManagementTest {
    private static final Logger logger = LoggerFactory.getLogger(UserManagementTest.class);
    private Page page;
    private UserManagementPage userManagementPage;



    public void setUp() {
        logger.info("Setting up test method");
        page = BrowserManager.getPage();
        userManagementPage = new UserManagementPage(page);
        userManagementPage.navigateToPage();
    }

   
    @Test(description = "Verify user creation with valid data", groups = {"create", "smoke"})
    @Severity(SeverityLevel.CRITICAL)
    public void createUserWithValidValue() {
        try {
            setUp();
            String employeeName = userManagementPage.randomValidEmployeeName();
            String username = RandomUtils.generateRandomString(8);
            String role = userManagementPage.randomRole();
            String status = userManagementPage.randomStatus();
            String password = RandomUtils.generateRandomStringWithSpecialChars(8);
            ElementUtils.getLinkInSideNav(page, "Admin").click();
            logger.info("2.Click button Add user");
            userManagementPage.clickAdd();
            logger.info("3.Add user with valid data");
            userManagementPage.addUser(employeeName, username, role, status, password, password);
            userManagementPage.clickSave();
            logger.info("4.Verify success message is displayed");
            assertTrue(CommonAction.isToastDisplayed(page, "Successfully Saved"), "Success message not displayed");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            logger.info("5.Verify user is created successfully and displayed in user list");
            assertTrue(userManagementPage.isUserCreated(username), "User was not created successfully");
        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage());
            throw e;
        }
    }

    /*@Test(description = "Verify error message for invalid employee name-----")
    @Severity(SeverityLevel.NORMAL)
    public void createUserWithInvalidEmpName() {
        try {
            setUp();
            String employeeName = userManagementPage.randomInvalidEmployeeName();
            String username = RandomUtils.generateRandomString(8);
            String role = userManagementPage.randomRole();
            String status = userManagementPage.randomStatus();
            String password = RandomUtils.generateRandomStringWithSpecialChars(8);

            logger.info("Login");
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

            logger.info("Navigate to user management");
            ElementUtils.getLinkInSideNav(page, "Admin").click();
            logger.info("Click button Add user");
            userManagementPage.clickAdd();
            logger.info("Add user with invalid employee name");
            userManagementPage.addUser(employeeName, username, role, status, password, password);
            logger.info("Click button Save");
            userManagementPage.clickSave();
            logger.info("Verify error message is displayed");
            assertTrue(userManagementPage.isErrorMessageDisplayed(), "Expected error message not displayed");
        } catch (Exception e) {
            logger.error("Test failed: {}", e.getMessage());
            AllureUtils.takeScreenshot(page, "createUserWithInvalidEmpName_failure");
            throw e;
        }
    }*/

    /*@Test(description = "Verify error message is displayed when leaving mandatory fields empty")
    public void createUserWithEmptyMandatoryField() {
        setUp();
        logger.info("Login");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("Navigate to user management");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        logger.info("Click button Add user");
        userManagementPage.clickAdd();
        logger.info("Click button Save");
        userManagementPage.clickSave();
        logger.info("Verify error message is displayed");
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
        logger.info("1. Login");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("2. Navigate to user management");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        logger.info("3. Click button Add user");
        userManagementPage.clickAdd();
        logger.info("4. Add user with special character in username");
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        logger.info("5. Verify success message is displayed");
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        page.waitForLoadState(LoadState.NETWORKIDLE);
        logger.info("6. Verify user is created successfully and displayed in user list");
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
        logger.info("1. Login");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("2. Navigate to user management");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        logger.info("3. Click button Add user");
        userManagementPage.clickAdd();
        logger.info("4. Add user with only spaces in username");
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        logger.info("5. Verify error message is displayed");
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
        logger.info("1. Login");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("2. Navigate to user management");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        logger.info("3. Click button Add user");
        userManagementPage.clickAdd();
        logger.info("4. Add user with a username less than 5 characters");
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        logger.info("5. Verify error message is displayed");
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }*/

    /*@Test(description = "Verify error message is displayed when add user with a username more than 40 characters")
    public void createUserWithUsernameMoreThanFortyChars() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(41);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        logger.info("1. Login");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("2. Navigate to user management");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        logger.info("3. Click button Add user");
        userManagementPage.clickAdd();
        logger.info("4. Add user with a username more than 40 characters");
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        logger.info("5. Verify error message is displayed");
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify user is created successfully when add user using only lowercase letters in username")
    public void createUserWithOnlyLowercaseInUsername() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = userManagementPage.randomExistingUsername().toLowerCase();
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        logger.info("1. Login");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("2. Navigate to user management");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        logger.info("3. Click button Add user");
        userManagementPage.clickAdd();
        logger.info("4. Add user with only lowercase in username");
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        logger.info("5. Verify error message is displayed");
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify user is created successfully when add user using only uppercase letters in username")
    public void createUserWithOnlyUppercaseInUsername() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = userManagementPage.randomExistingUsername().toUpperCase();
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        logger.info("1. Login");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("2. Navigate to user management");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        logger.info("3. Click button Add user");
        userManagementPage.clickAdd();
        logger.info("4. Add user with only uppercase in username");
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        logger.info("5. Verify error message is displayed");
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when enter password less than 7 characters")
    public void createUserWithPasswordLessThanSevenChars() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(5);
        logger.info("1. Login");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("2. Navigate to user management");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        logger.info("3. Click button Add user");
        userManagementPage.clickAdd();
        logger.info("4. Add user with password less than 7 characters");
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        logger.info("5. Verify error message is displayed");
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
        logger.info("1. Login");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("2. Navigate to user management");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        logger.info("3. Click button Add user");
        userManagementPage.clickAdd();
        logger.info("4. Add user with password more than 64 characters");
        userManagementPage.addUser(employeeName,username,role,status,password,password);
        logger.info("5. Verify error message is displayed");
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
        logger.info("1. Login");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("2. Navigate to user management");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        logger.info("3. Click button Add user");
        userManagementPage.clickAdd();
        logger.info("4. Add user with blank confirm password");
        userManagementPage.addUser(employeeName,username,role,status,password, "");
        userManagementPage.clickSave();
        logger.info("5. Verify error message is displayed");
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when enter mismatched Password and Confirm Password")
    public void createUserWithMismatchedPassword() {
        setUp();
        String employeeName = userManagementPage.randomValidEmployeeName();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);
        logger.info("1. Login");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("2. Navigate to user management");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        logger.info("3. Click button Add user");
        userManagementPage.clickAdd();
        logger.info("4. Add user with mismatched Password and Confirm Password");
        userManagementPage.addUser(employeeName,username,role,status,password, confirmPassword);
        logger.info("5. Verify error message is displayed");
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Clear search fields")
    public void clearSearchFields(){

    }

    @Test(description = "Verify error message is displayed when add user with leading/trailing spaces in username")
    @Severity(SeverityLevel.NORMAL)
    public void createUserWithLeadingTrailingSpacesInUsername() {
        try {
            setUp();
            String employeeName = userManagementPage.randomValidEmployeeName();
            String username = " " + userManagementPage.randomExistingUsername() + " ";
            String role = userManagementPage.randomRole();
            String status = userManagementPage.randomStatus();
            String password = RandomUtils.generateRandomStringWithSpecialChars(8);
            logger.info("1. Login");
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            logger.info("2. Navigate to user management");
            ElementUtils.getLinkInSideNav(page, "Admin").click();
            logger.info("3. Click button Add user");
            userManagementPage.clickAdd();
            logger.info("4. Add user with leading/trailing spaces in username");
            userManagementPage.addUser(employeeName, username, role, status, password, password);
            userManagementPage.clickSave();
            logger.info("5. Verify error message is displayed");
            assertTrue(userManagementPage.isErrorMessageDisplayed(), "Expected error message not displayed");
        } catch (Exception e) {
            logger.error("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify error message is displayed when add user with leading spaces in username")
    @Severity(SeverityLevel.NORMAL)
    public void createUserWithLeadingSpacesInUsername() {
        try {
            setUp();
            String employeeName = userManagementPage.randomValidEmployeeName();
            String username = " " + userManagementPage.randomExistingUsername();
            String role = userManagementPage.randomRole();
            String status = userManagementPage.randomStatus();
            String password = RandomUtils.generateRandomStringWithSpecialChars(8);
            logger.info("1. Login");
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            logger.info("2. Navigate to user management");
            ElementUtils.getLinkInSideNav(page, "Admin").click();
            logger.info("3. Click button Add user");
            userManagementPage.clickAdd();
            logger.info("4. Add user with leading spaces in username");
            userManagementPage.addUser(employeeName, username, role, status, password, password);
            userManagementPage.clickSave();
            logger.info("5. Verify error message is displayed");
            assertTrue(userManagementPage.isErrorMessageDisplayed(), "Expected error message not displayed");
        } catch (Exception e) {
            logger.error("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Verify error message is displayed when add user with trailing spaces in username")
    @Severity(SeverityLevel.NORMAL)
    public void createUserWithTrailingSpacesInUsername() {
        try {
            setUp();
            String employeeName = userManagementPage.randomValidEmployeeName();
            String username = userManagementPage.randomExistingUsername() + " ";
            String role = userManagementPage.randomRole();
            String status = userManagementPage.randomStatus();
            String password = RandomUtils.generateRandomStringWithSpecialChars(8);
            logger.info("1. Login");
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            logger.info("2. Navigate to user management");
            ElementUtils.getLinkInSideNav(page, "Admin").click();
            logger.info("3. Click button Add user");
            userManagementPage.clickAdd();
            logger.info("4. Add user with trailing spaces in username");
            userManagementPage.addUser(employeeName, username, role, status, password, password);
            logger.info("5. Verify error message is displayed");
            assertTrue(userManagementPage.isErrorMessageDisplayed(), "Expected error message not displayed");
        } catch (Exception e) {
            logger.error("Test failed: " + e.getMessage());
            throw e;
        }
    }*/

    /*@Test(description = "Verify system behavior when adding the same username simultaneously")
    @Severity(SeverityLevel.CRITICAL)
    public void addSameUsernameSimultaneously() {
        try {
            setUp();
            String employeeName = userManagementPage.randomValidEmployeeName();
            String username = RandomUtils.generateRandomString(8);
            String role = userManagementPage.randomRole();
            String status = userManagementPage.randomStatus();
            String password = RandomUtils.generateRandomStringWithSpecialChars(8);

            logger.info("1. Login in first tab and navigate to User Management page");
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            ElementUtils.getLinkInSideNav(page, "Admin").click();

            // Create second tab
            Page page2 = DriverFactory.getContext().newPage();
            page2.navigate(ConfigLoader.getProperty("URL"));
            LoginPage login2 = new LoginPage(page2);
            DashboardPage dashboard2 = new DashboardPage(page2);
            UserManagementPage userManagementPage2 = new UserManagementPage(page2);

            // Login in second tab
            logger.info("2. Login in second tab and navigate to User Management page");
            login2.login("Admin", "admin123");
            page2.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard2.isDashboardVisible(), "Dashboard should be visible in second tab");
            ElementUtils.getLinkInSideNav(page2, "Admin").click();

            // Create threads for simultaneous user creation
            Thread thread1 = new Thread(() -> {
                try {
                    logger.info("3. Add user in first tab");
                    userManagementPage.clickAdd();
                    userManagementPage.addUser(employeeName, username, role, status, password, password);
                    assertTrue(CommonAction.isToastDisplayed(page, "Successfully Saved"),
                        "Expected success message in first tab");
                } catch (Exception e) {
                    logger.error("Error in thread 1: " + e.getMessage());
                }
            });

            Thread thread2 = new Thread(() -> {
                try {
                    logger.info("4. Add user in second tab");
                    userManagementPage2.clickAdd();
                    userManagementPage2.addUser(employeeName, username, role, status, password, password);
                    // Second attempt should fail
                    assertTrue(userManagementPage2.isErrorMessageDisplayed(),
                        "Expected error message in second tab");
                } catch (Exception e) {
                    logger.error("Error in thread 2: " + e.getMessage());
                }
            });

            // Start both threads
            thread1.start();
            thread2.start();
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Threads were interrupted");
            }

            // Verify final state
            logger.info("5. Verify add successfully in first tab");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(userManagementPage.isUserCreated(username),
                "User should be created successfully in first attempt");

            // Clean up
            page2.close();
        } catch (Exception e) {
            logger.error("Test failed: " + e.getMessage());
            throw e;
        }
    }*/

    /*@Test(description = "Verify canceling user creation returns to user list without creating user")
    @Severity(SeverityLevel.NORMAL)
    public void cancelUserCreation() {
        try {
            setUp();
            String employeeName = userManagementPage.randomValidEmployeeName();
            String username = RandomUtils.generateRandomString(8);
            String role = userManagementPage.randomRole();
            String status = userManagementPage.randomStatus();
            String password = RandomUtils.generateRandomStringWithSpecialChars(8);


            // Login and navigate to user management
            logger.info("1. Login and navigate to user management");
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            ElementUtils.getLinkInSideNav(page, "Admin").click();

            // Start adding a user
            logger.info("2. Add a user");
            userManagementPage.clickAdd();

            // Fill in user details
            logger.info("3. Fill in user details");
            userManagementPage.addUser(employeeName, username, role, status, password, password);

            logger.info("4. Click Cancel button");
            userManagementPage.clickCancel();
            page.waitForLoadState(LoadState.NETWORKIDLE);

            logger.info("5. Verify canceling user creation returns to user list view");
            assertTrue(userManagementPage.isUserListVisible(), "Should return to user list view");

            logger.info("6. Verify user is not created when canceling");
            assertFalse(userManagementPage.isUserCreated(username),
                "User should not be created when canceling");

        } catch (Exception e) {
            logger.error("Test failed: " + e.getMessage());
            throw e;
        }
    }*/

    /*@Test(description = "Verify password strength indicator functionality")
    @Severity(SeverityLevel.NORMAL)
    public void verifyPasswordStrengthIndicator() {
        try {
            setUp();

            logger.info("1. Login and navigate to user management");
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            ElementUtils.getLinkInSideNav(page, "Admin").click();

            logger.info("2. Click button Add a user");
            userManagementPage.clickAdd();

            logger.info("3. Verify display Very Weak indicator");
            String veryWeakPassword = "password";
            userManagementPage.enterPassword(veryWeakPassword);
            assertTrue(userManagementPage.getPasswordStrengthLevel().equals("Very Weak"),
                "Password strength should be 'Very Weak'");

            logger.info("4. Verify display Weak indicator");
            String weakPassword = "password1232";
            userManagementPage.enterPassword(weakPassword);
            page.waitForTimeout(1000);
            assertTrue(userManagementPage.getPasswordStrengthLevel().equals("Weak"),
                "Password strength should be 'Weak'");

            logger.info("5. Verify display Better indicator");
            String betterPassword = "Password123!@#";
            userManagementPage.enterPassword(betterPassword);
            page.waitForTimeout(1000);
            assertTrue(userManagementPage.getPasswordStrengthLevel().equals("Better"),
                "Password strength should be 'Strong'");

            logger.info("6. Verify display Strong indicator");
            String veryStrongPassword = "P@ssw0rd123!@#$%^&*";
            userManagementPage.enterPassword(veryStrongPassword);
            page.waitForTimeout(1000);
            assertTrue(userManagementPage.getPasswordStrengthLevel().equals("Strong"),
                "Password strength should be 'Very Strong' for longer complex password");

            logger.info("7. Verify display Strongest indicator");
            String veryStrongestPassword = "P@ssw0rd123!@#$%^&*23@";
            userManagementPage.enterPassword(veryStrongestPassword);
            page.waitForTimeout(1000);
            assertTrue(userManagementPage.getPasswordStrengthLevel().equals("Strongest"),
                    "Password strength should be 'Very Strong' for longer complex password");


        } catch (Exception e) {
            logger.error("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "Sort by Employee Name in descending order")
     public void checkSortDescending(){
           setUp();
           logger.info("1. Login and navigate to user management");
           login.login("Admin", "admin123");
           page.waitForLoadState(LoadState.NETWORKIDLE);
           assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
           ElementUtils.getLinkInSideNav(page,"Admin").click();
           logger.info("2. Sort by Employee Name in descending order");
           boolean result = userManagementPage.sortByColName("Employee Name", "desc");
           assertTrue(result, "Sort failed");
       }*/


           //test update




           //test delete
           /* @Test(description = "Delete a user")
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
