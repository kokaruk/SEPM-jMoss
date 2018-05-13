package controller;

import assets.HelperFunctions;
import dal.BookingPurger;
import dal.DALFactory;
import dal.IUserRepoDAL;
import model.User;
import view.JMossView;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
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
        BookingPurger purger = new BookingPurger();
        purger.run();
        login();
        exit();
    }

    private void login(){
        if (MAX_LOGIN_ATTEMPTS > currentLoginAttempt){
            currentLoginAttempt++;
            String[] usernamePassword = Arrays.copyOf(myView.getInput().split("-"), 2);
            String username = usernamePassword[0];
            String password = usernamePassword[1];

            IUserRepoDAL userRepo = DALFactory.getUserRepoDAL();
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
                    myView.setError(true);
                    login();
                }

            } catch (Exception ex){
                System.err.println(ex.getMessage());
            }

        } else {
            System.out.println("\033[31mExhausted maximum allowed login attempts\033[0m");
            System.exit(1);
        }
    }

    private void exit() {
        System.out.println("Bye!");
        Toolkit.getDefaultToolkit().beep(); // doesn't beep on a mac

        // run some OS Checking
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isMacOs = osName.startsWith("mac os x");
        if (isMacOs)
        {
            // some crazy voodoo below. Stack overflow rules!!!
            byte[] buf = new byte[ 1 ];
            AudioFormat af = new AudioFormat( (float )64100, 8, 1, true, false );
            try {
                SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
                sdl.open();
                sdl.start();
                for (int i = 0; i < 1000 * (float) 44100 / 1000; i++) {
                    double angle = i / ((float) 44100 / 440) * 2.0 * Math.PI;
                    buf[0] = (byte) (Math.sin(angle) * 100);
                    sdl.write(buf, 0, 1);
                }
                sdl.drain();
                sdl.stop();
            } catch (Exception e) {
                // i don't care really
            }
        }


        System.exit(0);
    }



}
