package view;

/**
 * @author dimz
 * @since 21/4/18.
 */
public abstract class JMossView {
    private String myContent;

    JMossView(){
        ViewHelper.clearScreen();
    }

    void setMyContent(String myContent) {
        this.myContent = myContent;
    }

    public void displayContent(){
        System.out.println(myContent);
    }

}
