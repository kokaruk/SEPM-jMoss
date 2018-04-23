package view;
/**
 * @author Calvin and Dimi
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BookingClerkMainMenu extends JMossView {

    private String greetingsPrefix = "\t\t\t********************************************************************************\n";

    private String greetingsPostfix = "\t\t\t********       Welcome to movie search and booking system.  ********************\n";


    private String MenuOptions = "1. See All Movies\n" +
                                "2. Find A Movie\n" +
                                "3. Cinemas\n" +
                                "4. Add Booking\n" +
                                "5. Delete Booking\n" +
                                "6. Exit";

    public BookingClerkMainMenu(String userName) {
        String pathToAsciFile = "assets/ascii_art.txt";
        File asciArtFile = new File(pathToAsciFile);
        // check if running from IDE or console as relative path will differ
        if (!asciArtFile.exists()) pathToAsciFile = "src/" + pathToAsciFile;

        try {

            String userNameGreetingPrefix = String.format("\t\t\t********       %1$-" + (greetingsPrefix.length() - userName.length() - 36) + "s ******************** \n",
                    userName.toUpperCase()); // int 23 length of stars left and right

            String asciArt = new String(Files.readAllBytes(Paths.get(pathToAsciFile)));
            String menuText = asciArt + "\n" +
                            greetingsPrefix  +
                            userNameGreetingPrefix +
                            greetingsPostfix +
                            greetingsPrefix + "\n" +
                            MenuOptions;


            setMyContent(menuText);

        } catch (IOException ex) {
            System.out.println("Asset header not found");
        }


    }
}
