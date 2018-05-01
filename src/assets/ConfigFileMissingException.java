package assets;

import java.io.FileNotFoundException;

/**
 * Thrown if config *.properties file is missing.
 *
 * @author dimz
 * @version 2.0
 * @since 1/4/17
 */
public class ConfigFileMissingException extends FileNotFoundException {

    ConfigFileMissingException(String missingFile) {
        super(String.format("\033[31mConfig file %s doesn't exist " +
                "make sure to transfer to compile folder from sources", missingFile));
    }
}
