package controller;

import dal.BookingPurger;
import dal.DALFactory;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.BookingClerkMainMenu;
import view.JMossView;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    private Map<Integer, Movie> movies = new LinkedHashMap<>(); // all movies
    private Map<Integer, Session> sessions = new HashMap<>(); // all sessions
    private Map<Integer, Booking> bookings = new LinkedHashMap<>(); // all bookings

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
        // probably not the best solution. loads data from order lines, then purges old data
        DALFactory.getBookingRepoDAL()
                .getBookingLines()
                .forEach(row -> {
                    try {
                        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                        Booking booking = bookings.get(Integer.parseInt(row.get(0)));
                        Session session = sessions.get(Integer.parseInt(row.get(1)));
                        booking.addSession(session, Integer.parseInt(row.get(2)), parser.parse(row.get(3)));
                        session.addBooking(booking);
                    } catch (ParseException e){
                        // do nothing we simply ignore the data, no
                        // todo may be try repairing in the future
                    }
                }
        );

    }

    ////// getters ////
    public Map<Integer, Cinema> getCinemas() {
        return cinemas;
    }
    public User getUser() {
        return user;
    }
    public Map<Integer, Movie> getMovies() {
        return movies;
    }
    public Map<Integer, Booking> getBookings() {
        return bookings;
    }

    // setters
    public void setBooking(Integer bookingNumber, Booking booking) {
        bookings.put(bookingNumber, booking);
        // not sure if it the right spot.
        // drunk will fix later... maybe
        DALFactory.getBookingRepoDAL().addNewBooking(bookingNumber, booking);
    }

    @Override
    public void start() {
        logger.debug(user.getUserName());
        activateMenu();
    }

    public void removeBooking(Booking booking){
        for (Map.Entry<Integer, Booking> bookingEntry : bookings.entrySet()){
            if(bookingEntry.getValue() == booking){
                bookings.remove(bookingEntry.getKey());
                BookingPurger purger = new BookingPurger();
                purger.saveData(bookings);
                return;
            }
        }
    }

    public void removeBookingLine(Booking booking, int lineNumber){
        booking.getBookingLines().remove(lineNumber);
        BookingPurger purger = new BookingPurger();
        purger.saveData(bookings);
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
