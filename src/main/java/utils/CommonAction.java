package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class CommonAction {

    public static ArrayList<String> getHeaderofTable(Page page){

        Locator headerLocator = ElementUtils.table(page).locator("//div[@role='columnheader']");
        int count = headerLocator.count();
        ArrayList<String> headers = new ArrayList<>();
        for(int i = 0; i < count; i++){
            String headerName = headerLocator.nth(i).innerText().trim();
            headers.add(headerName);
        }
        return headers;
    }

    public static int getIndexOfColumnName(Page page, String colName){
        ArrayList<String> headers = getHeaderofTable(page);
        for(String header : headers){
            if(header.contains(colName)){
                return headers.indexOf(header) + 1;
            }
        }
        return -1;
    }

    public static ArrayList<String> getValueOfColumnName(Page page, String colName){
        int index = getIndexOfColumnName(page,colName);
        Locator elements = ElementUtils.table(page).locator("//div[@role='columnheader' and text()='"+colName+"']//following::div[@role='row']/div["+index+"]");
        ArrayList<String> valueList = new ArrayList<>();
        for(int i = 0; i< elements.count(); i++){
            String value = elements.nth(i).innerText().trim();
            valueList.add(value);
        }
        return valueList;
    }


    @Step("Navigate to option of Job page")
    public static void navigateToOptionOfTopPage(Page page, String parentOption, String option){
        ElementUtils.getLinkInTopNav(page,parentOption).click();
        ElementUtils.getOptionInTopNav(page, option).click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        boolean isHeaderVisible = ElementUtils.headerTitle(page,option).isVisible();
        assertTrue(isHeaderVisible, "Expected '" + option + "' page to be visible");
    }

    public static void waitElementAfterAdd(Page page){
        Locator toast = ElementUtils.toastSaved(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        System.out.println("Toast is visible now!");

        Locator table = ElementUtils.table(page);
        table.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        System.out.println("Table is visible now!");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @Step("Delete all")
    public static void deleteAll(Page page){
        ElementUtils.checkboxOnHeaderTable(page).click();
        ElementUtils.btnDeleteSelected(page).click();
        ElementUtils.btnOnPopUp(page, " Yes, Delete ").click();
        Locator toast = ElementUtils.toastMessage(page,"");
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        ElementUtils.noRecordFound(page).isVisible();

    }

    @Step("Delete multiple or single")
    public static void deleteSingleOrMultiple(Page page, String colName, List<String> jobList){
        for(String job : jobList){
            Locator checkboxRow = ElementUtils.checkboxInRowTable(page,job);
            checkboxRow.click();
        }
        ElementUtils.btnDeleteSelected(page).click();
        ElementUtils.btnOnPopUp(page, " Yes, Delete ").click();
        Locator toast = ElementUtils.toastMessage(page,"");
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        List<String> getDatAfterDelete = CommonAction.getValueOfColumnName(page, colName);
        for(String deleteJob : jobList){
            assert !getDatAfterDelete.contains(deleteJob) :
                    "Job '" + deleteJob + "' stll EXIST after delete";
        }
    }

    @Step("Add with name")
    public static void addWithName(Page page, String name){
        ElementUtils.btnAdd(page);
        ElementUtils.inputText(page,"Name").fill(name);
        ElementUtils.btnSave(page);
    }

    @Step("Verify add successfully")
    public static boolean verifyAddSuccess(Page page, String colName, String newName){
        Locator toast = ElementUtils.toastSaved(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        CommonAction.waitElementAfterAdd(page);
        List<String> statusList = CommonAction.getValueOfColumnName(page, colName);
        System.out.println("List of work shift: ");
        for(String job : statusList){
            System.out.println(job);
        }
        return statusList.contains(newName);
    }

    @Step("Verify toast is displayed")
    public static boolean isToastDisplayed(Page page, String message){
        Locator toast = ElementUtils.toastMessage(page, message);
        try {
            toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            String toastText = toast.innerText();
            return toastText.contains(message);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Verify search result")
    public static boolean hasSearchResults(Page page) {
        Locator noRecordMessage = page.getByText("No Records Found");
        return !noRecordMessage.isVisible();
    }
}
