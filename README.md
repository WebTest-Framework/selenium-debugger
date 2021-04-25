# webtest-call

Have you ever thought of developing and debugging your selenium code?
If yes, then all you need is the latest release of this repository.
You can debug your code using CLI operations of this jar.

## But why I need this... ?
Traditional way of writing selenium codes:
- Contains maximum locator errors
- Visibility of element checks during runtime
- Application is behaving differently during script execution

Well, all these problems can be solved if I parallely test 
locators along with automation scripting. Once you run your test, no errors
can stop you from ZERO script related errors.

## Tell me how to use this ?
This is pretty simple for you. Just download the latest <b>webtest-call.jar</b> file

*** Make sure you are using selenium standalone server with chromedriver
installed and the server is up and running on localhost:4444

1. Run the selenium-standalone server on localhost:4444
2. Start the jar using below command:
`java -jar webtest-call.jar`
   
3. If it detects the server, you will see the console same as below:

```
 > 
```
4. You can use the commands mentioned below for performing browser actions:
<table style="width:100%">
    <thead>
        <tr>
            <td style="width:25%">Command</td>
            <td style="width:10%">Description</td>
            <td style="width:75%">Example</td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td style="width:25%">launch</td>
            <td style="width:10%">Launches new browser session. Current session is stored in running instance.</td>
            <td style="width:75%">launch chrome</td>
        </tr>
        <tr>
            <td style="width:25%">navigate</td>
            <td style="width:10%">Navigates to a URI.</td>
            <td style="width:75%">navigate https://www.google.com</td>
        </tr>
        <tr>
            <td style="width:25%">find</td>
            <td style="width:10%">Finds an element in screen using xpath/CSS selector. Stores current element object in running instance</td>
            <td style="width:75%">find [title='Search']<br>find //*[@title='Search']</td>
        </tr>
        <tr>
            <td style="width:25%">listsessions</td>
            <td style="width:10%">Lists all the sessions with browser names</td>
            <td style="width:75%">listsessions</td>
        </tr>
        <tr>
            <td style="width:25%">connect</td>
            <td style="width:10%">Connects the jar instance to a already running selenium browser instance.<br>Note: You can list all the browser
            sessions using <b>listsessions</b> command</td>
            <td style="width:75%">connect <b>BROWSER_SESSION_ID</b></td>
        </tr>
        <tr>
            <td style="width:25%">close</td>
            <td style="width:10%">Closes the browser connected to current jar instance</td>
            <td style="width:75%">close</td>
        </tr>
        <tr>
            <td style="width:25%">click</td>
            <td style="width:10%">Clicks on an element</td>
            <td style="width:75%">click //*[@title='Search']</td>
        </tr>
        <tr>
            <td style="width:25%">setvalue</td>
            <td style="width:10%">Enters value to an input field</td>
            <td style="width:75%">Implementation way 1(Might not work several times due to spaces):<br>
                setvalue //*[@title='Search'] textstring=webtest<br>
                Implementation way 2(Work always...):<br>
                - find //*[@title='Search']<br>
                - setvalue webtest
            </td>
        </tr>
        <tr>
            <td style="width:25%">select</td>
            <td style="width:10%">Selects dropdown value</td>
            <td style="width:75%">Implementation way 1(Might not work several times due to spaces):<br>
                select //*[@title='Search'] selectstring=webtest<br>
                Implementation way 2(Work always...):<br>
                - find //*[@title='Search']<br>
                - select webtest
            </td>
        </tr>
        <tr>
            <td style="width:25%">exit</td>
            <td style="width:10%">Exit from jar instance</td>
            <td style="width:75%">exit</td>
        </tr>
    </tbody>
</table>

The best is about to come. Stay tuned !!