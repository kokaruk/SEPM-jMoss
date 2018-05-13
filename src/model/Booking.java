package model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * booking class
 * @author dimz
 * @since 26/4/18.
 */
public class Booking {

    private int bookingId;
    private String customerEmail;
    private int suburbPostcode;
    // order lines will be stored here
    // integer -> ID
    // session -> Session
    // integer -> seats
    //
    private Map<Integer, Tuple3<Session, Integer, Date>> bookingLines;

    // default constructor
    public Booking(){
        bookingLines = new HashMap<>();
    }

    public Booking(String customerEmail, int suburbPostcode, int bookingId) {
        this();
        this.customerEmail = customerEmail;
        this.suburbPostcode = suburbPostcode;
        this.bookingId = bookingId;
    }

    ////// GETTERS ////////////
    public String getCustomerEmail() {
        return customerEmail;
    }
    public Integer getSuburbPostcode() {
        return suburbPostcode;
    }
    public Map<Integer, Tuple3<Session, Integer, Date>> getBookingLines() {
        return bookingLines;
    }
    public int getBookingId() {
        return bookingId;
    }

    /////// SETTERS ////////////
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public void setSuburbPostcode(int suburbPostcode) {
        this.suburbPostcode = suburbPostcode;
    }
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    // invoked when adding new booking line
    public void addSession(Session session, Integer seats, Date date){
        if( ! this.hasSession(session)){
            bookingLines.put(bookingLines.size()+1, new Tuple3<>(session, seats, date) );
        }
    }

    /**
     * method to check if booking lines contains the session already
     * @return true if this session present in this booking
     */
    public boolean hasSession(Session session){
        for (Map.Entry<Integer, Tuple3<Session, Integer, Date>> sessionLine : bookingLines.entrySet() ){
            if(sessionLine.getValue().session == session) return true;
        }
        return false;
    }


    public class Tuple3<T extends Session, S extends Integer, U extends Date>  {
        private T session;
        private S seatsBooked;
        private U dateBooked;

        public Tuple3(T session, S seatsBooked, U dateBooked) {
            this.session = session;
            this.seatsBooked = seatsBooked;
            this.dateBooked = dateBooked;
        }

        public T getSession() {
            return session;
        }

        public void setSession(T session) {
            this.session = session;
        }

        public S getSeatsBooked() {
            return seatsBooked;
        }

        public void setSeatsBooked(S seatsBooked) {
            this.seatsBooked = seatsBooked;
        }

        public U getDateBooked() {
            return dateBooked;
        }

        public void setDateBooked(U dateBooked) {
            this.dateBooked = dateBooked;
        }
    }

}
