package controller;

import dal.CinemaRepo;
import dal.MovieRepo;
import model.Cinema;
import model.Movie;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.BookingClerkMainMenu;
import view.JMossView;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author dimz
 * @since 22/4/18.
 */

class JMossBookingClerkController implements IController {
    private static Logger logger = LogManager.getLogger();

    private User user;
    private JMossView currentMenu;
    private Map<Integer, Cinema> cinemas = new HashMap<>();
    private Map<Integer, Movie> movies = new HashMap<>();

    @SuppressWarnings("unused")
    JMossBookingClerkController(User user){
        this.user = user;
        currentMenu =  new BookingClerkMainMenu(this.user.getUserName());
        cinemas.putAll(CinemaRepo.getInstance().getAllCinemas());
        movies.putAll(MovieRepo.getInstance().getAllMovies());
    }

    @Override
    public void start() {
        logger.debug(user.getUserName());
        currentMenu.displayContent();
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
    }

}
