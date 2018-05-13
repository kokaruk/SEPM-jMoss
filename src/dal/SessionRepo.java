package dal;

import assets.HelperFunctions;
import model.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Real implementation of Sessions repo. Interacts with Data Reader class
 * @author dimz
 * @since 25/4/18.
 */
final class SessionRepo implements ISessionRepoDAL {
    private final static String SESSION_FILE;
    private static Logger logger = LogManager.getLogger();

    static {
        String SESSION_FILE1;
        try {
            SESSION_FILE1 = HelperFunctions.getConfigReader().getConfigString("SESSION_FILE");
        } catch (IOException e){
            logger.fatal(e.toString());
            SESSION_FILE1 = "";
        }
        SESSION_FILE = SESSION_FILE1;
    }


    // singleton instance
    private static ISessionRepoDAL instance;

    //private constructor
    private SessionRepo() {}

    // lazy instance constructor
    static synchronized ISessionRepoDAL getInstance() {
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
                            new Session(Integer.parseInt(sessionRow.get(0)),
                                        Integer.parseInt(sessionRow.get(1)),
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
