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



public class UserManagementPage extends BasePage {


    private final Locator btnAdd = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(" Add "));
    private final Locator inputUsername = page.locator("input:below(label:has-text('Username'))").first();
    private final Locator yesDelete = page.locator("//div[@role='document']").locator("button:has-text(' Yes, Delete ')");
    private final Locator selectStatus = page.locator("div.oxd-select-wrapper:below(label:has-text('Status'))").first();
    private final Locator inputEmployeeName = page.getByPlaceholder("Type for hints...");
    private final Locator selectUserRole = page.locator("div.oxd-select-wrapper:below(label:has-text('User Role'))").first();
    private final Locator inputPassword = page.locator("input[type='password']:below(label:has-text('Password'))").first();
    private final Locator inputConfirmPassword = page.locator("input[type='password']:below(label:has-text('Confirm Password'))").first();
    private final Locator btnSubmit = page.locator("button:has-text('Save')");
    private final Locator errorMessage= page.locator("//span[contains(@class,'input-field-error-message')]");

    public UserManagementPage(Page page){
        super(page);

    }

    public void clickAdd(){
        btnAdd.click();
    }



    public void getEmployeeName(String empName){
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(empName)).click();
    }

    public String randomEmployeeName() {
        Random rand = new Random();
        List<String> employeeNames = List.of(
                "Linda Anderson", "John Doe", "Jane Smith", "Mark Johnson", "Emily Davis");
        return employeeNames.get(rand.nextInt(employeeNames.size()));
    }

    public String generateUsername(String employeeName) {
        Random rand = new Random();
        int randomNumber = rand.nextInt(1000);
        return employeeName.replace(" ", "").toLowerCase() + randomNumber;
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



    public boolean isUserCreated(String username) {
        searchUserByUsername(username);
        ElementUtils.table(page).isVisible();
        List<String> userList = CommonAction.getValueOfColumnName(page, "Username");
        return userList.contains(username);
    }

    public void addUser(String employeeName, String username, String role, String status, String password ){
        selectUserRole.click();
        getValueInList(role);
        selectStatus.click();
        getValueInList(status);
        inputEmployeeName.fill(employeeName);
        getEmployeeName(employeeName);
        inputUsername.fill(username);
        inputPassword.fill(password);
        inputConfirmPassword.fill(password);
        btnSubmit.click();
    }


    public boolean isErrorMessageDisplayed(){
        return errorMessage.isVisible();
    }

  /*  public void verifyResult(String expectedResult, String expectedMessage){
        switch(expectedResult.toLowerCase()){
            case "success" ->{
               Locator toast = ElementUtils.toastMessage(page,"Successfully Saved");
               toast.waitFor();
               assertTrue("Success toast not visible", toast.isVisible());
            }
            case ""
        }
    }*/

    public void deleteUserByUsername(String username) {
        Locator btnDelete = ElementUtils.tableRow(page).locator("//div[text()='"+username+"']//ancestor::div[@role='row']//i[contains(@class,'bi-trash')]");
        btnDelete.click();
        yesDelete.click();
    }

    public boolean verifyDeleteUserSuccess(String colName, String username){
        List<String> userList = getValueOfColumnName(page, colName);
        return !userList.contains(username);
    }

    public void searchUserByUsername(String username){
        inputUsername.fill(username);
        ElementUtils.btnSearch(page).click();
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

}
