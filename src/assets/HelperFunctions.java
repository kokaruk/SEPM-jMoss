package assets;

/**
 * collection of static helper functions
 * @author dimz
 * @since 21/4/18.
 */
public final class HelperFunctions {
    static private IConfigFileReader propertyReader;

    /**
     * Factory method for properties file reader
     *
     * @return Singleton instance, implementation of  IConfigReader Interface
     */
    public static IConfigFileReader getConfigReader()  {
        if (propertyReader == null){
            setCustomReader(ConfigFileReader.getInstance());
        }
        return propertyReader;
    }

    /**
     * Method to Override instance of properties reader, used for UnitTest dependency injections
     * In reality should be removed from production code
     *
     * @param propertyReader_Fake expects a fake implementation of properties reader interface.
     */
    static void setCustomReader(IConfigFileReader propertyReader_Fake) {
        HelperFunctions.propertyReader = propertyReader_Fake;
    }


}
