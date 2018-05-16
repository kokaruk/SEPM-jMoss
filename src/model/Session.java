package model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author dimz
 * @since 24/4/18.
 */
public class Session {
    private final Integer id;
    private final int sessionDay;
    private final int sessionTime;
    private final int movieId;
    private final int cinemaId;
    private Movie movie;
    private Cinema cinema;
    private int availableSeats;
    private List<Booking> bookingList;

    public Session(int sessionId, int sessionDay, int sessionTime, int movieId, int cinemaId) {
        id = sessionId;
        this.sessionDay = sessionDay;
        this.sessionTime = sessionTime;
        this.movieId = movieId;
        this.cinemaId = cinemaId;
        this.bookingList = new LinkedList<>();
    }

    //////// setters ////////
    public void setMovie(Movie movie) {
        this.movie = movie;
        movie.addSession(this);
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
        availableSeats = cinema.getMAX_SEATS();
        cinema.addSession(this);
    }

    //////// getters ////////
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


    public Cinema getCinema() {
        return cinema;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public Integer getId() {
        return id;
    }

    public void addBooking(Booking booking) throws IndexOutOfBoundsException {
        int bookingSeats = sessionBookingSeats(booking);
        addBooking(booking, bookingSeats);
    }

    public void addBooking(Booking booking, int bookingSeats){
        if (bookingSeats <= availableSeats && availableSeats > 0){
            bookingList.add(booking);
            availableSeats -= bookingSeats;
        } else {
            throw new IndexOutOfBoundsException("The session is booked out");
        }
    }

    public void removeBooking(Booking booking, int bookingSeats){
        if (bookingList.contains(booking)){
            bookingList.remove(booking);
            availableSeats += bookingSeats;
        }
    }


    /**
     * get booked seats count from booking
     * @param booking booking with booking lines
     * @return taken seats
     */
    private Integer sessionBookingSeats(Booking booking){
        for(Map.Entry<Integer, Booking.Tuple3<Session, Integer, Date>> bookingLine : booking.getBookingLines().entrySet()){
            if(bookingLine.getValue().getSession() == this){
                return bookingLine.getValue().getSeatsBooked();
            }
        }
        return 0;
    }

}
