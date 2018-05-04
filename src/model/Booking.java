package model;

import java.util.Map;

/**
 * booking class
 * @author dimz
 * @since 26/4/18.
 */
public class Booking {

    private String customerEmail;
    private int suburbPostcode;
    // order lines will be stored here
    private Map<Integer, Pair<Session, Integer>> bookingLines;

    public Booking(String customerEmail, int suburbPostcode) {
        this.customerEmail = customerEmail;
        this.suburbPostcode = suburbPostcode;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public int getSuburbPostcode() {
        return suburbPostcode;
    }

    private class Pair<T extends Session, M extends Integer>  {
        private T session;
        private M seatsBooked;

        public Pair(T session, M seatsBooked) {
            this.session = session;
            this.seatsBooked = seatsBooked;
        }

        public Session getSession() {
            return session;
        }

        public Integer getSeatsBooked() {
            return seatsBooked;
        }
    }

}
