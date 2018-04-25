package view;

import assets.HelperFunctions;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author dimz
 * @since 21/4/18.
 */
public class LoginView extends JMossView{

    public LoginView() throws IOException {
        setMyContent(HelperFunctions.getConfigReader().getConfigString("LOGIN_VEW_CONTENT"));
    }

    @Override
    public String getInput() {

        String userNamePrompt = "Enter your Username: ";
        String username;
        String passwordPrompt = "Enter yor password: ";
        char[] password;
        Console console = System.console();
        /*
            console only available when running from terminal or dos.
            when compiled in IDE this object is null
         */
        if (console == null) {
            System.err.println("Running test mode from IDE");
            Scanner scanner = new Scanner(System.in);
            System.out.print(userNamePrompt);
            username = scanner.nextLine().trim();
            System.out.print(passwordPrompt);
            password = scanner.nextLine().trim().toCharArray();
        } else {
            username = console.readLine(userNamePrompt).trim();
            password = console.readPassword(passwordPrompt);
        }

        return username + "-" + new String(password);
    }
}
