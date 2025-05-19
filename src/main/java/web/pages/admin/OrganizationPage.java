package web.pages.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import utils.ElementUtils;
import web.base.BasePage;

import javax.swing.text.Element;

public class OrganizationPage extends BasePage {

    //Location
    private final Locator selectCountry = page.locator("div.oxd-select-wrapper:below(label:has-text('Country'))").first();
    private final Locator inputAddress = page.locator("//label[text()='Address']//following::textarea").first();
    private final Locator inputNotes = page.locator("//label[text()='Notes']//following::textarea");


    public OrganizationPage(Page page){
        super(page);
    }

    //General Information


    //Location
    @Step("Search by name")
    public void searchByName(String name){
        ElementUtils.inputText(page, "Name").fill(name);
        ElementUtils.btnSearch(page);
    }

    @Step("Search by city")
    public void searchByCity(String city){
        ElementUtils.inputText(page, "Name").fill(city);
        ElementUtils.btnSearch(page);
    }

    @Step("Search by country")
    public void searchByCountry(String country){
        selectCountry.click();
        getValueInList(country);
        ElementUtils.btnSearch(page).click();
    }
    public void getValueInList(String text){
        Locator locator = ElementUtils.droplist(page).locator("//span[text()='" + text + "']");
        locator.waitFor();
        locator.click();
    }

    @Step("Add new location")
    public void addLocation(String name, String city, String state, String zip, String country, String phone,
                            String fax, String address, String note){
        ElementUtils.inputText(page, "Name").fill(name);
        ElementUtils.inputText(page, "City").fill(city);
        ElementUtils.inputText(page, "State/Province").fill(state);
        ElementUtils.inputText(page,"Zip/Postal Code").fill(zip);
        ElementUtils.inputText(page,"Country").click();
        getValueInList(country);
        ElementUtils.inputText(page,"Phone").fill(phone);
        ElementUtils.inputText(page,"Fax").fill(fax);
        inputAddress.fill(address);
        inputNotes.fill(note);
        ElementUtils.btnSave(page);
    }


    @Step("Edit location")
    public void editLocation(String oldLocationName, String newLocationName, String city, String state, String zip, String country, String phone,
                            String fax, String address, String note){
        Locator btnEdit = ElementUtils.tableRow(page).locator("//div[text()='"+oldLocationName+"']//ancestor::div[@role='row']//i[contains(@class,'bi-pencil-fill')]");
        btnEdit.click();
        ElementUtils.inputText(page, "Name").fill(newLocationName);
        ElementUtils.inputText(page, "City").fill(city);
        ElementUtils.inputText(page, "State/Province").fill(state);
        ElementUtils.inputText(page,"Zip/Postal Code").fill(zip);
        ElementUtils.inputText(page,"Country").click();
        getValueInList(country);
        ElementUtils.inputText(page,"Phone").fill(phone);
        ElementUtils.inputText(page,"Fax").fill(fax);
        inputAddress.fill(address);
        inputNotes.fill(note);
        ElementUtils.btnSave(page);
        Locator toast = ElementUtils.toastUpdate(page);
        toast.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }


    //Structure
}
