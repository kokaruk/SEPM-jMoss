package controller;

import model.User;
import view.BookingClerkMainMenu;
import view.JMossView;

/**
 * @author dimz
 * @since 22/4/18.
 */
class JMossBookingClerkController implements IController {

    private User user;
    private JMossView currentMenu;

    JMossBookingClerkController(User user){
        this.user = user;
    }


    @Override
    public void start() {
        currentMenu = new BookingClerkMainMenu();
        currentMenu.displayContent();
    }
}
