package controller;

import dal.DALFactory;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.BookingClerkMainMenu;
import view.JMossView;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Primary controller for logged-in Booking Clerk
 * @author dimz
 * @since 22/4/18.
 */

public class JMossBookingClerkController implements IController {
    private static Logger logger = LogManager.getLogger();

    // instance variables
    private User user; // user variable to store current logged in user
    private JMossView currentMenu; // current active menu, all menus are extending JMossView
    private Map<Integer, Cinema> cinemas = new HashMap<>(); // all cinemas
    private Map<Integer, Movie> movies = new HashMap<>(); // all movies
    private Map<Integer, Session> sessions = new HashMap<>(); // all sessions
    private Map<Integer, Booking> bookings = new HashMap<>(); // all bookings

    /**
     *  Constructor.
     *  Reads all data from files and loads into instance variables
      */
    @SuppressWarnings("unused")
    JMossBookingClerkController(User user){
        this.user = user;
        currentMenu =  new BookingClerkMainMenu(this);
        cinemas.putAll(DALFactory.getCinemaRepoDAL().getAllCinemas());
        movies.putAll(DALFactory.getMovieRepoDAL().getAllMovies());
        sessions.putAll(DALFactory.getSessionRepoDAL().getAllSessions());
        bookings.putAll(DALFactory.getBookingRepoDAL().getAllBookings());
        for (Map.Entry<Integer, Session> sessionEntry : sessions.entrySet() ) {
            Session session = sessionEntry.getValue();
            session.setMovie(movies.get(session.getMovieId()));
            session.setCinema(cinemas.get(session.getCinemaId()));
        }
    }

    public Map<Integer, Cinema> getCinemas() {
        return cinemas;
    }

    @Override
    public void start() {
        logger.debug(user.getUserName());
        activateMenu();
    }

    // instance getters
    public Map<Integer, Booking> getBookings() {
        return bookings;
    }

    public User getUser() {
        return user;
    }

    /**
     * interaction point with view.
     * When requested, current active view returns a string corresponding to a user input
     * this method acts accordingly
     * 1. If input is "unknown' it will mark current menu as "error call" and display contents of current menu with error message
     * 2. If input is expected case, make instance of view based on the returned string, update View pointer of this,
     *    and recursively call to self
     * 3. If returned value is "exit" it will stop which subsequently call to "exit" method in login controller.
     */
    private void activateMenu(){
        String inputString = currentMenu.getInput();
        switch(inputString) {
            case "exit": break;
            case "unknown":
                currentMenu.setError(true);
                activateMenu();
                break;
            default: try {
                // make view based on returned string value
                String viewClassName = "view." + inputString;
                Class<?> viewClass = Class.forName(viewClassName);
                Constructor<?> viewClassConstructor = viewClass.getDeclaredConstructor(IController.class);
                currentMenu = (JMossView) viewClassConstructor.newInstance(this);
                activateMenu();
            } catch (Exception e) {
                // something went wrong
                logger.fatal(e.getMessage());
                activateMenu();
            }
        }



    }

}
