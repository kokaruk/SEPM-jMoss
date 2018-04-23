package controller;

import dal.CinemaRepo;
import model.Cinema;
import model.User;
import view.BookingClerkMainMenu;
import view.JMossView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author dimz
 * @since 22/4/18.
 */
class JMossBookingClerkController implements IController {
    private User user;
    private JMossView currentMenu;
    private List<Cinema> cinemas = new ArrayList<>();

    JMossBookingClerkController(User user){
        this.user = user;
        currentMenu =  new BookingClerkMainMenu(this.user.getUserName());
        cinemas.addAll(CinemaRepo.getInstance().getAllCinemas());
    }

    @Override
    public void start() {
        currentMenu.displayContent();
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
    }

}
