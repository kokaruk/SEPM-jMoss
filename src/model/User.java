package model;

/**
 * @author dimz
 * @since 21/4/18.
 */
public abstract class User {
    private final int userId;
    private final String userName;

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
