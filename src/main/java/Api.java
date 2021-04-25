import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class Api extends ApiBase {
    Api() { }

    private Api api;
    private HashMap<String, String> sessionMap = new HashMap<>();

    /**
     * Launches browser based on provided configurations. Default is chrome.
     * @param browserName chrome|firefox|htmlunit|internet explorer|iphone
     * @throws IOException
     */
    public void launchBrowser(String browserName) throws IOException {
        if(browserName.equals("chrome")) {
            api = new Api();
            String params = "{\n" +
                    "  \"capabilities\": {\n" +
                    "    \"alwaysMatch\": { \"browserName\": \"" + browserName + "\"},\n" +
                    "    \"firstMatch\": [ {} ]\n" +
                    "  },\n" +
                    "  \"desiredCapabilities\": { \"browserName\": \"" + browserName + "\"}\n" +
                    "}";
            HttpResponse httpResponse = api.postRequest("", params);
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
            sessionId = jsonObject.getJSONObject("value").getString("sessionId");
            System.out.println("Launched browser with session id : " + sessionId);
        } else {
            System.out.println("LaunchBrowser:Browser name didn't matched with the specifications");
            System.out.println("LaunchBrowser:Failed to launch browser");
        }
    }
    /**
     * Navigate to specified url
     * @param url
     * @throws IOException
     */
    public void navigate(String url) {
        try {
            if (sessionId.equals(null)) {
                System.out.println("Navigate: Invalid session id. Launch browser.");
            } else {
                String params = "{ \n" +
                        "\t\"url\": \"" + url + "\"\n" +
                        "}";
                HttpResponse httpResponse = api.postRequest(sessionId + "/url", params);
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
            }
        } catch(Exception e) {
            System.out.println("Failed to navigate to url");
        }
    }
    /**
     * Find elements using xpath/ css selector
     * @param locatorString xpath/ css selector
     * @throws IOException
     */
    public void findElement(String locatorString) {
        try {
            String params = "";
            locatorString = locatorString.trim();
            locatorString = locatorString.replace("\"", "'");
            if (isXpath(locatorString)) {
                System.out.println("Finding element using xpath");
                if (sessionId.equals(null)) {
                    System.out.println("Navigate: Invalid session id. Launch browser.");
                } else {
                    params = "{ \n" +
                            "\t\"using\": \"xpath\", \n" +
                            "\t\"value\": \"" + locatorString + "\"\n" +
                            "}";
                }
            } else if (locatorString.substring(0, 1).contains("[")) {
                System.out.println("Finding element using CSS locator");
                if (sessionId.equals(null)) {
                    System.out.println("Navigate: Invalid session id. Launch browser.");
                } else {
                    params = "{ \n" +
                            "\t\"using\": \"css selector\", \n" +
                            "\t\"value\": \"" + locatorString + "\"\n" +
                            "}";
                    HttpResponse httpResponse = api.postRequest(sessionId + "/elements", params);
                    JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                }
            }
            HttpResponse httpResponse = api.postRequest(sessionId + "/elements", params);
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
            JSONArray valueObj = jsonObject.getJSONArray("value");
            if (valueObj.length() == 0) {
                System.out.println("No element found with locator : " + locatorString);
                curElement = null;
            } else if (valueObj.length() > 1) {
                System.out.println(valueObj.length() + " elements found with locator : " + locatorString);
                curElement = null;
            } else {
                curElement = valueObj.get(0).toString().split(":")[1].split("\"")[1];
                System.out.println("1 element found with locator : " + locatorString);
            }
            curElemXpath = locatorString;
        } catch (Exception e) {
            System.out.println("No such element");
        }
    }
    /**
     * For fetching all available sessions
     * @throws IOException
     */
    private void getAllSessions() throws IOException {
        String endPoint = baseUrl.concat("s");
        HttpResponse httpResponse = getRequest(endPoint);
        JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
        JSONArray valueObj = jsonObject.getJSONArray("value");
        for (Object eachValue:valueObj) {
            JSONObject eachValjsonObject = (JSONObject)eachValue;
            String sessionId = eachValjsonObject.getString("id");
            String browserName = eachValjsonObject.getJSONObject("capabilities").getString("browserName");
            sessionMap.put(sessionId, browserName);
        }
    }
    /**
     * Prints all available sessions in console
     * @throws IOException
     */
    public void printSessions() throws IOException {
        getAllSessions();
        for (String eachSessionIds:sessionMap.keySet()) {
            System.out.println(sessionMap.get(eachSessionIds) + " ----> " + eachSessionIds);
        }
    }
    /**
     * Connects instance to already existing session
     * @param incSessionId
     */
    public void attachSession(String incSessionId) throws IOException {
        Boolean flag = false;
        api = new Api();
        sessionId = incSessionId;
        getAllSessions();
        System.out.println("Checking available session ids...");
        for (String eachSessionIds:sessionMap.keySet()) {
            if(eachSessionIds.equals(incSessionId)) {
                System.out.println("Session id found !");
                flag = true;
                break;
            }
        }
        if(!flag) {
            System.out.println("Invalid session id");
        }
        System.out.println("Connection successful with browser : " + sessionMap.get(sessionId));
    }
    /**
     * Closes browser or kills the browser session
     */
    public void closeBrowser() {
        HttpResponse httpResponse = api.deleteResponse(sessionId);
        sessionMap.remove(sessionId);
        curElemXpath = curElement = sessionId = null;
        System.out.println("Closed browser");
    }
    /**
     * Clicks on element
     * @param locatorString xpath/ CSS locator
     * @throws IOException
     */
    public void click(String locatorString) {
        findElement(locatorString);
        if(curElement == null) {
            System.out.println("No element found with specified xpath");
        } else {
            String actionCall = sessionId + "/element/" + curElement + "/click";
            String params = "{\n" +
                    "\t\"LEFT\": 0\n" +
                    "}";
            HttpResponse httpResponse = api.postRequest(actionCall, params);
        }
    }
    /**
     * Enter text in an edit field
     * @param locatorString xpath/ CSS selector
     * @param value value to enter
     * @throws IOException
     */
    public void setValue(String locatorString, String value) {
        findElement(locatorString);
        if(curElement == null) {
            System.out.println("No element found with specified xpath");
        } else {
            String actionCall = sessionId + "/element/" + curElement + "/value";
            String params = "{\n" +
                    "\t\"text\": \"" + value + "\"\n" +
                    "}";
            api.postRequest(actionCall, params);
            System.out.println("Entered value : " + value);
        }
    }
    /**
     * Enter text in an edit field
     * @param value value to enter
     * @throws IOException
     */
    public void setValue(String value) {
        String actionCall = sessionId + "/element/" + curElement + "/value";
        String params = "{\n" +
                    "\t\"text\": \"" + value + "\"\n" +
                "}";
        api.postRequest(actionCall, params);
        System.out.println("Entered value : " + value);
    }
    /**
     * Selecting drop down
     * @param locatorString XPath/ CSS selector
     * @param value value to select
     */
    public void select(String locatorString, String value) {
        if (locatorString.substring(0, 2).contains("//")) {
            locatorString = locatorString.concat("/option[text()=\"" + value + "\"]");
        } else if (locatorString.substring(0, 1).contains("[")) {
            locatorString = CSSToXpath(locatorString);
            locatorString = locatorString.concat("/option[text()=\"" + value + "\"]");
        }
        click(locatorString);
        System.out.println("Selected drop down with value : " + value);
    }
    /**
     * Selecting drop down
     * @param value Value to select
     */
    public void select(String value) {
        if (isXpath(curElemXpath)) {
            curElemXpath = curElemXpath.concat("/option[text()=\"" + value + "\"]");
        } else if (curElemXpath.substring(0, 1).contains("[")) {
            curElemXpath = CSSToXpath(curElemXpath);
            curElemXpath = curElemXpath.concat("/option[text()=\"" + value + "\"]");
        }
        click(curElemXpath);
        System.out.println("Selected drop down with value : " + value);
    }
}