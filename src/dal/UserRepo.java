package dal;

import assets.HelperFunctions;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Real implementation of Users repo. Interacts with Data Reader class
 * @author dimz
 * @since 21/4/18.
 */
final class UserRepo implements IUserRepoDAL {
    private final static String USERS_FILE;
    private static Logger logger = LogManager.getLogger();
    static {
        String USERS_FILE1;
        try {
            USERS_FILE1 = HelperFunctions.getConfigReader().getConfigString("USERS_FILE");
        } catch (IOException e) {
            logger.fatal(e.toString());
            USERS_FILE1 = "";
        }
        USERS_FILE = USERS_FILE1;
    }


    // singleton instance
    private static IUserRepoDAL instance;

    //private constructor
    private UserRepo() {}

    // lazy instance constructor
    static IUserRepoDAL getInstance() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }

     public User getUser(String username, String password)
            throws ClassNotFoundException,
            InstantiationException,
            IllegalAccessException,
            NoSuchMethodException,
            InvocationTargetException {
        List<String> userLine = CSVUtils.getInstance().readAndSearch(USERS_FILE, username);
        User aUser = null;
        if (userLine.size() > 0 && userLine.get(1).equals(username) && userLine.get(2).equals(password)){
            String type = "model." + userLine.get(3);
            Class<?> userClass = Class.forName(type);
            Constructor<?> userConstructor = userClass.getConstructor(int.class, String.class);
            aUser = (User) userConstructor.newInstance(Integer.parseInt(userLine.get(0)), username);
        }
        return aUser;
    }
}
