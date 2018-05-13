package dal;

import model.Booking;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IBookingRepoDAL {
    // create
    void addNewBooking(Integer bookingNumber, Booking booking);
    // read
    Map<Integer, Booking> getAllBookings();
    Set<List<String>> getBookingLines();
    // update
    // delete

    /**
     * get largest id of booking, used by adding new booking. since we are reading file anyway, saves time for iterating
     * collection later
      */
    Integer getLargestId();

}
