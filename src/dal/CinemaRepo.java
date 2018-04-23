package dal;

import assets.ConfigFileMissingException;
import assets.HelperFunctions;
import model.Cinema;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author dimz
 * @since 24/4/18.
 */
public class CinemaRepo implements ICinemaRepoDAL {

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
    public static ICinemaRepoDAL getInstance() {
        if (instance == null) {
            instance = new CinemaRepo();
        }
        return instance;
    }


    @Override
    public List<Cinema> getAllCinemas() {
        Set<List<String>> allFile = CSVUtils.getInstance().readAll(CINEMA_FILE);
        List<Cinema> allCinemas = new LinkedList<>();
        try {
            allFile.forEach(cinemaRow -> allCinemas.add(new Cinema(Integer.parseInt(cinemaRow.get(1)), cinemaRow.get(0))));
        } catch (Exception e) {
           e.printStackTrace();
        }
        return allCinemas;
    }
}
