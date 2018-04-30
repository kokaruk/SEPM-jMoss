package dal;

import assets.ConfigFileMissingException;
import assets.HelperFunctions;
import model.Booking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class BookingRepo implements IBookingRepoDAL{

    private final static String BOOKING_FILE;

    static {
        String BOOKING_FILE1;
        try {
            BOOKING_FILE1 = HelperFunctions.getConfigReader().getConfigString("BOOKING_FILE");
        } catch (ConfigFileMissingException e){
            BOOKING_FILE1 = "";
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
