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
import java.util.LinkedHashMap;
import java.util.Map;

import static view.ViewHelper.ANSI_RESET;

public class BookingClerkMainMenu extends JMossView {
    private Map<Integer, String[]> menuOptionsMap;

    public BookingClerkMainMenu(IController controller) {
        super(controller);
    }



    public String getInput() {
            initInput();
            return menuOptionsMap.get(
                    getInputIntWithBound(menuOptionsMap.size())
            )[1];
    }

    @Override
    void buildMyContent() {
        System.out.print(ANSI_RESET);
        menuOptionsMap = new LinkedHashMap<>();
        String greetingsPrefix = "\t\t\t********************************************************************************\n";

        String greetingsPostfix = "\t\t\t********       Welcome to movie search and booking system.  ********************\n";

        int menuOptionsCounter = 0;

        menuOptionsMap.put(++menuOptionsCounter, new String[]{"See All Movies", "AllMoviesSessions"});
        menuOptionsMap.put(++menuOptionsCounter, new String[]{"Cinemas", "CineplexSearch"});
        menuOptionsMap.put(++menuOptionsCounter, new String[]{"Add Booking", "AddBooking"});
        menuOptionsMap.put(++menuOptionsCounter, new String[]{"Find / Delete Booking", "BookingViewDelelete"});
        menuOptionsMap.put(++menuOptionsCounter, new String[]{"Exit", "exit"});

        StringBuilder menuOptionsBuilder = new StringBuilder();

        for(Map.Entry<Integer, String[]> menuOption : menuOptionsMap.entrySet()){
            menuOptionsBuilder.append(String.format("%d. ", menuOption.getKey()));
            menuOptionsBuilder.append(String.format("%s\n",menuOption.getValue()[0]));
        }

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
            myContent = asciArt + "\n" +
                    greetingsPrefix  +
                    userNameGreetingPrefix +
                    greetingsPostfix +
                    greetingsPrefix + "\n" +
                    menuOptionsBuilder.toString();

        } catch (IOException ex) {
            System.out.println("Asset header not found");
        }
    }
}
