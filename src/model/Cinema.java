package model;

import assets.HelperFunctions;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author dimz
 * @since 23/4/18.
 */
public class Cinema {

    private int MAX_SEATS;
    private String cinemaName;
    private Set<Session> sessions;
    private final int SESSIONS_PER_DAY_LIMIT;

    private Map<DayOfWeek,Integer> dayOfWeekSessionCounts;

    public Cinema(String cinemaName, int MAX_SEATS) {
        int SESSIONS_PER_DAY_LIMIT1;
        try {
            SESSIONS_PER_DAY_LIMIT1 = HelperFunctions.getConfigReader().getConfigInt("SESSIONS_PER_DAY_LIMIT");
        } catch (Exception E) {
            // doesn't matter what exception
            SESSIONS_PER_DAY_LIMIT1 = 0;
        }
        SESSIONS_PER_DAY_LIMIT = SESSIONS_PER_DAY_LIMIT1;
        this.MAX_SEATS = MAX_SEATS;
        this.cinemaName = cinemaName;
        dayOfWeekSessionCounts = new HashMap<>();
    }

    public int getMAX_SEATS() {
        return MAX_SEATS;
    }

    public String getCinemaName() {
        return cinemaName;
    }
    // some clumsy logic to limit only 5 sessions per day
    public void addSession(Session session) {
        if(sessions == null) sessions = new LinkedHashSet<>(); // lazy instantiate
        if(!sessions.contains(session)){
            if(dayOfWeekSessionCounts.containsKey(DayOfWeek.of(session.getSessionDay()))) {
                int daySessionCount = dayOfWeekSessionCounts.get(DayOfWeek.of(session.getSessionDay()));
                if (daySessionCount < SESSIONS_PER_DAY_LIMIT ) {
                    this.sessions.add(session);
                    dayOfWeekSessionCounts.replace(DayOfWeek.of(session.getSessionDay()), ++daySessionCount);
                }
            } else {
                dayOfWeekSessionCounts.put(DayOfWeek.of(session.getSessionDay()), 1);
                this.sessions.add(session);
            }
        }
    }

    public Set<Session> getSessions() {
        return sessions;
    }
}
