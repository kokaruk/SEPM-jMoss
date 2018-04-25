package controller;

import dal.CinemaRepo;
import dal.MovieRepo;
import dal.SessionRepo;
import model.Cinema;
import model.Movie;
import model.Session;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.BookingClerkMainMenu;
import view.JMossView;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dimz
 * @since 22/4/18.
 */

public class JMossBookingClerkController implements IController {
    private static Logger logger = LogManager.getLogger();

    private User user;
    private JMossView currentMenu;
    private Map<Integer, Cinema> cinemas = new HashMap<>();
    private Map<Integer, Movie> movies = new HashMap<>();
    private Map<Integer, Session> sessions = new HashMap<>();

    @SuppressWarnings("unused")
    JMossBookingClerkController(User user){
        this.user = user;
        currentMenu =  new BookingClerkMainMenu(this.user.getUserName());
        cinemas.putAll(CinemaRepo.getInstance().getAllCinemas());
        movies.putAll(MovieRepo.getInstance().getAllMovies());
        sessions.putAll(SessionRepo.getInstance().getAllSessions());
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
        System.out.println();
        activateMenu();
    }

    private void activateMenu(){
        String inputString = currentMenu.getInput();
        switch(inputString) {
            case "exit": break;
            case "unknown":
                activateMenu();
                break;
            case "BookingClerkMainMenu":try {
                // make view based on returned string value
                String viewClassName = "view." + inputString;
                Class<?> viewClass = Class.forName(viewClassName);
                Constructor<?> viewClassConstructor = viewClass.getDeclaredConstructor(String.class);
                currentMenu = (JMossView) viewClassConstructor.newInstance(this.user.getUserName());
                activateMenu();
            } catch (Exception e) {
                // something went wrong
                activateMenu();
            }
                break;
            default: try {
                // make view based on returned string value
                String viewClassName = "view." + inputString;
                Class<?> viewClass = Class.forName(viewClassName);
                Constructor<?> viewClassConstructor = viewClass.getDeclaredConstructor(IController.class);
                JMossView newView = (JMossView) viewClassConstructor.newInstance(this);
                currentMenu = newView;
                activateMenu();
            } catch (Exception e) {
                // something went wrong
                activateMenu();
            }
        }



    }

}
