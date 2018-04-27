package dal;

import assets.ConfigFileMissingException;
import assets.HelperFunctions;
import model.Cinema;

import java.util.*;

/**
 * @author dimz
 * @since 24/4/18.
 */
final class CinemaRepo implements ICinemaRepoDAL {

    private final static String CINEMA_FILE;
    static {
        String CINEMA_FILE1;
        try {
            CINEMA_FILE1 = HelperFunctions.getConfigReader().getConfigString("CINEMA_FILE");
        } catch (ConfigFileMissingException e){
            CINEMA_FILE1 = "";
        }
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
            allFile.forEach(cinemaRow ->
                    allCinemas.put(Integer.parseInt(cinemaRow.get(0)),
                            new Cinema(cinemaRow.get(1),
                                    Integer.parseInt(cinemaRow.get(2)))));
        } catch (Exception e) {
           e.printStackTrace();
        }
        return allCinemas;
    }
}
