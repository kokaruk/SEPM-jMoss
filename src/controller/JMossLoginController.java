package controller;

import assets.HelperFunctions;
import dal.IUserRepoDAL;
import dal.UserRepo;
import model.User;
import view.JMossView;
import view.ViewHelper;

import java.awt.*;
import java.io.Console;
import java.lang.reflect.Constructor;
import java.util.Scanner;

/**
 * @author dimz
 * @since 21/4/18.
 */
public class JMossLoginController implements IController {

    private final int MAX_LOGIN_ATTEMPTS;
    private int currentLoginAttempt;
    private JMossView myView;


    public JMossLoginController(JMossView myView)  {
        this.MAX_LOGIN_ATTEMPTS = HelperFunctions.getConfigReader().getConfigInt("MAX_LOGIN_ATTEMPTS");
        currentLoginAttempt = 0;
        this.myView = myView;
    }

    @Override
    public void start() {
        myView.displayContent();
        login();
        exit();
    }

    private void login(){
        if (MAX_LOGIN_ATTEMPTS > currentLoginAttempt){
            currentLoginAttempt++;
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

            IUserRepoDAL userRepo = UserRepo.getInstance();
            User user;
            try {
                user = userRepo.getUser(username, new String(password));
                if (user != null) {
                    String userClassName = user.getClass().getSimpleName();
                    String userClassControllerName = "controller.JMoss" + userClassName + "Controller";
                    Class<?> controllerClass = Class.forName(userClassControllerName);
                    Constructor<?> controllerConstructor = controllerClass.getDeclaredConstructor(User.class);
                    IController userController = (IController) controllerConstructor.newInstance(user);
                    userController.start();

                } else {
                    ViewHelper.clearScreen();
                    System.out.println("Can't find username/password combination. Please try again");
                    login();
                }

            } catch (Exception ex){
                System.err.println(ex.getMessage());
            }

        } else {
            System.out.println("\033[31mExhausted maximum allowed login attempts");
            System.exit(1);
        }
    }

    void exit() {
        System.out.println("Bye!");
        Toolkit.getDefaultToolkit().beep();
        System.exit(0);
    }



}
