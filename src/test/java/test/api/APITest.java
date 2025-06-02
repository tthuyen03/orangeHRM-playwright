package test.api;

import com.microsoft.playwright.APIResponse;
import browser.BrowserManager;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import web.base.BaseAPI;

import java.util.Map;


public class APITest {
    private static BaseAPI api;

    @BeforeClass
    public static void setUp() {
        BrowserManager.initAPI(Map.of("Accept", "application/json"));
        api = new BaseAPI(BrowserManager.getApiRequestContext());
    }

    @AfterClass
    public void tearDown(){
        BrowserManager.closePlaywright();
    }



   @Test
    public void getAllProductList(){
        APIResponse response = api.sendGET("/productsList");
        Assert.assertEquals(response.status(),200,"Status code is not 200");
        System.out.println("Response body: " + response.text());
    }

    /*@Test
    public void searchProduct() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString("top");
        APIResponse response = api.sendPOST("/searchProduct", jsonBody);
        Assert.assertEquals(response.status(), 201);
        System.out.println("Created user:" + response.text());
    }*/

    /*@Test
    public void putUserAccount() throws JsonProcessingException {
        AccountData account = new AccountData(
                ""Nguyen Van A"", ""a@gmail.com"", ""123456"", ""Mr"",
                ""01"", ""01"", ""1990"", ""Nguyen"", ""A"",
                ""ABC Corp"", ""123 Street"", """", ""Vietnam"",
                ""70000"", ""HCMC"", ""HCMC"", ""0909123456""
        );

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(account);
        APIResponse response = api.sendPUT(""/updateAccount"", jsonBody);
        System.out.println(response.ok());
        assertThat(response).isOK();
    }

    @Test
    public void deleteUserAccount(){
        APIResponse response = api.sendDELETE(""/deleteAccount"");
        System.out.println(response.ok());
        assertThat(response).isOK();
    }*/
}
