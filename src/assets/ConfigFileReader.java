package assets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * the class reads properties from config file
 * @author dimz
 * @since 21/4/18.
 */
final class ConfigFileReader implements IConfigFileReader {

    private static Logger logger = LogManager.getLogger();

    // assuming there is only one config file, hardcode it in here
    private static final String CONFIG_FILE = "jMoss.properties";

    // singleton instance
    private static ConfigFileReader instance;

    //private constructor
    private ConfigFileReader() {}

    // lazy instance constructor
    static ConfigFileReader getInstance() {
        if (instance == null) {
            instance = new ConfigFileReader();
        }
        return instance;
    }

    /**
     * Get int from config file. Interface implementation.
     *
     * @param myPropertyName property name string
     * @return integer from
     */
    @Override
    public int getConfigInt(String myPropertyName) {
        int configInt = 0;
        try {
            configInt = Integer.parseInt(getConfigString(myPropertyName));
        } catch (NumberFormatException e) {
            logger.fatal(String.format("%s\r\nInvalid string to int casting format. Exiting ...", e.getMessage()));
            System.err.println();
            System.err.println(e.toString());
            System.exit(0);
        } catch (IOException err) {
            logger.fatal(err.getMessage());
            System.exit(0);
        }
        return configInt;
    }

    /**
     * Get string from config file. Interface implementation.
     *
     * @param myPropertyName property name string
     * @return string of property value
     * @throws ConfigFileMissingException if passed URI doesn't exist
     */
    @Override
    public String getConfigString(String myPropertyName) throws IOException {
        Properties myProp = new Properties();

        String myPropertyString;
        try (InputStream in = getClass().getResourceAsStream(CONFIG_FILE)) {
            myProp.load(in);
            myPropertyString = myProp.getProperty(myPropertyName);
        } catch (IOException ex) {
            logger.fatal(ex.getMessage());
            throw new ConfigFileMissingException(String.format("Unknown issue accessing config file. See if %s exist ",
                    CONFIG_FILE));

        } catch (Exception ex){
            logger.fatal(ex.toString());
            throw ex;
        }
        return myPropertyString;
    }


    /**
     * @return returns map of properties as Map of HashTable
     * @throws IOException if there are issues reading the file
     */
    @Override
    public Properties getProperties() throws IOException {
        Properties myProp = new Properties();

        try (InputStream in = getClass().getResourceAsStream(CONFIG_FILE)) {
            myProp.load(in);
        }
        return myProp;
    }


}
