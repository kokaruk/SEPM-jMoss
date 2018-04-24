package dal;

import model.Cinema;

import java.util.Map;

/**
 * @author dimz
 * @since 24/4/18.
 */
public interface ICinemaRepoDAL {
    // create
    // read
    Map<Integer, Cinema> getAllCinemas();
    // update
    // delete
}
