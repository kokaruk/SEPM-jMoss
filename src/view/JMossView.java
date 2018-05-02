package view;

import java.util.Scanner;

/**
 * @author dimz
 * @since 21/4/18.
 */
public abstract class JMossView {

    private String myContent;
    private boolean error;

    void wrongInput() {
        System.err.println("\033[31mThis input value is not allowed\033[37m");
    }

    void setMyContent(String myContent) {
        this.myContent = myContent;
    }

    void displayContent(){
        System.out.println(myContent);
    }

    /**
     * gets input from user
     * @return corresponding values based on user input
     */
    public abstract String getInput();

    /**
     * get input integer
     */
    int getInputInt(){
        initInput();
        Scanner scanner = new Scanner(System.in);
        Integer option = 0;
        try {
            option = scanner.nextInt();
        } catch (Exception e) {
            setError(true);
        }
        return option;
    }

    /**
     * init user input screen
     */
    void initInput(){
        ViewHelper.clearScreen();
        if(isError()){
            displayContent();
            wrongInput();
            setError(false);
        } else {
            displayContent();
        }
    }


    /**
     * error. set it to true if calling to get input did not produce required result
     */
    public void setError(boolean error) {
        this.error = error;
    }

    boolean isError() {
        return error;
    }


}
