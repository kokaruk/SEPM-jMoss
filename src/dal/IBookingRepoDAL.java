package dal;

import model.Booking;

import java.util.Map;

public interface IBookingRepoDAL {
    // create
    // read
    Map<Integer, Booking> getAllBookings();
    // update
    // delete
}
