package assets;

import java.io.FileNotFoundException;

import static view.ViewHelper.ANSI_RED;
import static view.ViewHelper.ANSI_RESET;

/**
 * Thrown if config *.properties file is missing.
 *
 * @author dimz
 * @version 2.0
 * @since 1/4/17
 */
public class ConfigFileMissingException extends FileNotFoundException {

    ConfigFileMissingException(String missingFile) {
        super(String.format("%sConfig file %s doesn't exist " +
                "make sure to transfer to compile folder from sources%s", ANSI_RED, missingFile, ANSI_RESET));
    }
}
