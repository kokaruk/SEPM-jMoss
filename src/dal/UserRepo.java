package dal;

import assets.ConfigFileMissingException;
import assets.HelperFunctions;
import model.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author dimz
 * @since 21/4/18.
 */
final class UserRepo implements IUserRepoDAL {
    private final static String USERS_FILE;
    static {
        String USERS_FILE1;
        try {
            USERS_FILE1 = HelperFunctions.getConfigReader().getConfigString("USERS_FILE");
        } catch (ConfigFileMissingException e) {
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
        if (userLine.size() > 0 && userLine.contains(username) && userLine.contains(password)){
            String type = "model." + userLine.get(3);
            Class<?> userClass = Class.forName(type);
            Constructor<?> userConstructor = userClass.getConstructor(int.class, String.class);
            aUser = (User) userConstructor.newInstance(Integer.parseInt(userLine.get(0)), username);
        }
        return aUser;
    }
}
