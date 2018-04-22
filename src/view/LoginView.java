package view;

import assets.HelperFunctions;

import java.io.IOException;

/**
 * @author dimz
 * @since 21/4/18.
 */
public class LoginView extends JMossView{

    public LoginView() throws IOException {
        setMyContent(HelperFunctions.getConfigReader().getConfigString("LOGIN_VEW_CONTENT"));
    }
}
