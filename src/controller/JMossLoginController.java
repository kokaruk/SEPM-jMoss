package controller;

import assets.HelperFunctions;
import dal.IUserRepoDAL;
import dal.UserRepo;
import model.User;
import view.JMossView;
import view.ViewHelper;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.Arrays;

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
        login();
        exit();
    }

    private void login(){
        if (MAX_LOGIN_ATTEMPTS > currentLoginAttempt){
            currentLoginAttempt++;
            String[] usernamePassword = Arrays.copyOf(myView.getInput().split("-"), 2);
            String username = usernamePassword[0];
            String password = usernamePassword[1];

            IUserRepoDAL userRepo = UserRepo.getInstance();
            User user;
            try {
                user = userRepo.getUser(username.toLowerCase(), password);
                if (user != null) {
                    // make user controller based on user class instance name
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

    private void exit() {
        System.out.println("Bye!");
        Toolkit.getDefaultToolkit().beep();
        System.exit(0);
    }



}
