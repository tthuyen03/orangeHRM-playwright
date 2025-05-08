package test.pim;

import com.microsoft.playwright.Page;
import factory.DriverFactory;
import io.qameta.allure.Step;
import utils.CommonAction;
import utils.RandomUtils;
import web.pages.admin.UserManagementPage;
import web.pages.dashboard.DashboardPage;
import web.pages.login.LoginPage;
import web.pages.pim.EmployeePage;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.testng.AssertJUnit.assertTrue;

public class EmployeeTest {
    private Page page;
    private LoginPage login;
    private DashboardPage dashboard;
    private EmployeePage employeePage;

    public void setUp(){
        page = DriverFactory.getPage();
        login = new LoginPage(page);
        dashboard = new DashboardPage(page);
        employeePage = new EmployeePage(page);
        employeePage.navigateToPage();
    }

    //add employee
    @Step("Verify employee is added successfully without creating login details")
    public void addEmployeeWithoutLoginDetail() throws URISyntaxException {
        setUp();
        String firstName = RandomUtils.generateRandomString(8);
        String middleName = RandomUtils.generateRandomString(8);
        String lastName = RandomUtils.generateRandomString(8);
        String empId = RandomUtils.generateNumber(5);
        String filePath =  Paths.get(ClassLoader.getSystemResource("data/images.png").toURI()).toString();
        employeePage.addEmployee(firstName, middleName, lastName, empId, filePath, null, null, null);
        assertTrue(CommonAction.isToastDisplayed(page,"Successfully Saved"));
        assertTrue("Employee DOES NOT created", employeePage.isEmployeeCreated(empId));
    }

}
