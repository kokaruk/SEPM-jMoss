package dal;

import assets.HelperFunctions;
import model.Cinema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Real implementation of Cinema repo. Interacts with Data Reader class
 * @author dimz
 * @since 24/4/18.
 */
final class CinemaRepo implements ICinemaRepoDAL {
    private static Logger logger = LogManager.getLogger();
    private final static String CINEMA_FILE;
    private final static int CINEPLEX_COUNT;

    static {
        String CINEMA_FILE1;
        int CINEPLEX_COUNT1;
        try {
            CINEMA_FILE1 = HelperFunctions.getConfigReader().getConfigString("CINEMA_FILE");
            CINEPLEX_COUNT1 = HelperFunctions.getConfigReader().getConfigInt("CINEPLEX_COUNT");
        } catch (IOException e){
            CINEMA_FILE1 = "";
            CINEPLEX_COUNT1= 0;
            logger.fatal(e.toString());
        }
        CINEPLEX_COUNT = CINEPLEX_COUNT1;
        CINEMA_FILE = CINEMA_FILE1;
    }


    // singleton instance
    private static ICinemaRepoDAL instance;

    //private constructor
    private CinemaRepo() {}

    // lazy instance constructor
     static ICinemaRepoDAL getInstance() {
        if (instance == null) {
            instance = new CinemaRepo();
        }
        return instance;
    }


    @Override
    public Map<Integer, Cinema> getAllCinemas() {
        Set<List<String>> allFile = CSVUtils.getInstance().readAll(CINEMA_FILE);
        Map<Integer, Cinema> allCinemas = new HashMap<>();
        try {
            allFile.stream().limit(CINEPLEX_COUNT).forEach(cinemaRow ->
                    allCinemas.put(Integer.parseInt(cinemaRow.get(0)),
                            new Cinema(cinemaRow.get(1),
                                    Integer.parseInt(cinemaRow.get(2)))));
        } catch (Exception e) {
           e.printStackTrace();
        }
        return allCinemas;
    }
}
