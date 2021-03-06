package view;

import controller.IController;
import controller.JMossBookingClerkController;
import model.Cinema;

import java.util.Map;

import static view.ViewHelper.ANSI_RED;
import static view.ViewHelper.ANSI_RESET;

/**
 * all movies view
 * @author dimz
 * @since 25/4/18.
 */
public class AllMoviesSessions extends JMossView {

    public AllMoviesSessions(IController controller) {
        super(controller);
    }

    @Override
    public String getInput() {
        switch (getInputInt()) {
            case 1: return "BookingClerkMainMenu";
            default: return "unknown";
        }
    }

    @Override
    void buildMyContent() {
        System.out.print(ANSI_RESET);
        final int MAX_MOVIE_STRING_LENGTH = 14; // length of a movie name string for large table.

        String header =
                "                   ********************************************************************************\n" +
                        "                   ********************             Movie Sessions             ********************\n" +
                        "                   ********************************************************************************\n";

        String daysRow = "    A. Mon              B. Tue              C. Wed              D. Thur             E. Fri              F. Sat              G. Sun\n";
        StringBuilder stringBuilder = new StringBuilder(header);
        for (Map.Entry<Integer, Cinema> cinemaEntry : ((JMossBookingClerkController) controller).getCinemas().entrySet() ) {
            String cinemaNameRow = String.format("\n%s. %s\n", cinemaEntry.getKey(), cinemaEntry.getValue().getCinemaName());
            stringBuilder.append(cinemaNameRow);
            makeCinemaMoviesSessionsTable(stringBuilder, cinemaEntry.getValue());
        }
        stringBuilder.append(String.format("\nType %s1%s to return to previous menu", ANSI_RED, ANSI_RESET));
        myContent = stringBuilder.toString();
    }

}
