package model;

/**
 * Class for type of user Booking Clerk
 * get's instantiated via reflection at login time
 * @author dimz
 * @since 21/4/18.
 */
@SuppressWarnings("unused")
public class BookingClerk extends User {
    public BookingClerk(int userId, String userName) {
        super(userId, userName);
    }
}
