package test.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import factory.DriverFactory;

import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.*;
import web.constants.Constants;
import web.pages.admin.UserManagementPage;
import web.pages.dashboard.DashboardPage;
import web.pages.login.LoginPage;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.fail;


public class UserManagementTest {
    private Page page;
    private LoginPage login;
    private DashboardPage dashboard;
    private UserManagementPage userManagementPage;


    public void setUp(){
        page = DriverFactory.getPage();
        login = new LoginPage(page);
        dashboard = new DashboardPage(page);
        userManagementPage = new UserManagementPage(page);
        userManagementPage.navigateToPage();
    }

    //test search
    @DataProvider(name = "searchUserData")
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

    }




    //test add
    @Test(description = "Verify user is created successfully when input all field with valid value")
    public void createUserWithValidValue() {
        String employeeName = userManagementPage.randomEmployeeName();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password );
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue(userManagementPage.isUserCreated(username), "User DOEST NOT created");
    }


    @Test(description = "Verify error message is displayed when add user with invalid employee name ")
    public void createUser() {
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
        userManagementPage.addUser(employeeName,username,role,status,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when leaving mandatory fields empty")
    public void createUserWithEmptyMandatoryField() {
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(null,null,null,null,null);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when add user with special characters/number in username")
    public void createUserWithSpecialCharacterInUsername() {
        String employeeName = userManagementPage.randomEmployeeName();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue(userManagementPage.isUserCreated(username), "User DOEST NOT created");
    }

    @Test(description = "Verify error message is displayed when Add user with only spaces in username")
    public void createUserWithOnlySpaceInUsername() {
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
        userManagementPage.addUser(employeeName,username,role,status,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when add user with a username less than 5 characters")
    public void createUserWithUsernameLessThanFiveChars() {
        String employeeName = userManagementPage.randomEmployeeName();
        String username = RandomUtils.generateRandomString(5);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when add user with a username more than 40 characters")
    public void createUserWithUsernameLessThanFortyChars() {
        String employeeName = userManagementPage.randomEmployeeName();
        String username = RandomUtils.generateRandomString(40);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when add user using only lowercase letters in username")
    public void createUserWithOnlyLowercaseInUsername() {
        String employeeName = userManagementPage.randomEmployeeName();
        String username = RandomUtils.generateRandomString(8).toLowerCase();
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when add user using only uppercase letters in username")
    public void createUserWithOnlyUppercaseInUsername() {
        String employeeName = userManagementPage.randomEmployeeName();
        String username = RandomUtils.generateRandomString(8).toLowerCase();
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when enter password less than 7 characters")
    public void createUserWithPasswordLessThanSevenChars() {
        String employeeName = userManagementPage.randomEmployeeName();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(5);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }

    @Test(description = "Verify error message is displayed when Enter password more than 64 characters")
    public void createUserWithPasswordMoreThanSixtyFourChars() {
        String employeeName = userManagementPage.randomEmployeeName();
        String username = RandomUtils.generateRandomString(8);
        String role = userManagementPage.randomRole();
        String status = userManagementPage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(65);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        userManagementPage.clickAdd();
        userManagementPage.addUser(employeeName,username,role,status,password);
        assertTrue(userManagementPage.isErrorMessageDisplayed(),"Expected error message under input");
    }






    @Test(description = "Verify sort descending by username")
        public void checkSortDescending(){
            setUp();
            login.login("Admin", "admin123");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
            ElementUtils.getLinkInSideNav(page,"Admin").click();
            boolean result = userManagementPage.sortByColName("Employee Name", "desc");
            assertTrue(result, "Sort failed");
            ScreenShotUtils.takeScreenshot(page);
        }


        //test update




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

        }

    }
