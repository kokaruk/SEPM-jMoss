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
public class UserRepo implements IUserRepoDAL {
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
    public static IUserRepoDAL getInstance() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }

     @SuppressWarnings("unchecked")
     public User getUser(String username, String password)
            throws ClassNotFoundException,
            InstantiationException,
            IllegalAccessException,
            NoSuchMethodException,
            InvocationTargetException {
        List<String> userLIne = CSVUtils.getInstance().readAndSearch(USERS_FILE, username);
        User aUser = null;
        if (userLIne.size() > 0 && userLIne.contains(username) && userLIne.contains(password)){
            String type = "model." + userLIne.get(3);
            Class userClass = Class.forName(type);
            //noinspection unchecked
            Constructor userConstructor = userClass.getDeclaredConstructor(int.class, String.class);
            aUser = (User) userConstructor.newInstance(Integer.parseInt(userLIne.get(0)), username);
        }
        return aUser;
    }
}
