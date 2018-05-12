package view;

import controller.IController;
import controller.JMossBookingClerkController;
import model.Cinema;

import java.util.Map;

import static view.ViewHelper.ANSI_RED;
import static view.ViewHelper.ANSI_RESET;

/**
 * @author dimz
 * @since 11/5/18.
 */
public class CineplexSearch extends JMossView {

    public CineplexSearch(IController controller) {
        super(controller);
    }

    @Override
    void buildMyContent() {
        System.out.print(ANSI_RESET);
        String header =
                "                   ********************************************************************************\n" +
                        "                   ********************              Cinema Status             ********************\n" +
                        "                   ********************************************************************************\n\n";
        StringBuilder stringBuilder = new StringBuilder(header);

        stringBuilder.append(String
                .format("\nSelect a cinema # " + ANSI_RED + "(1-%d)" + ANSI_RESET +" or type "+ ANSI_RED +"0" + ANSI_RESET + " to exit to previous menu\n\n",
                        ((JMossBookingClerkController) controller).getCinemas().size()));

        // for each movie
        for (Map.Entry<Integer, Cinema> cinemaEntry : ((JMossBookingClerkController) controller).getCinemas().entrySet()) {
            stringBuilder.append(String.format("%d. %s\n", cinemaEntry.getKey(), cinemaEntry.getValue().getCinemaName()));
        }
        myContent = stringBuilder.toString();
    }

    @Override
    public String getInput() {
        int userInput = getInputInt();
        switch (userInput) {
            case 0:
                return "BookingClerkMainMenu";
            default:
                if (userInput > 0 && userInput <= ((JMossBookingClerkController) controller).getCinemas().size()) {
                    ViewHelper.clearScreen();
                    cinemaInfo(userInput);
                    return getInput();
                } else {
                    return DEFAULT_RETURN_FROM_USER;
                }

        }
    }

    private void cinemaInfo(int userInput) {
        Cinema cinema = ((JMossBookingClerkController) controller)
                .getCinemas()
                .get(userInput);

        String greetingsPrefix = "\t\t\t********************************************************************************\n";
        String prePostFix = "********************";
        String cinemaNamePrefixSpace = "             "; // position where to start inserting the name;
        // compensate for tabs = 4
        int padSpacesLength = greetingsPrefix.length() - 2*prePostFix.length() - cinemaNamePrefixSpace.length() - 4;

        String cinemaNamePrefix = "\t\t\t" + prePostFix +
                cinemaNamePrefixSpace +
                padRight(cinema.getCinemaName(),  padSpacesLength) +
                prePostFix +"\n";





        String header =
                greetingsPrefix +
                    cinemaNamePrefix +
                        greetingsPrefix;

        StringBuilder stringBuilder = new StringBuilder(header);
        makeCinemaMoviesSessionsTable(stringBuilder, cinema);
        stringBuilder.append("\nType \033[31m1\033[0m to return to previous menu");
        System.out.println(stringBuilder.toString());
        getIntFromUser(1);

    }

}
