package dal;

import assets.HelperFunctions;
import model.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Real implementation of Movie repo. Interacts with Data Reader class
 */
final class MovieRepo implements IMovieRepoDAL {
    private static Logger logger = LogManager.getLogger();
    private final static String MOVIE_FILE;

    static {
        String MOVIE_FILE1;
        try {
            MOVIE_FILE1 = HelperFunctions.getConfigReader().getConfigString("MOVIE_FILE");
        } catch (IOException e){
            MOVIE_FILE1 = "";
            logger.fatal(e.toString());
        }
        MOVIE_FILE = MOVIE_FILE1;
    }


    // singleton instance
    private static IMovieRepoDAL instance;

    //private constructor
    private MovieRepo() {}

    // lazy instance constructor
    static IMovieRepoDAL getInstance() {
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
