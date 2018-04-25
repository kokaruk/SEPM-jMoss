package model;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author dimz
 * @since 23/4/18.
 */
public class Cinema {

    private int MAX_SEATS;
    private String cinemaName;
    private Set<Session> sessions;

    public Cinema(String cinemaName, int MAX_SEATS) {
        this.MAX_SEATS = MAX_SEATS;
        this.cinemaName = cinemaName;
    }

    public int getMAX_SEATS() {
        return MAX_SEATS;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void addSession(Session session) {
        if(sessions == null) sessions = new LinkedHashSet<>(); // lazy instantiate
        this.sessions.add(session);
    }

    public Set<Session> getSessions() {
        return sessions;
    }
}
