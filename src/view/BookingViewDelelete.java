package view;

import controller.IController;
import controller.JMossBookingClerkController;
import model.Booking;
import model.Session;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static view.ViewHelper.*;

/**
 * @author dimz
 * @since 12/5/18.
 */
public class BookingViewDelelete extends JMossView {

    public BookingViewDelelete(IController controller) {
        super(controller);
    }

    @Override
    public String getInput() {

        switch (getInputInt()) {
            case 2:
                clearScreen();
                getBookingByEmail();
                return getInput();
            case 3:
                clearScreen();
                getBookingFromList(getAllBookings());
                return getInput();
            case 1:
                return "BookingClerkMainMenu";
            default:
                return DEFAULT_RETURN_FROM_USER;
        }
    }

    private void getBookingByEmail() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Email (whole or part of string) : ");
        System.out.print(ANSI_GREEN);
        String customerEmail = scanner.nextLine().trim();
        System.out.print(ANSI_RESET);
        List<Booking> bookings = ((JMossBookingClerkController) controller)
                .getBookings()
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .filter(booking -> booking.getCustomerEmail().contains(customerEmail))
                .collect(Collectors.toCollection(ArrayList::new));
        if (bookings.size() == 0) {
            StringBuilder stringBuilder = new StringBuilder(ANSI_RED + "Nothing found" + ANSI_RESET);
            stringBuilder.append("\n1. Try again");
            stringBuilder.append("\n2. Go Back");
            System.out.println(stringBuilder.toString());
            switch (getInputIntWithBound(2)) {
                case 1:
                    clearScreen();
                    getBookingByEmail();
                    break;
                default:
                    return;
            }
        }
        getBookingFromList(bookings);
    }

    private List<Booking> getAllBookings() {
        return ((JMossBookingClerkController) controller)
                .getBookings()
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void getBookingFromList(List<Booking> bookingsList) {
        if (bookingsList.size() == 0) {
            setError(true);
            return;
        }
        System.out.print(ANSI_RESET);
        StringBuilder bookingsListOptions = new StringBuilder();
        for (int i = 0; i < bookingsList.size(); i++) {
            bookingsListOptions.append(String.format(ANSI_RESET + "%d. %s\n", i + 1, bookingsList.get(i).getCustomerEmail()));
        }
        System.out.println(bookingsListOptions.toString());
        System.out.print("Choose number to view booking: ");
        int bookingNumber = getInputIntWithBound(bookingsList.size());
        clearScreen();
        Booking booking = bookingsList.get(--bookingNumber);
        showBookingStatus(booking);


    }

    @SuppressWarnings("Duplicates")
    private void showBookingStatus(Booking booking) {
        String header = "\n" +
                "\n" +
                "                   ********************************************************************************\n" +
                "                   ********************             Booking Status             ********************\n" +
                "                   ********************************************************************************\n" +
                " \n" +
                "Booking Number: " + booking.getBookingId() + "\n" +
                "Customer email: " + booking.getCustomerEmail() + "\n" +
                "Customer postcode: " + booking.getSuburbPostcode() + "\n";


        StringBuilder stringBuilder = new StringBuilder(header);

        for (Map.Entry<Integer, Booking.Tuple3<Session, Integer, Date>> bookingLine : booking.getBookingLines().entrySet()) {
            stringBuilder.append("=======================================================\n");
            stringBuilder.append(String.format("#%d\n", bookingLine.getKey()));
            stringBuilder.append(String.format("Location: %s\n", bookingLine.getValue().getSession().getCinema().getCinemaName()));
            stringBuilder.append(String.format("Day: %s\n", DayOfWeek.of(bookingLine.getValue().getSession().getSessionDay()).getDisplayName(TextStyle.SHORT, Locale.CANADA)));
            stringBuilder.append(String.format("Time: %d:00\n", bookingLine.getValue().getSession().getSessionTime()));
            stringBuilder.append(String.format("Movie: %s\n", bookingLine.getValue().getSession().getMovie().getMovieName()));
            stringBuilder.append(String.format("Seats: %d\n", bookingLine.getValue().getSeatsBooked()));
        }
        //max options for request
        int maxValueOptions = 2;
        //check if booking has more than one line
        stringBuilder.append("\n\n1. Go back\n");
        stringBuilder.append("2. Delete whole booking\n");
        if (booking.getBookingLines().size() > 1) {
            stringBuilder.append("3. Delete session booking\n");
            maxValueOptions++;
        }

        System.out.println(stringBuilder.toString());
        switch (getInputIntWithBound(maxValueOptions)) {
            case 1: // do nothig
                break;
            case 2:
                removeBooking(booking);
                break;
            case 3:
                removeLine(booking);
        }
    }

    @SuppressWarnings("Duplicates")
    private void removeLine(Booking booking) {
            StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Integer, Booking.Tuple3<Session, Integer, Date>> bookingLine : booking.getBookingLines().entrySet()) {
            stringBuilder.append("=======================================================\n");
            stringBuilder.append(String.format("#%d\n", bookingLine.getKey()));
            stringBuilder.append(String.format("Location: %s\n", bookingLine.getValue().getSession().getCinema().getCinemaName()));
            stringBuilder.append(String.format("Day: %s\n", DayOfWeek.of(bookingLine.getValue().getSession().getSessionDay()).getDisplayName(TextStyle.SHORT, Locale.CANADA)));
            stringBuilder.append(String.format("Time: %d:00\n", bookingLine.getValue().getSession().getSessionTime()));
            stringBuilder.append(String.format("Movie: %s\n", bookingLine.getValue().getSession().getMovie().getMovieName()));
            stringBuilder.append(String.format("Seats: %d\n", bookingLine.getValue().getSeatsBooked()));
        }

        clearScreen();
        System.out.print(stringBuilder.toString());
        System.out.print("\n\nWhich session to remove?");
        int lineTodelete = getInputIntWithBound(booking.getBookingLines().size());
        clearScreen();
        String confirm = "\n\n1. Yes \n" +
                "2. No\n" +
                "\nConfirm line deletion:";

        System.out.print(confirm);
        int confirmInt = getInputIntWithBound(2);
        if(confirmInt == 1){
            ((JMossBookingClerkController) controller).removeBookingLine(booking, lineTodelete);
        }

    }

    private void removeBooking(Booking booking) {
        clearScreen();
        String confirm = "\n\n1. Yes \n" +
                "2. No\n" +
                "\nConfirm booking deletion:";

        System.out.print(confirm);
        int confirmInt = getInputIntWithBound(2);
        if (confirmInt == 1) {
            // clear order lines
            for (Map.Entry<Integer, Booking.Tuple3<Session, Integer, Date>> bookingLine : booking.getBookingLines().entrySet()) {
                // make seats available
                Session session = bookingLine.getValue().getSession();
                int bookingSeats = bookingLine.getValue().getSeatsBooked();
                session.removeBooking(booking, bookingSeats);
                booking.getBookingLines().remove(bookingLine.getKey());
                //clear booking from controller (oh its so bad talking from view to controller)
                ((JMossBookingClerkController) controller).removeBooking(booking);
                buildMyContent();
            }
        }
    }


    @Override
    void buildMyContent() {
        myContent  =
                "                   ********************************************************************************\n" +
                        "                   ****************             Find / Delete Booking              ****************\n" +
                        "                   ********************************************************************************\n\n" +
                        "Bookings recorded in the system: " + ANSI_YELLOW + ((JMossBookingClerkController) controller).getBookings().size()  + ANSI_RESET +
                        "\n\n1. Return to previous menu";

        if (((JMossBookingClerkController) controller).getBookings().size() > 0 ) {
            myContent +=  "\n2. Find booking by email\n" +
                    "3. See full list of bookings\n";
        }

    }
}
