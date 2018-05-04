package model;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String movieName;
    private MovieClassification movieClassification;
    private List<Session> sessions;

    public Movie(String movieName, String movieClassification) {
        this.movieName = movieName;
        this.movieClassification = MovieClassification.valueOf(movieClassification);
    }

     void addSession(Session session) {
        if(sessions == null) sessions = new ArrayList<>(); // lazy instantiate
        this.sessions.add(session);
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieClassification() {
        return movieClassification.getClassification();
    }

    public List<Session> getSessions() {
        return sessions;
    }
}
