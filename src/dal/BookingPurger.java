package dal;

import assets.HelperFunctions;
import model.Booking;
import model.Cinema;
import model.Movie;
import model.Session;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * running data purger on a separate thread
 * @author dimz
 * @since 13/5/18.
 */
public class BookingPurger extends Thread {

    static {
        String BOOKING_FILE1;
        try {
            BOOKING_FILE1 = HelperFunctions.getConfigReader().getConfigString("BOOKING_FILE");
        } catch (IOException e){
            BOOKING_FILE1 = "";
        }
        BOOKING_FILE = BOOKING_FILE1;
        String BOOKING_LINES_FILE1;
        try {
            BOOKING_LINES_FILE1 = HelperFunctions.getConfigReader().getConfigString("BOOKING_LINES_FILE");
        } catch (IOException e){
            BOOKING_LINES_FILE1 = "";
        }
        BOOKING_LINES_FILE = BOOKING_LINES_FILE1;
    }

        private final static String BOOKING_FILE;
        private final static String BOOKING_LINES_FILE;

        @Override
        public void run() {
            purgeOldOrders();
        }

        /**
         * delete old orders
         */
        void purgeOldOrders() {
            Map<Integer, Cinema> cinemas = DALFactory.getCinemaRepoDAL().getAllCinemas();
            Map<Integer, Booking> bookings = DALFactory.getBookingRepoDAL().getAllBookings();
            Map<Integer, Session> sessions = DALFactory.getSessionRepoDAL().getAllSessions();
            Map<Integer, Movie> movies = DALFactory.getMovieRepoDAL().getAllMovies();
            for (Map.Entry<Integer, Session> sessionEntry : sessions.entrySet()) {
                Session session = sessionEntry.getValue();
                session.setMovie(movies.get(session.getMovieId()));
                session.setCinema(cinemas.get(session.getCinemaId()));
            }
            DALFactory
                    .getBookingRepoDAL()
            .getBookingLines()
                    .forEach(row -> {
                                try {
                                    Booking booking = bookings.get(Integer.parseInt(row.get(0)));
                                    Session session = sessions.get(Integer.parseInt(row.get(1)));

                                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                                    Date today = parser.parse(parser.format(new Date()));
                                    Date orderDate = parser.parse(row.get(3));
                                    // ignore old orders
                                    if (!orderDate.before(today)) {
                                        booking.addSession(session, Integer.parseInt(row.get(2)),
                                                orderDate);
                                        session.addBooking(booking);
                                    }
                                } catch (ParseException e) {
                                    // do nothing we simply ignore the data, no
                                    // todo may be try repairing in the future
                                }
                            }
                    );

           saveData(bookings);
        }

        public void saveData(Map<Integer, Booking> bookings){
            // clear empty bookings
            Map<Integer, Booking> reducedBookings = bookings
                    .entrySet()
                    .parallelStream()
                    .filter(integerBookingEntry -> integerBookingEntry.getValue().getBookingLines().size() > 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            // build lists and write to file
            List<String> bookingRowsList = new LinkedList<>();
            bookingRowsList.add("id,email,suburb");
            List<String> bookingLinesRowList = new LinkedList<>();
            bookingLinesRowList.add("booking_id,session_id,tickets_count,date");
            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
            for(Map.Entry<Integer, Booking> bookingEntry : reducedBookings.entrySet()){
                bookingRowsList.add(
                        String.format(
                                "\"%d\",\"%s\",\"%d\"",
                                bookingEntry.getKey(),
                                bookingEntry.getValue().getCustomerEmail(),
                                bookingEntry.getValue().getSuburbPostcode()
                        ));
                for(Map.Entry<Integer, Booking.Tuple3<Session, Integer, Date>> bookinLine :
                        bookingEntry.getValue().getBookingLines().entrySet()){
                    bookingLinesRowList.add(
                            String.format(
                                    "\"%d\",\"%d\",\"%d\",\"%s\"",
                                    bookingEntry.getKey(),
                                    bookinLine.getValue().getSession().getId(),
                                    bookinLine.getValue().getSeatsBooked(),
                                    dateParser.format(bookinLine.getValue().getDateBooked())
                            )
                    );
                }
            }

            try{
                CSVUtils.getInstance().reWriteAllFile(bookingRowsList, BOOKING_FILE);
                CSVUtils.getInstance().reWriteAllFile(bookingLinesRowList, BOOKING_LINES_FILE);
            } catch (IOException e) {
                //can't write to file for some reason
            }
        }

    }

