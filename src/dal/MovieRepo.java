package dal;

import assets.ConfigFileMissingException;
import assets.HelperFunctions;
import model.Movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MovieRepo implements IMovieRepoDAL {


    private final static String MOVIE_FILE;
    static {
        String MOVIE_FILE1;
        try {
            MOVIE_FILE1 = HelperFunctions.getConfigReader().getConfigString("MOVIE_FILE");
        } catch (ConfigFileMissingException e){
            MOVIE_FILE1 = "";
        }
        MOVIE_FILE = MOVIE_FILE1;
    }


    // singleton instance
    private static IMovieRepoDAL instance;

    //private constructor
    private MovieRepo() {}

    // lazy instance constructor
    public static IMovieRepoDAL getInstance() {
        if (instance == null) {
            instance = new MovieRepo();
        }
        return instance;
    }


    @Override
    public Map<Integer, Movie> getAllMovies() {
        Set<List<String>> allFile = CSVUtils.getInstance().readAll(MOVIE_FILE);
        Map<Integer, Movie> allMovies = new HashMap<>();
        try {
            allFile.forEach(movieRow ->
                    allMovies.put(Integer.parseInt(movieRow.get(0)),
                            new Movie(movieRow.get(1),
                                    movieRow.get(2))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allMovies;
    }
}
