package model;

import java.util.HashMap;
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

    // default constructor
    public Booking(){
        bookingLines = new HashMap<>();
    }

    public Booking(String customerEmail, int suburbPostcode) {
        this();
        this.customerEmail = customerEmail;
        this.suburbPostcode = suburbPostcode;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public Integer getSuburbPostcode() {
        return suburbPostcode;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setSuburbPostcode(int suburbPostcode) {
        this.suburbPostcode = suburbPostcode;
    }

    // invoked when adding new booking line
    public void addSession(Session session, Integer seats){
        if( ! this.hasSession(session)){
            bookingLines.put(bookingLines.size()+1, new Pair<>(session, seats) );
        }
    }

    /**
     * method to check if booking lines contains the session already
     * @return true if this session present in this booking
     */
    public boolean hasSession(Session session){
        for (Map.Entry<Integer, Pair<Session, Integer>> sessionLine : bookingLines.entrySet() ){
            if(sessionLine.getValue().session == session) return true;
        }
        return false;
    }



    public Map<Integer, Pair<Session, Integer>> getBookingLines() {
        return bookingLines;
    }

    public class Pair<T extends Session, M extends Integer>  {
        private T session;
        private M seatsBooked;

        Pair(T session, M seatsBooked) {
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
