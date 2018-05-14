package view;

import assets.HelperFunctions;

import java.io.Console;
import java.io.IOException;
import java.util.Scanner;

import static view.ViewHelper.ANSI_RED;
import static view.ViewHelper.ANSI_RESET;

/**
 * @author dimz
 * @since 21/4/18.
 */
public class LoginView extends JMossView{

    @Override
    public String getInput() {
        initInput();
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

    @Override
    public void wrongInput() {
        System.err.println(String.format("%sCan't find username/password combination. Please try again%s", ANSI_RED, ANSI_RESET));
    }

    @Override
    void initInput() {
        ViewHelper.clearScreen();
        if (isError()) {
            wrongInput();
            displayContent();
            setError(false);
        } else {
            displayContent();
        }
    }

    @Override
    void buildMyContent() {
        System.out.print(ANSI_RESET);
        try {
            myContent = HelperFunctions.getConfigReader().getConfigString("LOGIN_VEW_CONTENT");
        } catch (IOException e) {
            System.exit(-1);
        }

    }
}
