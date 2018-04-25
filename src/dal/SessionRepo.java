package dal;

import assets.ConfigFileMissingException;
import assets.HelperFunctions;
import model.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author dimz
 * @since 25/4/18.
 */
public class SessionRepo implements ISessionRepoDAL {

    private final static String SESSION_FILE;
    static {
        String SESSION_FILE1;
        try {
            SESSION_FILE1 = HelperFunctions.getConfigReader().getConfigString("SESSION_FILE");
        } catch (ConfigFileMissingException e){
            SESSION_FILE1 = "";
        }
        SESSION_FILE = SESSION_FILE1;
    }


    // singleton instance
    private static ISessionRepoDAL instance;

    //private constructor
    private SessionRepo() {}

    // lazy instance constructor
    public static ISessionRepoDAL getInstance() {
        if (instance == null) {
            instance = new SessionRepo();
        }
        return instance;
    }


    @Override
    public Map<Integer, Session> getAllSessions() {
        Set<List<String>> allFile = CSVUtils.getInstance().readAll(SESSION_FILE);
        Map<Integer, Session> allSessions = new HashMap<>();
        try {
            allFile.forEach(sessionRow ->
                    allSessions.put(Integer.parseInt(sessionRow.get(0)),
                            new Session(Integer.parseInt(sessionRow.get(1)),
                                        Integer.parseInt(sessionRow.get(2)),
                                    Integer.parseInt(sessionRow.get(3)),
                                    Integer.parseInt(sessionRow.get(4))
                                    )));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allSessions;
    }
}
