package view;

/**
 * static vew helper snippets
 * @author dimz
 * @since 21/4/18.
 */
public class ViewHelper {

    public static final String ANSI_RESET;
    static final String ANSI_BLACK;
    public static final String ANSI_RED;
    static final String ANSI_GREEN;
    static final String ANSI_YELLOW;
    static final String ANSI_BLUE;
    static final String ANSI_PURPLE;
    static final String ANSI_CYAN;
    static final String ANSI_WHITE;

    static{
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isMacOs = osName.startsWith("mac os x");
        if(isMacOs){
            ANSI_RESET = "\u001B[0m";
            ANSI_BLACK = "\u001B[30m";
            ANSI_RED = "\u001B[31m";
            ANSI_GREEN = "\u001B[32m";
            ANSI_YELLOW = "\u001B[33m";
            ANSI_BLUE = "\u001B[34m";
            ANSI_PURPLE = "\u001B[35m";
            ANSI_CYAN = "\u001B[36m";
            ANSI_WHITE = "\u001B[37m";
        } else {
            ANSI_RESET = "";
            ANSI_BLACK = "";
            ANSI_RED = "";
            ANSI_GREEN = "";
            ANSI_YELLOW = "";
            ANSI_BLUE = "";
            ANSI_PURPLE = "";
            ANSI_CYAN = "";
            ANSI_WHITE = "";
        }

    }


    // no instance please
    private ViewHelper(){}

    public static void clearScreen() {
        final String ANSI_CLS = "\u001b[2J";
        final String ANSI_HOME = "\u001b[H";
        System.out.print(ANSI_CLS + ANSI_HOME);
        System.out.flush();
        System.out.print(ANSI_RESET);
    }
}
