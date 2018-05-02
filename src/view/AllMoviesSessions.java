package view;

import controller.IController;
import controller.JMossBookingClerkController;
import model.Cinema;
import model.Session;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * all movies view
 * @author dimz
 * @since 25/4/18.
 */
public class AllMoviesSessions extends JMossView {

    private final int MAX_MOVIE_STRING_LENGHT = 14; // length of a movie name string for large table.

    public AllMoviesSessions(IController controller){

        String header =
                "                   ********************************************************************************\n" +
                "                   ********************             Movie Sessions             ********************\n" +
                "                   ********************************************************************************\n";

        int[] hours = {10, 12, 16, 19, 22};
        String daysRow = "    A. Mon              B. Tue              C. Wed              D. Thur             E. Fri              F. Sat              G. Sun\n";
        StringBuilder stringBuilder = new StringBuilder(header);
        for (Map.Entry<Integer,Cinema> cinemaEntry : ((JMossBookingClerkController) controller).getCinemas().entrySet() ) {
            String cinemaNameRow = String.format("\n%s. %s\n", cinemaEntry.getKey(), cinemaEntry.getValue().getCinemaName());
            stringBuilder.append(cinemaNameRow);
            stringBuilder.append(daysRow);
            Set<Session> sessions = cinemaEntry.getValue().getSessions();
            for (Integer hour:hours) {
                Predicate<Session> sessionPredicate = session -> session.getSessionTime() == hour;
                List<Session> hourSessions = sessions
                        .stream()
                        .filter(sessionPredicate)
                        .collect(Collectors.toCollection(ArrayList::new));
                StringBuilder movieRow = new StringBuilder(hour.toString());
                for(Session session : hourSessions){
                    String movieName = session.getMovie().getMovieName();
                    movieName = movieName.length() >= MAX_MOVIE_STRING_LENGHT ?
                            movieName.substring(0, Math.min(movieName.length(),MAX_MOVIE_STRING_LENGHT)) :
                            padRight(movieName, MAX_MOVIE_STRING_LENGHT);
                    movieRow.append(String.format("  %s(%02d)",
                            movieName,
                            session.getCinema().getMAX_SEATS() - session.getBookingList().size()));
                }
                movieRow.append("\n");
                stringBuilder.append(movieRow);
            }
        }
        stringBuilder.append("\nType 1 to return to previous menu");
        setMyContent(stringBuilder.toString());
    }
    // bloody java caches string format! need to have a separate function!!!
    private String padRight(String string, int n){
        return String.format("%1$-" + n + "s", string);
    }

    @Override
    public String getInput() {
        switch (super.getInputInt()) {
            case 1: return "BookingClerkMainMenu";
            default: return "unknown";
        }
    }
}
