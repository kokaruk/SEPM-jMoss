package dal;

import model.Session;

import java.util.Map;

/**
 * @author dimz
 * @since 25/4/18.
 */
public interface ISessionRepoDAL {
    // create
    // read
    Map<Integer, Session> getAllSessions();
    // update
    // delete
}
