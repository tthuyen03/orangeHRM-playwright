package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class ElementUtils {
    public static Locator getLinkInSideNav(Page page, String tabName){
        return page.locator("a.oxd-main-menu-item:has-text('" + tabName + "')");
    }

    public static Locator table(Page page){
        return page.locator("//div[@role='table']");
    }

    public static Locator tableRow(Page page){
        return page.locator("//div[@role='row']");
    }


    public static Locator popUpDelete(Page page){
        return page.locator("//div[@role='document']");
    }

    public static Locator getLinkInTopNav(Page page, String tabName){
        return page.locator("//nav[@aria-label='Topbar Menu']//span[normalize-space(text()) = '"+tabName+"']");
    }

    public static Locator getOptionInTopNav(Page page, String optionName){
        return page.locator("//nav[@aria-label='Topbar Menu']//a[text()='"+optionName+"']");
    }

    public static Locator headerTitle(Page page, String headerName){
        return page.locator("//h6[text()='"+headerName+"']");
    }

    public static Locator inputText(Page page, String text){
        return page.locator("//label[text()='"+text+"']/ancestor::div[contains(@class,'oxd-input-group')]/descendant::input");
    }


    public static Locator btnSearch(Page page){
        return page.locator("button:has-text(' Search ')");
    }


    public static Locator btnOnPopUp(Page page, String textOnButton){
        return page.getByText(textOnButton);
    }

    //Get checkbox in header table
    public static Locator checkboxOnHeaderTable(Page page){
        return table(page).locator("//div[@role='columnheader']//input");
    }

    public static Locator droplist(Page page){
        return page.locator("//div[@role='listbox']");
    }

    //Get checkbox in row table
    public static Locator checkboxInRowTable(Page page, String jobTitle){
        return tableRow(page).locator("//div[text()='"+jobTitle+"']//ancestor::div[contains(@class,'oxd-table-row')]//label");
    }

    //button add
    public static Locator btnAdd(Page page){
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(" Add "));
    }

    //button save
    public static Locator btnSave(Page page){
        return page.locator("button:has-text('Save')");
    }

    // toast save successfully
    public static Locator toastSaved(Page page){
        return page.locator(".oxd-toast-content:has-text('Successfully Saved')");

    }

    // toast update successfully
    public static Locator toastUpdate(Page page){
        return page.locator(".oxd-toast-content:has-text('Successfully Updated')");

    }

    // toast delete successfully
    public static Locator toastMessage(Page page, String message){
        return page.locator(".oxd-toast-content:has-text('"+message+"')");

    }

    // toast cannot delete
    public static Locator toastCannotDelete(Page page){
        return page.locator(".oxd-toast-content:has-text('Cannot be deleted')");

    }

    //
    public static Locator btnDeleteSelected(Page page){
        return page.getByText(" Delete Selected ");
    }


     public static Locator noRecordFound(Page page){
        return page.getByText("No Records Found");
    }



}
