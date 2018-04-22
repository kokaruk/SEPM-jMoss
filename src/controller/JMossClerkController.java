package controller;

import view.JMossView;

/**
 * @author Calvin
 */
public class JMossClerkController implements IController {

    private JMossView myView;

    public JMossClerkController(JMossView myView) {
        this.myView = myView;
    }

    @Override
    public void start() {
        myView.displayContent();
    }
}
