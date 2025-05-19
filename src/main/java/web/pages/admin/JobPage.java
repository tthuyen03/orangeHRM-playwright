package web.pages.admin;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import utils.CommonAction;
import utils.ElementUtils;
import web.base.BasePage;
import java.nio.file.Paths;
import java.util.List;


public class JobPage extends BasePage {
    //Job Title

    private final Locator inputJobDescription = page.getByPlaceholder("Type description here");
    private final Locator inputNote = page.getByPlaceholder("Add note");
    private final Locator btnBrowse = page.getByText("Browse");

    //Work shift
    private final Locator btnUpFromHour = page.locator("//div[@class='oxd-time-hour-input']//i[contains(@class,'oxd-time-hour-input-up')]");
    private final Locator btnDownFromHour = page.locator("//div[@class='oxd-time-hour-input']//i[contains(@class,'oxd-time-hour-input-down')]");
    private final Locator btnUpToHour = page.locator("//div[@class='oxd-time-minute-input']//i[contains(@class, 'oxd-time-minute-input-up')]");
    private final Locator btnDownToHour = page.locator("//div[@class='oxd-time-minute-input']//i[contains(@class, 'oxd-time-minute-input-down')]");
    private final Locator textFromHour = page.locator("//div[@class='oxd-time-hour-input']//input[contains(@class, 'oxd-input')]");
    private final Locator textToHour = page.locator("//div[@class='oxd-time-minute-input']//input[contains(@class, 'oxd-input')]");
    private final Locator labelAM = page.locator("//label[@for='am']");
    private final Locator labelPM = page.locator("//label[@for='pm']");

    public JobPage(Page page){
        super(page);
    }


    //----Job Title------
    @Step("Add Job Title")
    public void addJobTitle(String jobName, String jobDescription, String filePath, String note){
        ElementUtils.btnAdd(page).click();
        ElementUtils.inputText(page,"Job Title").fill(jobName);
        inputJobDescription.fill(jobDescription);
        FileChooser fileChooser = page.waitForFileChooser(() -> btnBrowse.click());
        fileChooser.setFiles(Paths.get(filePath));
        inputNote.fill(note);
        ElementUtils.btnSave(page).click();
    }


    @Step("Edit Job Titles")
    public void editJobTitle(String jobNameBeforeEdit, String jobName, String jobDescription, String filePath, String note){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+jobNameBeforeEdit+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page,"Job Title").fill(jobName);
        inputJobDescription.fill(jobDescription);
        FileChooser fileChooser = page.waitForFileChooser(() -> btnBrowse.click());
        fileChooser.setFiles(Paths.get(filePath));
        inputNote.fill(note);
        ElementUtils.btnSave(page).click();
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }


    //----Pay Grades------
    public void addPayGrade(String payName){
        ElementUtils.btnAdd(page).click();
        ElementUtils.inputText(page, "Name").fill(payName);
        ElementUtils.btnSave(page).click();;
    }

    public boolean verifyAddPayGradeSuccess(String colName, String payName){
        CommonAction.waitElementAfterAdd(page);
        List<String> payGradeList = CommonAction.getValueOfColumnName(page, colName);
        System.out.println("List of pay grade: ");
        for(String job : payGradeList){
            System.out.println(job);
        }
        return payGradeList.contains(payName);
    }

    public void editPayGrade(String oldPayName, String newPayName){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+oldPayName+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page,"Job Title").fill(newPayName);
        ElementUtils.btnSave(page).click();
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }


    //----Employment Status---
    public void addEmploymentStatus(String statusName){
        ElementUtils.btnAdd(page).click();
        ElementUtils.inputText(page, "Name").fill(statusName);
        ElementUtils.btnSave(page).click();
    }

    public boolean verifyAddEmploymentStatusSuccess(String colName, String payName){
        CommonAction.waitElementAfterAdd(page);
        List<String> statusList = CommonAction.getValueOfColumnName(page, colName);
        System.out.println("List of employment status: ");
        for(String job : statusList){
            System.out.println(job);
        }
        return statusList.contains(payName);
    }

    public void editEmploymentStatus(String oldStatusName, String newStatusName){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+oldStatusName+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page,"Job Title").fill(newStatusName);
        ElementUtils.btnSave(page).click();
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }
    //---Job Categories---
    public void addJobCategories(String categoryName){
        ElementUtils.btnAdd(page).click();
        ElementUtils.inputText(page, "Name").fill(categoryName);
        ElementUtils.btnSave(page).click();
    }

    public boolean verifyAddJobCategoriesSuccess(String colName, String payName){
        CommonAction.waitElementAfterAdd(page);
        List<String> statusList = CommonAction.getValueOfColumnName(page, colName);
        System.out.println("List of job category: ");
        for(String job : statusList){
            System.out.println(job);
        }
        return statusList.contains(payName);
    }

    public void editJobCategories(String oldCategoryName, String newCategoryName){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+oldCategoryName+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page,"Job Title").fill(newCategoryName);
        ElementUtils.btnSave(page).click();
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    //---Work Shifts---
    public void addWorkShifts(String shiftName, String fromTime, String toTime ,String assignedEmployee){
        ElementUtils.btnAdd(page).click();
        ElementUtils.inputText(page, "Shift Name").fill(shiftName);
        ElementUtils.inputText(page, "From").click();
        setInputTime(fromTime,textFromHour,btnUpFromHour, btnDownFromHour);
        ElementUtils.inputText(page, "To").click();
        setInputTime(toTime, textToHour, btnUpToHour, btnDownToHour);
        ElementUtils.inputText(page, "Assigned Employees").fill(assignedEmployee);
        getAssignedEmployee(assignedEmployee);
        ElementUtils.btnSave(page).click();
    }

    public void getAssignedEmployee(String employee){
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(employee)).click();
    }


    private void setInputTime(String time, Locator textTime, Locator btnUp, Locator btnDown) {
        String[] parts = time.split(":");
        int targetHour = Integer.parseInt(parts[0]);
        while (true) {
            int currentHour = Integer.parseInt(textTime.inputValue().trim());

            if (currentHour == targetHour) {
                break;
            } else if (currentHour < targetHour) {
                btnUp.click();
            } else {
                btnDown.click();
            }
        }
    }


    public boolean verifyAddWorkShifts(String colName, String payName){
        CommonAction.waitElementAfterAdd(page);
        List<String> statusList = CommonAction.getValueOfColumnName(page, colName);
        System.out.println("List of work shift: ");
        for(String job : statusList){
            System.out.println(job);
        }
        return statusList.contains(payName);
    }

    public void editWorkShifts(String oldWorkShift, String newWorkShift, String fromTime, String toTime ,String assignedEmployee){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+oldWorkShift+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page, "Shift Name").fill(newWorkShift);
        ElementUtils.inputText(page, "From").click();
        setInputTime(fromTime,textFromHour,btnUpFromHour, btnDownFromHour);
        ElementUtils.inputText(page, "To").click();
        setInputTime(toTime, textToHour, btnUpToHour, btnDownToHour);
        ElementUtils.inputText(page, "Assigned Employees").fill(assignedEmployee);
        getAssignedEmployee(assignedEmployee);
        ElementUtils.btnSave(page).click();
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

}
