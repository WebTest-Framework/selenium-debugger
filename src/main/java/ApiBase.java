import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class ApiBase {
    protected String baseUrl = "http://localhost:4444/wd/hub/session";
    protected String sessionId = null;
    protected String curElement = null;
    protected String curElemXpath = null;

    ApiBase() {
        // Default constructor
    }

    private CloseableHttpResponse httpResponse;

    public HttpResponse postRequest(String actionCall, String params) {
        try {
            HttpPost requests = new HttpPost(baseUrl + "/" + actionCall);
            HttpEntity stringEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
            requests.setEntity(stringEntity);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpResponse = httpClient.execute(requests);
            return httpResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    protected HttpResponse getRequest(String actionCall) throws IOException {
        HttpUriRequest request = new HttpGet(actionCall);
        httpResponse = HttpClientBuilder.create().build().execute( request );
        return httpResponse;
    }

    protected HttpResponse deleteResponse(String actionCall){
        try {
            HttpDelete httpDelete = new HttpDelete(baseUrl + "/" + actionCall);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpResponse = httpclient.execute(httpDelete);
            return httpResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String CSSToXpath(String cssLocator) {
        if(isXpath(cssLocator)) {
            return cssLocator;
        } else {
            String kvpair = cssLocator.split("\\[")[1].split("]")[0];
            return "//select[@" + kvpair + "]";
        }
    }

    protected Boolean isXpath(String locatorString) {
        Boolean flag = false;
        int counter = 0;
        char[] locatorArr = locatorString.toCharArray();
        while(counter < locatorArr.length) {
            if(locatorArr[counter] == '/') {
                if(locatorArr[counter + 1] == '/') {
                    flag = true;
                    break;
                }
            } else {
                counter ++;
            }
        }
        return flag;
    }
}
