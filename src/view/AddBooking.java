package view;

import controller.IController;
import controller.JMossBookingClerkController;

import java.util.Scanner;

/**
 * @author dimz
 * @since 2/5/18.
 */
public class AddBooking extends JMossView{

    private String custEmail;
    private Integer postCode;
    private IController controller;

    public AddBooking(IController controller) {
        this.controller = controller;
        buildMyContent();
    }

    @Override
    public String getInput() {
        switch (super.getInputInt()) {
            case 1: System.out.print("Email: ");
                custEmail = String.format("Customer email: %s\n",getEmailFromUserInput());
                postCode = getPostcodeFromUserInout();
                buildMyContent();
                return getInput();
            case 6: return "BookingClerkMainMenu";
            default: return "unknown";
        }
    }

    private String getEmailFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        String wrongEmailPatternError =  "\033[31mIncorrect email format. Try again";
        String custEmail = scanner.nextLine().trim();
        if (!custEmail.matches("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")){
            System.err.println(wrongEmailPatternError);
            custEmail = getEmailFromUserInput();
        }
        return custEmail;
    }

    private Integer getPostcodeFromUserInout(){
        Scanner scanner = new Scanner(System.in);
        String wrongPostcodeError = "\033[31mIncorrect Postocde format. Try again";
        System.out.print("Post code: ");
        Integer postCode;
        try {
            postCode = scanner.nextInt();
        } catch (Exception ex){
            // something went wrong, lets try again
            System.err.println(wrongPostcodeError);
            postCode = getPostcodeFromUserInout();
        }

        if(postCode.toString().length() != 4 ){
            System.err.println(wrongPostcodeError);
            postCode = getPostcodeFromUserInout();
        }
        return postCode;
    }

    private void buildMyContent(){
        String header =
                "                   ********************************************************************************\n" +
                        "                   ********************             Booking Status             ********************\n" +
                        "                   ********************************************************************************\n\n";
        String menuOptions = "\n\n1. Add customer details\n" +
                "2. Add A movie\n" +
                "6. Return to previous";

        int bookingNumber = ((JMossBookingClerkController) controller).getBookings().size() + 1;

        StringBuilder stringBuilder = new StringBuilder(header);
        stringBuilder.append(String.format("Booking number: %d\n", bookingNumber));
        if(custEmail != null) stringBuilder.append(custEmail);
        if(postCode != null) stringBuilder.append(String.format("Postcode: %d", postCode));
        stringBuilder.append(menuOptions);

        setMyContent(stringBuilder.toString());
    }


}
