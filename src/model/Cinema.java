package model;

import java.util.List;

/**
 * @author dimz
 * @since 23/4/18.
 */
public class Cinema {
    private int MAX_SEATS;
    private String cinemaName;

    private List<Session> sessions;

    public Cinema(int MAX_SEATS, String cinemaName) {
        this.MAX_SEATS = MAX_SEATS;
        this.cinemaName = cinemaName;
    }

    public int getMAX_SEATS() {
        return MAX_SEATS;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public List<Session> getSessions() {
        return sessions;
    }
}
