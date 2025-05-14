package test.pim;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import driver.BrowserManager;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.admin.UserManagementTest;
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
    private static final Logger logger = LoggerFactory.getLogger(EmployeeTest.class);
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
        logger.info("Log in with Admin credentials");
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        logger.info("Verifying dashboard is visible after login");
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        logger.info("Navigating to PIM section via side navigation");
        ElementUtils.getLinkInSideNav(page, "PIM").click();
        logger.info("Clicking on 'Add Employee'");
        employeePage.clickAdd();
        logger.info("Filling employee details and submitting without login info");
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, null, null, null,null);
        logger.info("Verifying success toast message appears");
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        logger.info("Verifying employee was actually created in the list");
        assertTrue("Employee DOES NOT created", employeePage.isEmployeeCreated(empId));
    }

    /*@Test(description = "Verify employee is added successfully with creating login details")
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
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password,password);
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
        employeePage.addEmployee(null, middleName, null, null, null, null, null, null,null);
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
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, null, null, null,null);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue("Employee DOES NOT created", employeePage.isEmployeeCreated(empId));
    }

    @Test(description = "Verify employee is added successfully with number in Employee Full Name")
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
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, null, null, null,null);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that image is uploaded successfully when the file extension is in uppercase")
    public void addEmployeeWithFileExtensionInUppercase() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateNumber(8);
        String middleName = RandomUtils.generateNumber(8);
        String lastName = RandomUtils.generateNumber(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/hinh.PNG").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.addEmployee(firstName,middleName,lastName,empId,filePath,null,null,null,null);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue("Employee DOES NOT created", employeePage.isEmployeeCreated(empId));
    }

    @Test(description = "Verify that image file is uploaded successfully with the file name contains space")
    public void addEmployeeWithFileContainSpaces() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/hinh anh.png").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.addEmployee(firstName,middleName,lastName,empId,filePath,null,null,null,null);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue("Employee DOES NOT created", employeePage.isEmployeeCreated(empId));
    }

    @Test(description = "Verify that image file is uploaded successfully with the file name contains some special characters")
    public void addEmployeeWithFileContainSpecialChars() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/KyTu@@.png").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.addEmployee(firstName,middleName,lastName,empId,filePath,null,null,null,null);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue("Employee DOES NOT created", employeePage.isEmployeeCreated(empId));
    }

    @Test(description = "Verify that an error message is displayed when uploading image with invalid file type")
    public void addEmployeeWithInvalidFileType() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/test.docx").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.addEmployee(firstName,middleName,lastName,empId,filePath,null,null,null,null);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed with valid image size exceeding 1MB")
    public void addEmployeeWithSizeExceed() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/test.docx").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.addEmployee(firstName,middleName,lastName,empId,filePath,null,null,null,null);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that new employee is added successfully when user does not upload image")
    public void addEmployeeWithoutUploadImage() {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.addEmployee(firstName,middleName,lastName,empId,null,null,null,null,null);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue("Employee DOES NOT created", employeePage.isEmployeeCreated(empId));
    }

    @Test(description = "Verify that a new employee is added successfully when the Middle Name field is left blank")
    public void addEmployeeWithEmptyMiddleName() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/test.docx").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.addEmployee(firstName,null,lastName,empId,filePath,null,null,null,null);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue("Employee DOES NOT created", employeePage.isEmployeeCreated(empId));
    }

    @Test(description = "Verify that an error message is displayed when the First Name, Last Name field exceeds 30 characters")
    public void addEmployeeWithMoreThanThirtyChars() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(34);
        String lastName = RandomUtils.generateRandomString(34);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/test.docx").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.addEmployee(firstName,null,lastName,empId,filePath,null,null,null,null);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when Employee Id is duplicate")
    public void addEmployeeWithDuplicateEmployeeId() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(34);
        String lastName = RandomUtils.generateRandomString(34);
        String empId = employeePage.randomExistingEmployeeId();
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/test.docx").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.addEmployee(firstName,null,lastName,empId,filePath,null,null,null,null);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when the Employee ID is duplicated and contains leading spaces")
    public void addEmployeeWithDuplicateAndContainLeadingSpaceInEmployeeId() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(34);
        String lastName = RandomUtils.generateRandomString(34);
        String empId = " " + employeePage.randomExistingEmployeeId();
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/test.docx").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.addEmployee(firstName,null,lastName,empId,filePath,null,null,null,null);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when the Employee ID is duplicated and contains trailing spaces")
    public void addEmployeeWithDuplicateAndContainTrailingSpaceInEmployeeId() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(34);
        String lastName = RandomUtils.generateRandomString(34);
        String empId = employeePage.randomExistingEmployeeId() + " ";
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/test.docx").toURI()).toString();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.addEmployee(firstName,null,lastName,empId,filePath,null,null,null,null);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when Username is duplicate")
    public void addEmployeeWithDuplicateUsername() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        String username = employeePage.randomExistingUsername();
        String status = employeePage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password,password);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when a duplicate Username is entered with uppercase")
    public void addEmployeeWithDuplicateAndUppercaseInUsername() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        String username = employeePage.randomExistingUsername().toUpperCase();
        String status = employeePage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password,password);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when a duplicate Username is entered with lowercase")
    public void addEmployeeWithDuplicateAndLowercaseInUsername() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        String username = employeePage.randomExistingUsername().toLowerCase();
        String status = employeePage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password,password);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when the Username is duplicated and contains leading spaces")
    public void addEmployeeWithDuplicateAndLeadingInUsername() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        String username = " " + employeePage.randomExistingUsername();
        String status = employeePage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password,password);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when the Username is duplicated and contains trailing spaces")
    public void addEmployeeWithDuplicateAndTrailingInUsername() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        String username = employeePage.randomExistingUsername() + " ";
        String status = employeePage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password,password);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when the Password and Confirm Password fields do not match")
    public void addEmployeeWithMismatchPassword() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        String username = employeePage.randomExistingUsername() + " ";
        String status = employeePage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);
        String confirmPassword = RandomUtils.generateRandomStringWithSpecialChars(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password,confirmPassword);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when user input Username fields under 5 characters")
    public void addEmployeeWithLessThanFiveCharacterInUsername() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        String username = RandomUtils.generateRandomString(4);
        String status = employeePage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password,password);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when user input Username fields more than 40 characters")
    public void addEmployeeWithMoreThanFortyCharacterInUsername() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        String username = RandomUtils.generateRandomString(41);
        String status = employeePage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(8);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password,password);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when user input Password under 7 characters")
    public void addEmployeeWithLessThanSevenCharacterInPassword() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        String username = RandomUtils.generateRandomString(8);
        String status = employeePage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(5);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password,password);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }

    @Test(description = "Verify that an error message is displayed when user input Password exceeding 64 characters")
    public void addEmployeeWithMoreThanSixtyFourCharacterInPassword() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        String username = RandomUtils.generateRandomString(8);
        String status = employeePage.randomStatus();
        String password = RandomUtils.generateRandomStringWithSpecialChars(65);

        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");

        ElementUtils.getLinkInSideNav(page, "PIM").click();
        employeePage.clickAdd();
        employeePage.clickToggleLogin();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, username, status, password,password);
        assertTrue("Expected error message display under input", employeePage.isErrorMessageDisplayed());
    }*/



}
