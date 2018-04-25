package model;

import java.util.LinkedHashSet;
import java.util.Set;

public class Movie {
    private String movieName;
    private MovieClassification movieClassification;
    private Set<Session> sessions;

    public Movie(String movieName, String movieClassification) {
        this.movieName = movieName;
        this.movieClassification = MovieClassification.valueOf(movieClassification);
    }

     void addSession(Session session) {
        if(sessions == null) sessions = new LinkedHashSet<>(); // lazy instantiate
        this.sessions.add(session);
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieClassification() {
        return movieClassification.getClassification();
    }
}
