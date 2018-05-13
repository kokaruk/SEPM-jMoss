package view;

import controller.IController;
import controller.JMossBookingClerkController;
import dal.DALFactory;
import model.Booking;
import model.Cinema;
import model.Movie;
import model.Session;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static view.ViewHelper.*;

/**
 * @author dimz
 * @since 2/5/18.
 */

public class AddBooking extends JMossView {

    private Booking booking;
    private Integer bookingNumber;

    public AddBooking(IController controller) {
        super(controller);
    }

    @Override
    public String getInput() {
        switch (getInputInt()) {
            case 1:
                String custEmail = getEmailFromUserInput();
                int postCode = getPostcodeFromUserInout();
                if (booking == null) {
                    booking = new Booking(custEmail, postCode, bookingNumber);
                } else {
                    booking.setCustomerEmail(custEmail);
                    booking.setSuburbPostcode(postCode);
                }
                buildMyContent();
                return getInput();
            case 2:
                if (booking == null) booking = new Booking();
                ViewHelper.clearScreen();
                addBookingStartingFromAMovie();
                buildMyContent();
                return getInput();
            // if there is booking object and it has selected movie
            case 3:
                if (booking != null && booking.getBookingLines().size() > 0 && confirmSaveBooking()) {
                    // save booking logic goes here
                    bookingSave();
                }
                return "BookingClerkMainMenu";
            default:
                return DEFAULT_RETURN_FROM_USER;
        }
    }

    /**
     * add booking starting from a movie
     */
    private void addBookingStartingFromAMovie() {
        // build movie request string
        StringBuilder moviesSelection = new StringBuilder();
        for (Map.Entry<Integer, Movie> movieEntry : ((JMossBookingClerkController) controller).getMovies().entrySet()) {
            moviesSelection.append(String.format("%d. %s (%s)\n",
                    movieEntry.getKey(),
                    movieEntry.getValue().getMovieName(),
                    movieEntry.getValue().getMovieClassification()
            ));
        }
        // output movie request
        System.out.println(moviesSelection.toString());
        System.out.print(ANSI_RESET + "Movie: ");
        int movieNumber = getInputIntWithBound(((JMossBookingClerkController) controller).getMovies().size());
        Movie movie = ((JMossBookingClerkController) controller).getMovies().get(movieNumber); // searching by key
        ViewHelper.clearScreen();
        Session bookingSession = getSessionFromAMovie(movie);
        if (bookingSession == null) {
            ViewHelper.clearScreen();
            System.out.println("\033[31mNo available sessions for this movie.Try again\n\r\033[0m");
            addBookingStartingFromAMovie();
        } else {
            // get available seats from session
            int availableSeatsInSession = bookingSession.getAvailableSeats();
            System.out.print( ANSI_RESET + "Seats: ");
            int seats = getInputIntWithBound(availableSeatsInSession);
            Date date = getDateFromDayOfWeek(bookingSession.getSessionDay());
            booking.addSession(bookingSession, seats, date);
        }
    }


