//
//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//
//            bless your code to be bug free


import controller.IController;
import controller.JMossLoginController;
import dal.BookingPurger;
import view.JMossView;
import view.LoginView;

import java.io.IOException;

public class jMoss {
    public static void main(String[] args) throws IOException {
        BookingPurger purger = new BookingPurger();
        purger.start();

        JMossView loginView = new LoginView();
        IController controller = new JMossLoginController(loginView);
        controller.start();
    }
}