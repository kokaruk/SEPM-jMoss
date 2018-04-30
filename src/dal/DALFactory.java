package dal;

/**
 * @author dimz
 * @since 27/4/18.
 */
public final class DALFactory {
    static private IUserRepoDAL userRepoDAL;
    static private ICinemaRepoDAL cinemaRepoDAL;
    static private IMovieRepoDAL movieRepoDAL;
    static private ISessionRepoDAL sessionRepoDAL;
    static private IBookingRepoDAL bookingRepoDAL;

    /**
     * Factory method for repo implementation
     *
     * @return Singleton instance, implementation of  IUserRepoDAL Interface
     */
    public static IUserRepoDAL getUserRepoDAL() {
        if(userRepoDAL==null){
            setUserRepoDAL(UserRepo.getInstance());
        }
        return userRepoDAL;
    }

    /**
     * Method to Override instance of properties reader, used for UnitTest dependency injections
     * In reality should be removed from production code
     *
     * @param userRepoDAL_Fake expects a fake implementation of properties reader interface.
     */
    static void setUserRepoDAL(IUserRepoDAL userRepoDAL_Fake) {
        DALFactory.userRepoDAL = userRepoDAL_Fake;
    }

    public static ICinemaRepoDAL getCinemaRepoDAL() {
        if (cinemaRepoDAL == null){
            setCinemaRepoDAL(CinemaRepo.getInstance());
        }
        return cinemaRepoDAL;
    }

    static void setCinemaRepoDAL(ICinemaRepoDAL cinemaRepoDAL_Fake) {
        DALFactory.cinemaRepoDAL = cinemaRepoDAL_Fake;
    }

    public static IMovieRepoDAL getMovieRepoDAL() {
        if (movieRepoDAL ==null){
            setMovieRepoDAL(MovieRepo.getInstance());
        }
        return movieRepoDAL;
    }

    public static void setMovieRepoDAL(IMovieRepoDAL movieRepoDAL) {
        DALFactory.movieRepoDAL = movieRepoDAL;
    }

    public static ISessionRepoDAL getSessionRepoDAL() {
        if(sessionRepoDAL == null){
            setSessionRepoDAL(SessionRepo.getInstance());
        }
        return sessionRepoDAL;
    }

    public static void setSessionRepoDAL(ISessionRepoDAL sessionRepoDAL) {
        DALFactory.sessionRepoDAL = sessionRepoDAL;
    }

    public static IBookingRepoDAL getBookingRepoDAL() {
        if( bookingRepoDAL == null){
          setBookingRepoDAL(BookingRepo.getInstance());
        }
        return bookingRepoDAL;
    }

    public static void setBookingRepoDAL(IBookingRepoDAL bookingRepoDAL) {
        DALFactory.bookingRepoDAL = bookingRepoDAL;
    }
}