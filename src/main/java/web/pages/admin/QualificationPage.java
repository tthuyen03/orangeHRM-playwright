package web.pages.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import utils.CommonAction;
import utils.ElementUtils;
import web.base.BasePage;

import javax.swing.text.Element;
import java.util.List;

public class QualificationPage extends BasePage {
    public QualificationPage(Page page){
        super(page);
    }
    private final Locator description = page.getByPlaceholder("Type description here");


    //Skills
    @Step("Add new skill")
    public void addSkill(String skillName, String skillDesc){
        ElementUtils.btnAdd(page);
        ElementUtils.inputText(page,"Name").fill(skillName);
        description.fill(skillDesc);
        ElementUtils.btnSave(page);
    }


    @Step("Edit new skill")
    public void editSkill(String oldSkillName, String newSkillName, String skillDesc){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+oldSkillName+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page,"Name").fill(newSkillName);
        description.fill(skillDesc);
        ElementUtils.btnSave(page);
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    // Education
    @Step("Add Education")
    public void addEducation(String levelName){
        CommonAction.addWithName(page,levelName);
    }



    @Step("Edit education")
    public void editEducation(String oldEducation, String newEducation){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+oldEducation+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page,"Name").fill(newEducation);
        ElementUtils.btnSave(page);
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    //Licenses
    @Step("Add license")
    public void addLicense(String licenseName){
        CommonAction.addWithName(page,licenseName);
    }

    @Step("Edit license")
    public void editLicense(String oldLicense, String newLicense){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+oldLicense+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page,"Name").fill(newLicense);
        ElementUtils.btnSave(page);
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    //Languages
    @Step("Add language")
    public void addLanguage(String languageName){
        CommonAction.addWithName(page,languageName);
    }

    @Step("Edit language")
    public void editLanguage(String oldLanguage, String newLanguage){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+oldLanguage+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page,"Name").fill(newLanguage);
        ElementUtils.btnSave(page);
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    //Memberships
    @Step("Add membership")
    public void addMembership(String membership){
        CommonAction.addWithName(page,membership);
    }

    @Step("Edit membership")
    public void editMembership(String oldMembership, String newMembership){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+oldMembership+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page,"Name").fill(newMembership);
        ElementUtils.btnSave(page);
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

}
