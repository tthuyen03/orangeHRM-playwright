
package web.base;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;

public class BaseAPI {

    private final APIRequestContext apiRequestContext;

    public BaseAPI(APIRequestContext apiRequestContext){
        this.apiRequestContext = apiRequestContext;
    }

    //get
    public APIResponse sendGET(String endpoint) {
        return apiRequestContext.get(endpoint);
    }

    //post
    public APIResponse sendPOST(String endpoint, String jsonBody){
        return apiRequestContext.post(endpoint, RequestOptions.create().setData(jsonBody));
    }

    //put
    public APIResponse sendPUT(String endpoint, String jsonBody){
        return apiRequestContext.put(endpoint, RequestOptions.create().setData(jsonBody));
    }

    //delete
    public APIResponse sendDELETE(String endpoint){
        return apiRequestContext.delete(endpoint);
    }


}
