package test.admin;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import factory.DriverFactory;
import org.testng.annotations.Test;
import utils.CommonAction;
import utils.ElementUtils;
import web.pages.admin.JobPage;
import web.pages.dashboard.DashboardPage;
import web.pages.login.LoginPage;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import static org.testng.Assert.assertTrue;

public class JobTest {
    private Page page;
    private LoginPage login;
    private DashboardPage dashboard;
    private JobPage jobPage;
    private String filePath = "";


    public void init() throws URISyntaxException {
        page = DriverFactory.getPage();
        login = new LoginPage(page);
        dashboard = new DashboardPage(page);
        jobPage = new JobPage(page);
        login.navigateToPage();
        filePath = Paths.get(ClassLoader.getSystemResource("data/test.docx").toURI()).toString();

    }

    @Test(description = "Verify create job title successfully")
    public void addJobTitle() throws URISyntaxException {
        init();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        ElementUtils.getLinkInTopNav(page,"Job Titles");
        jobPage.addJobTitle("Fresher Tester", "manual", filePath, "");
        assertTrue(CommonAction.verifyAddSuccess(page,"Job Titles","Fresher Tester"), "Add Job Titles failed");
    }

    @Test()
    public void updateJobTitle() throws URISyntaxException {
        init();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        CommonAction.navigateToOptionOfTopPage(page, "Job","Job Titles");
        jobPage.editJobTitle("Chief Executive Officer", "Chief Executive Officer", "officer", filePath, "");
    }

    /*@Test()
    public void deleteByJobTitle() throws URISyntaxException {
        init();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        CommonAction.navigateToOptionOfJobPage(page, "Job","Job Titles");
        List<String> jobsToDelete = List.of( "Chief Executive Officer");
        CommonAction.deleteSingleOrMultiple(page,"Job Title", jobsToDelete);
    }

     @Test()
    public void deleteAllJob() throws URISyntaxException {
        init();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
        CommonAction.navigateToOptionOfJobPage(page, "Job","Job Titles");
         CommonAction.deleteAll(page);
    }

   @Test()
    public void deleteMultipleJob() throws URISyntaxException {
        init();
        login.login("Admin", "admin123");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(dashboard.isDashboardVisible(), "Dashboard should be visible after successful login");
        ElementUtils.getLinkInSideNav(page,"Admin").click();
       CommonAction.navigateToOptionOfJobPage(page, "Job","Job Titles");
       List<String> jobsToDelete = List.of("Content Specialist", "Head of Support");
       CommonAction.deleteSingleOrMultiple(page,"Job Title", jobsToDelete);
    }*/
}
