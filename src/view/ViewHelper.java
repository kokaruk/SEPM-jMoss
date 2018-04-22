package view;

/**
 * static vew helper snippets
 * @author dimz
 * @since 21/4/18.
 */
public class ViewHelper {

    // no instance please
    private ViewHelper(){}

    public static void clearScreen() {
        final String ANSI_CLS = "\u001b[2J";
        final String ANSI_HOME = "\u001b[H";
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.flush();
    }

}
