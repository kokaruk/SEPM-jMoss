package view;

import controller.IController;
import controller.JMossBookingClerkController;
import model.Booking;
import model.Movie;
import model.Session;

import java.util.Map;
import java.util.Scanner;

/**
 * @author dimz
 * @since 2/5/18.
 */

public class AddBooking extends JMossView{

    private IController controller;
    private Booking booking;

    public AddBooking(IController controller) {
        this.controller = controller;
        buildMyContent();
    }

    @Override
    public String getInput() {
        switch (super.getInputInt()) {
            case 1: System.out.print("Email: ");
                String custEmail = getEmailFromUserInput();
                int postCode = getPostcodeFromUserInout();
                booking = new Booking(custEmail, postCode);
                buildMyContent();
                return getInput();
            case 2: return Integer.toString(addMovie().getMovieId()) ;

            case 6: return "BookingClerkMainMenu";
            default: return "unknown";
        }
    }

    /**
     * add movie to this booking
     */
    private Session addMovie(){
        ViewHelper.clearScreen();
        // output all movies
        StringBuilder moviesSelection = new StringBuilder("What Movie?\n");

        /*
        *********    drunk, fix later...maybe    ***************
         */

        for(Map.Entry<Integer, Movie> movieEntry : ((JMossBookingClerkController) controller).getMovies().entrySet()){
            moviesSelection.append(String.format("%d. %s\n", movieEntry.getKey(), movieEntry.getValue().getMovieName()));
        }
        System.out.println(moviesSelection.toString());

        return getSession(((JMossBookingClerkController) controller).getMovies().get(getMovieNumber()));
    }


    private Integer getMovieNumber(){
        Scanner scanner = new Scanner(System.in);
        String wrongMovieNumber = "\033[31mWrong movie number. Try again\033[37m";
        System.out.print("Movie number: ");
        Integer movieNumber;
        try {
            movieNumber = scanner.nextInt();
        } catch (Exception ex) {
            // don't care what exception, just try again
            System.out.println(wrongMovieNumber);
            movieNumber = getMovieNumber();
        }

        // if number is incorrect
        if(movieNumber<1 || movieNumber > ((JMossBookingClerkController)controller).getMovies().size()){
            // incorrect number
            System.out.println(wrongMovieNumber);
            movieNumber = getMovieNumber();
        }

        return movieNumber;

    }


    private Session getSession(Movie movie){
        ViewHelper.clearScreen();
        // output sessions
        StringBuilder sessions = new StringBuilder("Pick session\n\r");
        for( int i = 0; i < movie.getSessions().size(); i++ ){
            sessions.append(String.format("%d. %s %d\n\r",  (i+1), // session row number
                    movie.getSessions().get(i).getCinema().getCinemaName(), // cinema name
                    movie.getSessions().get(i).getSessionTime())); // time
        }
        System.out.println(sessions.toString());
        return movie.getSessions().get(getSessionNumber(movie));
    }

    private int getSessionNumber(Movie movie){
        Scanner scanner = new Scanner(System.in);
        String wrongSessionNumber = "\033[31mWrong session number. Try again";
        System.out.println("Session: ");
        Integer sessionNumber;
        try {
            System.out.println(wrongSessionNumber);
            sessionNumber = scanner.nextInt();
        } catch (Exception ex){
            // don't care what exception, just try again
            sessionNumber = getSessionNumber(movie);
        }

        if(sessionNumber<0 || sessionNumber > movie.getSessions().size()){
            System.out.println(wrongSessionNumber);
            sessionNumber = getSessionNumber(movie);
        }

        return sessionNumber;
    }

    private String getEmailFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        String wrongEmailPatternError =  "\033[31mIncorrect email format. Try again";
        String custEmail = scanner.nextLine().trim();
        //check regex if string is an actual email
        if (!custEmail.matches("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")){
            System.err.println(wrongEmailPatternError);
            custEmail = getEmailFromUserInput();
        }
        return custEmail;
    }

    private Integer getPostcodeFromUserInout(){
        Scanner scanner = new Scanner(System.in);
        String wrongPostcodeError = "\033[31mIncorrect Postocde format. Try again";
        System.out.print("Post code: ");
        Integer postCode;

        try {
            postCode = scanner.nextInt();
        } catch (Exception ex){
            // something went wrong, lets try again
            System.err.println(wrongPostcodeError);
            postCode = getPostcodeFromUserInout();
        }

        if(postCode.toString().length() != 4 ){
            System.err.println(wrongPostcodeError);
            postCode = getPostcodeFromUserInout();
        }
        return postCode;
    }

    private void buildMyContent(){
        String header =
                "                   ********************************************************************************\n" +
                        "                   ********************             Booking Status             ********************\n" +
                        "                   ********************************************************************************\n\n";
        String menuOptions = "\n\n1. Add customer details\n" +
                "2. Select a movie\n" +
                "3. Select a cinema\n" +
                "6. Return to previous";

        int bookingNumber = ((JMossBookingClerkController) controller).getBookings().size() + 1;

        StringBuilder stringBuilder = new StringBuilder(header);
        stringBuilder.append(String.format("Booking number: %d\n", bookingNumber));
        // if booking object present, append info from it
        if(booking != null) {
            stringBuilder.append(String.format("Customer email: %s\n",booking.getCustomerEmail()));
            stringBuilder.append(String.format("Postcode: %d", booking.getSuburbPostcode()));
        }
        stringBuilder.append(menuOptions);

        setMyContent(stringBuilder.toString());
    }


}
