package view;

import controller.IController;
import model.Cinema;
import model.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static view.ViewHelper.*;

/**
 * @author dimz
 * @since 21/4/18.
 */
public abstract class JMossView {

    IController controller;
    String myContent;
    private boolean error;
    final String DEFAULT_RETURN_FROM_USER = "unknown";

    private int[] hours = {10, 12, 16, 19, 22}; // hours of sessions array

    JMossView(){
        buildMyContent();
    }
    JMossView(IController controller){
        this.controller = controller;
        buildMyContent();
    }

    void wrongInput() {
        System.err.println("\033[31mThis input value is not allowed\n\r\033[0m");
    }

    void displayContent(){
        System.out.println(myContent);
    }

    /**
     * gets input from user
     * @return corresponding values based on user input
     */
     public abstract String getInput();

    /**
     * get input integer
     */
    int getInputInt(){
        initInput();
        Scanner scanner = new Scanner(System.in);
        Integer option = -1;
        try {
            System.out.print(ANSI_GREEN);
            option = scanner.nextInt();
            System.out.print(ANSI_RESET);
        } catch (Exception e) {
            System.out.print(ANSI_RESET);
            setError(true);
        }
        return option;
    }

    /**
     * init user input screen
     */
    void initInput(){
        clearScreen();
        System.out.print(ANSI_RESET);
        if(isError()){
            displayContent();
            wrongInput();
            setError(false);
        } else {
            displayContent();
        }
    }

    /**
     * ask user for a nunmber input with specified max value
     * @param maxValue the largest number allowed
     * @return int from user input
     */
    int getInputIntWithBound(int maxValue){
        int number;
        String wrongNumber = ANSI_RED + "Wrong number input. Try again" + ANSI_RESET;
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print(ANSI_GREEN);
            number = scanner.nextInt();
            System.out.print(ANSI_RESET);
            if(number <1 || number > maxValue){
                System.out.println(wrongNumber);
                number = getInputIntWithBound(maxValue);
            }
        } catch (Exception ex){
            System.out.print(ANSI_RESET);
            // don't care what exception, just try again
            System.out.println(wrongNumber);
            number = getInputIntWithBound(maxValue);
        }
        return number;
    }


    /**
     * error. set it to true if calling to get input did not produce required result
     */
    public void setError(boolean error) {
        this.error = error;
    }

    boolean isError() {
        return error;
    }

    /**
     * build content of this submenu
     */
    abstract void buildMyContent();


    void makeCinemaMoviesSessionsTable(StringBuilder stringBuilder, Cinema cinema){
        final int MAX_MOVIE_STRING_LENGTH = 14; // length of a movie name string for large table.
        String daysRow = "    A. Mon              B. Tue              C. Wed              D. Thur             E. Fri              F. Sat              G. Sun\n";
        // String cinemaNameRow = String.format("\n%s. %s\n", cinemaEntry.getKey(), cinemaEntry.getValue().getCinemaName());
        // stringBuilder.append(cinemaNameRow);
        stringBuilder.append(daysRow);
        Set<Session> sessions = cinema.getSessions();
        for (Integer hour : hours) {
            Predicate<Session> sessionPredicate = session -> session.getSessionTime() == hour;
            List<Session> hourSessions = sessions
                    .stream()
                    .filter(sessionPredicate)
                    .collect(Collectors.toCollection(ArrayList::new));
            StringBuilder movieRow = new StringBuilder(hour.toString());
            for (Session session : hourSessions) {
                String movieName = session.getMovie().getMovieName();
                movieName = movieName.length() >= MAX_MOVIE_STRING_LENGTH ?
                        movieName.substring(0, Math.min(movieName.length(), MAX_MOVIE_STRING_LENGTH)) :
                        padRight(movieName, MAX_MOVIE_STRING_LENGTH);
                movieRow.append(String.format("  %s(%02d)",
                        movieName,
                        session.getAvailableSeats()));
            }
            movieRow.append("\n");
            stringBuilder.append(movieRow);
        }


    }

    /**
     * bloody java caches string format! need to have a separate function!!!
     * pad a string with spaces
     * @param string for padding
     * @param n spaces to pad
     * @return padded string
     */
    String padRight(String string, int n){
        return String.format("%1$-" + n + "s", string);
    }


}
