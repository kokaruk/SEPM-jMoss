package dal;

import model.Movie;

import java.util.Map;

public interface IMovieRepoDAL {
    // create
    // read
    Map<Integer, Movie> getAllMovies();
    // update
    // delete
}
