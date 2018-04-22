package controller;

import assets.HelperFunctions;
import dal.IUserRepoDAL;
import dal.UserRepo;
import model.BookingClerk;
import model.User;
import view.JMossView;

import java.io.Console;
import java.util.Scanner;

/**
 * @author dimz
 * @since 21/4/18.
 */
public class JMOssLoginController implements IController {

    private final int MAX_LOGIN_ATTEMPTS;
    private int currentLoginAtempt;
    private JMossView myView;


    public JMOssLoginController(JMossView myView)  {
        this.MAX_LOGIN_ATTEMPTS = HelperFunctions.getConfigReader().getConfigInt("MAX_LOGIN_ATTEMPTS");
        currentLoginAtempt = 0;
        this.myView = myView;
    }

    @Override
    public void start() {
        myView.displayContent();
        login();
    }

    void login(){
        if (MAX_LOGIN_ATTEMPTS > currentLoginAtempt){
            currentLoginAtempt++;
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

            initMainController(username, new String(password));

        } else {
            System.out.println("\033[31mExhausted maximum allowed login attempts");
            System.exit(1);
        }
    }

    private void initMainController(String username, String password)  {
        IUserRepoDAL userRepo = UserRepo.getInstance();
        try {
            User user = userRepo.getUser(username, password);
            if (user instanceof BookingClerk) {
                System.out.println("This is a booking clerk logging in");
                System.out.println("user id: " +  user.getUserId());
            }

        } catch (Exception ex){
            System.err.println(ex.getMessage());
        }
    }

}
