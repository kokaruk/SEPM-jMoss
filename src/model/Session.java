package model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author dimz
 * @since 24/4/18.
 */
public class Session {
    private final int sessionDay;
    private final int sessionTime;
    private final int movieId;
    private final int cinemaId;
    private Movie movie;
    private Cinema cinema;
    private List<Booking> bookingList;

    public Session(int sessionDay, int sessionTime, int movieId, int cinemaId) {
        this.sessionDay = sessionDay;
        this.sessionTime = sessionTime;
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.bookingList = new LinkedList<>();
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        movie.addSession(this);
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
        cinema.addSession(this);
    }

    public int getMovieId() {
        return movieId;
    }

    public int getCinemaId() {
        return cinemaId;
    }

    public int getSessionDay() {
        return sessionDay;
    }

    public int getSessionTime() {
        return sessionTime;
    }

    public Movie getMovie() {
        return movie;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void addBooking(Booking booking) throws IndexOutOfBoundsException {
        if (bookingList.size() < cinema.getMAX_SEATS()){
            bookingList.add(booking);
        } else {
            throw new IndexOutOfBoundsException("The session is booked out");
        }
    }

}
