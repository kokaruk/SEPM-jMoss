import controller.IController;
import controller.JMossLoginController;
import view.JMossView;
import view.LoginView;

import java.io.IOException;

public class jMoss {
    public static void main(String[] args) throws IOException {
        JMossView loginView = new LoginView();
        IController controller = new JMossLoginController(loginView);
        controller.start();
    }
}