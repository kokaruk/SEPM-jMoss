package assets;

import java.io.IOException;
import java.util.Properties;

/**
 * Interface for Config file Reader.
 *
 * @author dimz
 * @since 19/3/17
 */

public interface IConfigFileReader {
    /**
     * Get int from config file.
     *
     * @param myPropertyName property name string
     * @return parsed integer
     */
    int getConfigInt(String myPropertyName);

    /**
     * Get string from config file.
     *
     * @param myPropertyName property name string
     * @return string of property value
     * @throws ConfigFileMissingException if passed URI doesn't exist
     */
    String getConfigString(String myPropertyName) throws ConfigFileMissingException;

    /**
     * @return returns map of properties as Map of HashTable
     * @throws IOException if there are issues reading the file
     */
    Properties getProperties() throws IOException;
}
