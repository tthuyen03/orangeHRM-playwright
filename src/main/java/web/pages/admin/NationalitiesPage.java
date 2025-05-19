package web.pages.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import utils.CommonAction;
import utils.ElementUtils;
import web.base.BasePage;

import java.util.List;

public class NationalitiesPage extends BasePage {

    public NationalitiesPage(Page page){
        super(page);
    }

    @Step("Add nationality")
    public void addNationality(String nationality){
        CommonAction.addWithName(page,nationality);
    }

    @Step("Edit nationality")
    public void editNationality(String oldNationality, String newNationality){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+oldNationality+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page,"Job Title").fill(newNationality);
        ElementUtils.btnSave(page).click();
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

}
