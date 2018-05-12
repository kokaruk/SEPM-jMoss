package view;
/**
 * @author Calvin and Dimi
 */

import controller.IController;
import controller.JMossBookingClerkController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BookingClerkMainMenu extends JMossView {

    public BookingClerkMainMenu(IController controller) {
        super(controller);
    }

    public String getInput() {
        switch (getInputInt()) {
            case 1: return "AllMoviesSessions";
            case 3: return "CineplexSearch";
            case 4: return "AddBooking";
            case 6: return "exit";
            default: return "unknown";
        }
    }

    @Override
    void buildMyContent() {
        String greetingsPrefix = "\t\t\t********************************************************************************\n";

        String greetingsPostfix = "\t\t\t********       Welcome to movie search and booking system.  ********************\n";


        String menuOptions = "1. See All Movies\n" +
                "2. Find A Movie\n" +
                "3. Cinemas\n" +
                "4. Add Booking\n" +
                "5. Find / Delete Booking\n" +
                "6. Exit";
        String userName = ((JMossBookingClerkController)controller).getUser().getUserName();
        String pathToAsciFile = "assets/ascii_art.txt";
        File asciArtFile = new File(pathToAsciFile);
        // check if running from IDE or console as relative path will differ
        if (!asciArtFile.exists()) pathToAsciFile = "src/" + pathToAsciFile;

        try {
            // int 36 length of stars left and right
            String userNameGreetingPrefix = String.format("\t\t\t********       %1$-" + (greetingsPrefix.length() - userName.length() - 36) + "s ******************** \n",
                    userName.toUpperCase());
            String asciArt = new String(Files.readAllBytes(Paths.get(pathToAsciFile)));
            String menuText = asciArt + "\n" +
                    greetingsPrefix  +
                    userNameGreetingPrefix +
                    greetingsPostfix +
                    greetingsPrefix + "\n" +
                    menuOptions;


            myContent = menuText;

        } catch (IOException ex) {
            System.out.println("Asset header not found");
        }
    }
}
