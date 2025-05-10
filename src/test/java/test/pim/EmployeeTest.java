package test.pim;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import driver.BrowserManager;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.CommonAction;
import utils.ElementUtils;
import utils.RandomUtils;
import web.pages.dashboard.DashboardPage;
import web.pages.login.LoginPage;
import web.pages.pim.EmployeePage;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.testng.AssertJUnit.assertTrue;

public class EmployeeTest {
    private Page page;
    private LoginPage login;
    private DashboardPage dashboard;
    private EmployeePage employeePage;

    public void setUp(){
        page = BrowserManager.getPage();
        login = new LoginPage(page);
        dashboard = new DashboardPage(page);
        employeePage = new EmployeePage(page);
        employeePage.navigateToPage();
    }

    //add employee
    @Test(description = "Verify employee is added successfully without creating login details")
    public void addEmployeeWithoutLoginDetail() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, null, null, null);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue("Employee DOES NOT created", employeePage.isEmployeeCreated(empId));
    }

    @Test(description = "Verify employee is added successfully without creating login details")
    public void addEmployeeWithLoginDetail() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        String username = RandomUtils.generateRandomString(8);
        String status = employeePage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue("Employee DOES NOT created", employeePage.isEmployeeCreated(empId));
    }

    @Test(description = "Verify error message is displayed when leave required fields empty")
    public void addEmployeeWithEmptyRequiredFields() throws URISyntaxException {
        setUp();
        String middleName = RandomUtils.generateRandomString(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(null, middleName, null, null, null, null, null, null);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify employee is added successfully with special characters in Employee Full Name")
    public void addEmployeeWithSpecialCharInFullName() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomStringWithSpecialChars(8);
        String middleName = RandomUtils.generateRandomStringWithSpecialChars(8);
        String lastName = RandomUtils.generateRandomStringWithSpecialChars(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, null, null, null);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue("Employee DOES NOT created", employeePage.isEmployeeCreated(empId));
    }

    @Test(description = "Verify employee is added successfully with special characters in Employee Full Name")
    public void addEmployeeWithNumberInFullName() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateNumber(8);
        String middleName = RandomUtils.generateNumber(8);
        String lastName = RandomUtils.generateNumber(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, null, null, null);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }



}
