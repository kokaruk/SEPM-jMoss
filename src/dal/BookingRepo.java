package dal;

import assets.HelperFunctions;
import model.Booking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Real implementation of booking repo. Interacts with Data Reader class
 */
final class BookingRepo implements IBookingRepoDAL{
    private static Logger logger = LogManager.getLogger();
    private final static String BOOKING_FILE;

    static {
        String BOOKING_FILE1;
        try {
            BOOKING_FILE1 = HelperFunctions.getConfigReader().getConfigString("BOOKING_FILE");
        } catch (IOException e){
            BOOKING_FILE1 = "";
            logger.fatal(e.toString());
        }
        BOOKING_FILE = BOOKING_FILE1;
    }


    // singleton instance
    private static IBookingRepoDAL instance;

    //private constructor
    private BookingRepo() {}

    // lazy instance constructor
    public static IBookingRepoDAL getInstance() {
        if (instance == null) {
            instance = new BookingRepo();
        }
        return instance;
    }

    @Override
    public Map<Integer, Booking> getAllBookings() {
        Set<List<String>> allFile = CSVUtils.getInstance().readAll(BOOKING_FILE);
        Map<Integer, Booking> allBookings = new HashMap<>();
        try {
            allFile.forEach(bookingRow ->
                    allBookings.put(Integer.parseInt(bookingRow.get(0)),
                            new Booking(bookingRow.get(1),
                                   Integer.parseInt(bookingRow.get(2)))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allBookings;

    }
}
