package model;

public class Movie {
    private String movieName;
    private MovieClassification movieClassification;

    public Movie(String movieName, String movieClassification) {
        this.movieName = movieName;
        this.movieClassification = MovieClassification.valueOf(movieClassification);
        String c = MovieClassification.values()[0].getClassification();
    }


}
