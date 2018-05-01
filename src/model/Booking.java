package model;

/**
 * booking class
 * @author dimz
 * @since 26/4/18.
 */
public class Booking {

    private String customerEmail;
    private int suburbPostcode;

    public Booking(){}

    public Booking(String customerEmail, int suburbPostcode) {
        this.customerEmail = customerEmail;
        this.suburbPostcode = suburbPostcode;
    }
}
