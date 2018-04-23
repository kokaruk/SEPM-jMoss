package view;
/**
 * @author Calvin and Dimi
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BookingClerkMainMenu extends JMossView {

    public String MenuOptions = "1. See All Movies\n" +
                                "2. Find A Movie\n" +
                                "3. Cinemas\n" +
                                "4.  Add Booking\n" +
                                "5. Delete Booking\n";

    public BookingClerkMainMenu() {
        String pathToAsciFile = "assets/ascii_art.txt";
        File asciArtFile = new File(pathToAsciFile);
        // check if running from IDE or console as relative path will differ
        if (!asciArtFile.exists()) pathToAsciFile = "src/" + pathToAsciFile;

        try {
            String asciArt = new String(Files.readAllBytes(Paths.get(pathToAsciFile)));
            String MenuText = asciArt + "\n" + MenuOptions;
            setMyContent(MenuText);

        } catch (IOException ex) {
            System.out.println("Asset header not found");
        }


    }
}
