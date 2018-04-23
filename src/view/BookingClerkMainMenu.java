package view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BookingClerkMainMenu extends JMossView {

    public BookingClerkMainMenu() {
        String pathToAsciFile = "assets/ascii_art.txt";
        File asciArtFile = new File(pathToAsciFile);
        // check if running from IDE or console as relative path will differ
        if (!asciArtFile.exists()) pathToAsciFile = "src/" + pathToAsciFile;

        try {
            String asciArt = new String(Files.readAllBytes(Paths.get(pathToAsciFile)));
            setMyContent(asciArt);
        } catch (IOException ex) {
            System.out.println("Asset header not found");
        }


    }
}