    /**
     * trnasforms day of week for order to date format
     * @param dayOfWeekForOrder day of booking order
     * @return date of order
     */
    private Date getDateFromDayOfWeek(int dayOfWeekForOrder){
        Date dateToday = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateToday);
        int dayOfWeekToday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        dayOfWeekToday = dayOfWeekToday == 1 ? 7 : --dayOfWeekToday;
        int daysDifference = dayOfWeekForOrder - dayOfWeekToday >= 0 ? dayOfWeekForOrder - dayOfWeekToday : 7 -  dayOfWeekToday + dayOfWeekForOrder;
        calendar.add(Calendar.DATE, daysDifference);
        return calendar.getTime();
    }

    /**
     * get session from provided movie when cinema is not selected
     *
     * @param movie selected movie
     * @return session for booking
     */
    private Session getSessionFromAMovie(Movie movie) {
        // build collection of cinemas this movie runs
        List<Cinema> cinemas = getMovieCinemas(movie);
        // output cinema options
        if (cinemas.size() == 0) return null;
        StringBuilder cinemaOptions = new StringBuilder();
        for (int i = 0; i < cinemas.size(); i++) {
            cinemaOptions.append(String.format("%d. %s\n", (i + 1), cinemas.get(i).getCinemaName()));
        }
        System.out.println(cinemaOptions.toString() + "\n");
        System.out.print("Cinema: ");
        // get which cinema they wat to go
        Cinema cinema = cinemas.get(getInputIntWithBound(cinemas.size()) - 1);

        // list of sessions based on movie and cinema
        List<Session> sessions = getSessionsList(cinema, movie);
        // iterate over sessions and remove already booked ones
        sessions.removeIf(session -> this.booking.hasSession(session));
        // if no sessions left, all either booked out or already added, take a step back

        if (sessions.size() == 0) {
            ViewHelper.clearScreen();
            System.out.println("\033[31mNo available sessions for this movie in this cinema.Try again\n\r\033[0m");
            return getSessionFromAMovie(movie);
        }
        // output sessions
        clearScreen();
        StringBuilder sessionsOptions = new StringBuilder("Pick session\n\r");
        for (int i = 0; i < sessions.size(); i++) {
            sessionsOptions.append(String.format("%d. %s  %d:00 | Available seats: %d\n\r",
                    (i + 1), // session row number
                    DayOfWeek.of(sessions.get(i).getSessionDay()).getDisplayName(TextStyle.SHORT, Locale.CANADA), // session day
                    sessions.get(i).getSessionTime(), // time
                    sessions.get(i).getAvailableSeats()
            ));
        }
        System.out.println(sessionsOptions.toString());
        // get session from the list
        System.out.print("Session: ");
        return sessions.get(getInputIntWithBound(sessions.size()) - 1);
    }

    /**
     * build list of cinemas where a movie is being shown
     *
     * @param movie movie to get cinemas from
     * @return List of cinemas
     */
    private List<Cinema> getMovieCinemas(Movie movie) {
        List<Cinema> cinemas = new ArrayList<>();
        for (Session session : movie.getSessions()) {
            if (!this.booking.hasSession(session) && !cinemas.contains(session.getCinema()) && session.getAvailableSeats() > 0)
                cinemas.add(session.getCinema());
        }
        return cinemas;
    }

    /**
     * Builds list of sessions for a movie cinema pair
     *
     * @param cinema selected cinema
     * @param movie  selected movie
     * @return list of sessions
     */
    private List<Session> getSessionsList(Cinema cinema, Movie movie) {
        return movie.getSessions().stream()
                .filter(session -> session.getCinema().equals(cinema) && session.getAvailableSeats() > 0)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * get email from user input. performs regex validation of email string to confirm valid email entry
     *
     * @return validated email string
     */
    private String getEmailFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        String wrongEmailPatternError = ANSI_RED + "Incorrect email format. Try again" + ANSI_RESET;
        System.out.print("Email: ");
        System.out.print(ANSI_GREEN);
        String customerEmail = scanner.nextLine().trim();
        System.out.print(ANSI_RESET);
        //check regex if string is an actual email
        if (!customerEmail.matches("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
            System.err.println(wrongEmailPatternError);
            customerEmail = getEmailFromUserInput();
        }
        return customerEmail;
    }

    /**
     * get postcode from user input as 4 numbers integer. length == 4
     *
     * @return postcode
     */
    private Integer getPostcodeFromUserInout() {
        Scanner scanner = new Scanner(System.in);
        String wrongPostcodeError = ANSI_RED + "Incorrect postcode format. Try again" + ANSI_RESET;
        System.out.print("Post code: ");
        Integer postCode;

        try {
            System.out.print(ANSI_GREEN);
            postCode = scanner.nextInt();
            System.out.print(ANSI_RESET);
        } catch (Exception ex) {
            // something went wrong, lets try again
            System.err.println(wrongPostcodeError);
            postCode = getPostcodeFromUserInout();
        }

        if (postCode.toString().length() != 4) {
            System.err.println(wrongPostcodeError);
            postCode = getPostcodeFromUserInout();
        }
        return postCode;
    }

    /**
     * build content of this submenu
     */
    void buildMyContent() {
        String header =
                "                   ********************************************************************************\n" +
                        "                   ********************             Booking Status             ********************\n" +
                        "                   ********************************************************************************\n\n";

        String menuOptions =
                "\nPlease start with option " + ANSI_RED + "1" + ANSI_RESET +
                        " to enter in customer details" +
                        "\nand then select at Movie in option " +
                        ANSI_RED + "2" + ANSI_RESET +
                        "\n\n1. Add\\Edit customer details\n" +
                        "2. Select a movie\n" +
                        //   "3. Select a cinema\n" +
                        "3. Save and Return to main menu\n";

        bookingNumber = DALFactory.getBookingRepoDAL().getLargestId() + 1;

        StringBuilder stringBuilder = new StringBuilder(header);
        stringBuilder.append(String.format("Booking number: " + ANSI_CYAN + "%d" + ANSI_RESET + "\n", bookingNumber));
        // if booking object present, append info from it
        if (booking != null) {
            if (booking.getCustomerEmail() != null)
                stringBuilder.append(String.format("Customer email: %s\n", booking.getCustomerEmail()));
            if (booking.getSuburbPostcode() != 0)
                stringBuilder.append(String.format("Postcode: %d\n", booking.getSuburbPostcode()));
            if (booking.getBookingLines().size() != 0) {
                for (Map.Entry<Integer, Booking.Tuple3<Session, Integer, Date>> bookingLine : booking.getBookingLines().entrySet()) {
                    stringBuilder.append(String.format("%d. %s | Seats: %d\n",
                            bookingLine.getKey(),
                            bookingLine.getValue().getSession().getMovie().getMovieName(),
                            bookingLine.getValue().getSeatsBooked()));
                }
            }
        }
        stringBuilder.append(menuOptions);

        myContent = stringBuilder.toString();
    }

    /**
     * yes / no save booking
     */
    private boolean confirmSaveBooking() {
        String confirmBookingSavePrompt = "Save existing booking details?\n" +
                "1. Yes\n" +
                "2. No";
        System.out.println(confirmBookingSavePrompt);
        return getInputIntWithBound(2) - 1 == 0;
    }

    /**
     * gets called when booking object exists and has movies selected and clerk selects 'yes'
     */
    private void bookingSave() {
        // see if booking has customer
        if (booking.getCustomerEmail() == null || booking.getSuburbPostcode() == 0) {
            String custEmail = getEmailFromUserInput();
            int postCode = getPostcodeFromUserInout();
            booking.setCustomerEmail(custEmail);
            booking.setSuburbPostcode(postCode);
            booking.setBookingId(bookingNumber);
        }
        // add booking to every session with seats
        for (Map.Entry<Integer, Booking.Tuple3<Session, Integer, Date>> bookingLine : booking.getBookingLines().entrySet()) {
            bookingLine.getValue().getSession().addBooking(booking,
                    bookingLine.getValue().getSeatsBooked());
        }
        ((JMossBookingClerkController) controller).setBooking(bookingNumber, booking);
    }
}
