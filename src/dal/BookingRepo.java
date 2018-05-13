package dal;

import assets.HelperFunctions;
import model.Booking;
import model.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Real implementation of booking repo. Interacts with Data Reader class
 */
final class BookingRepo implements IBookingRepoDAL{
    private static Logger logger = LogManager.getLogger();
    private final static String BOOKING_FILE;
    private final static String BOOKING_LINES_FILE;

    // we need to know largest id of booking in file
    // since it's all is in natural order, last line in file would contain largest ID number
    private Integer largestId;

    static {
        String BOOKING_FILE1;
        try {
            BOOKING_FILE1 = HelperFunctions.getConfigReader().getConfigString("BOOKING_FILE");
        } catch (IOException e){
            BOOKING_FILE1 = "";
            logger.fatal(e.toString());
        }
        BOOKING_FILE = BOOKING_FILE1;
        String BOOKING_LINES_FILE1;
        try {
            BOOKING_LINES_FILE1 = HelperFunctions.getConfigReader().getConfigString("BOOKING_LINES_FILE");
        } catch (IOException e){
            BOOKING_LINES_FILE1 = "";
            logger.fatal(e.toString());
        }
        BOOKING_LINES_FILE = BOOKING_LINES_FILE1;
    }


    // singleton instance
    private static IBookingRepoDAL instance;

    //private constructor
    private BookingRepo() {
        largestId = 0;
    }

    // lazy instance constructor
    public static IBookingRepoDAL getInstance() {
        if (instance == null) {
            instance = new BookingRepo();
        }
        return instance;
    }

    @Override
    public Integer getLargestId() {
        return largestId;
    }

    @Override
    public Map<Integer, Booking> getAllBookings() {
        Set<List<String>> allFile = CSVUtils.getInstance().readAll(BOOKING_FILE);
        Map<Integer, Booking> allBookings = new HashMap<>();
        try {
            allFile.forEach(bookingRow -> {
                allBookings.put(allBookings.size()+1,
                            new Booking(bookingRow.get(1),
                                   Integer.parseInt(bookingRow.get(2)),
                        Integer.parseInt(bookingRow.get(0))));
                if (largestId < Integer.parseInt(bookingRow.get(0))){
                    largestId = Integer.parseInt(bookingRow.get(0));
                }
                });
        } catch (Exception e) {
            //some exception handling code goes here
            e.printStackTrace();
        }
        return allBookings;
    }

    @Override
    public Set<List<String>> getBookingLines() {
        return CSVUtils.getInstance().readAll(BOOKING_LINES_FILE);
    }

    @Override
    public void addNewBooking(Integer bookingNumber, Booking booking) {

            List<String> bookingRowList = new LinkedList<>();
            bookingRowList.add(bookingNumber.toString()); // booking id
            bookingRowList.add(booking.getCustomerEmail()); // cust email
            bookingRowList.add(booking.getSuburbPostcode().toString()); // cust postcode
            try { // writing to file
                CSVUtils.getInstance().writeLine(bookingRowList, BOOKING_FILE);
                largestId++; // increment 'cause just added new line
            } catch (IOException e){
                e.printStackTrace();
                logger.fatal(e.getMessage());
            }
            //append booking lines
            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
            for(Map.Entry<Integer, Booking.Tuple3<Session, Integer, Date>> bookingLine : booking.getBookingLines().entrySet()){
                List<String > bookingLineList = new LinkedList<>();
                bookingLineList.add(bookingNumber.toString()); // add booking id
                bookingLineList.add(bookingLine.getValue().getSession().getId().toString()); //add sessionId
                bookingLineList.add(bookingLine.getValue().getSeatsBooked().toString());
                bookingLineList.add(dateParser.format(bookingLine.getValue().getDateBooked()));
                try{
                    CSVUtils.getInstance().writeLine(bookingLineList, BOOKING_LINES_FILE);
                } catch (IOException e){
                    e.printStackTrace();
                    logger.fatal(e.getMessage());
                }
            }

    }
}
