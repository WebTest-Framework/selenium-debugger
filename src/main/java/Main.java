import java.io.Console;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;

public class Main {
    public static void main(String[] args) throws IOException {
        if(hostAvailabilityCheck()) {
            Api api = new Api();
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.print("> ");
                String command = sc.nextLine();
                String driverCommand = command.toLowerCase().split(" ")[0];
                String[] option = command.split(" ");
                switch (driverCommand) {
                    case "launch": {
                        api.launchBrowser(option[1]);
                        break;
                    }
                    case "navigate": {
                        api.navigate(option[1]);
                        break;
                    }
                    case "find": {
                        api.findElement(command.replace(option[0], ""));
                        break;
                    }
                    case "listsessions": {
                        api.printSessions();
                        break;
                    }
                    case "connect": {
                        api.attachSession(option[1]);
                        break;
                    }
                    case "close": {
                        api.closeBrowser();
                        break;
                    }
                    case "click": {
                        api.click(command.replace(option[0], ""));
                        api.curElement = null;
                        break;
                    }
                    case "setvalue": {
                        if (api.curElement != null) {
                            api.setValue(command.replace(option[0], "").trim());
                            api.curElement = null;
                        } else {
                            String locatorString = "";
                            String valueToEnter = "";
                            for (int i = 1; i < option.length; i++) {
                                if (option[i].contains("textstring=")) {
                                    valueToEnter = option[i].split("textstring=")[1];
                                } else {
                                    locatorString = locatorString.concat(option[i] + " ");
                                }
                            }
                            locatorString = locatorString.substring(0, locatorString.length() - 1);
                            api.setValue(locatorString, valueToEnter);
                        }
                        break;
                    }
                    case "select": {
                        if (api.curElement != null) {
                            api.select(command.replace(option[0], "").trim());
                            api.curElement = null;
                        } else {
                            String locatorString = "";
                            String valueToEnter = "";
                            for (int i = 1; i < option.length; i++) {
                                if (option[i].contains("selectstring=")) {
                                    valueToEnter = option[i].split("selectstring=")[1];
                                } else {
                                    locatorString = locatorString.concat(option[i] + " ");
                                }
                            }
                            locatorString = locatorString.substring(0, locatorString.length() - 1);
                            api.select(locatorString, valueToEnter);
                        }
                        break;
                    }
                    case "exit": {
                        System.exit(0);
                    }
                    default: {
                        System.out.println("Enter 'exit' to exit.");
                        break;
                    }
                }
            }
        } else {
            System.out.println("ERROR: Selenium standalone server is not running on port 4444");
            System.out.println("To run this utility version, it is necessary to run your server on localhost:4444");
        }
    }

    public static boolean hostAvailabilityCheck() {
        try (Socket s = new Socket("localhost", 4444)) {
            return true;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;
    }
}