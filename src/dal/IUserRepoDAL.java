package dal;

import model.User;

import java.lang.reflect.InvocationTargetException;

/**
 * @author dimz
 * @since 21/4/18.
 */
public interface IUserRepoDAL {

    // create
    // read
    User getUser(String username, String password) throws ClassNotFoundException,
            InstantiationException,
            IllegalAccessException,
            NoSuchMethodException,
            InvocationTargetException;
    // update
    // delete

}
