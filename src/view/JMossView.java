package view;

/**
 * @author dimz
 * @since 21/4/18.
 */
public abstract class JMossView {

    private String myContent;

    public void wrongInput(){
        System.err.println("No such option.Try again");
    }

    void setMyContent(String myContent) {
        this.myContent = myContent;
    }

    void displayContent(){
        ViewHelper.clearScreen();
        System.out.println(myContent);
    }

    public abstract String getInput();

}
