package getCreditScore;

import com.rabbitmq.client.Channel;
import com.google.gson.Gson;

import entities.Message;
import config.RabbitConnection;
import config.Constants;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Luke
 */
public class GetCreditScore {

    private final static String SENDING_QUEUE = Constants.SENDING_GET_CREDIT_SCORE;
    private final static Scanner SCANNER = new Scanner(System.in);
    private static long ssn;
    private static int loanDuration;
    private static double loanAmount;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.util.concurrent.TimeoutException
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel channel = rabbitConnection.makeConnection();

        // Get data from user - Client
        getInputData();

        Gson gson = new Gson();
        String message = gson.toJson(new Message(ssn, creditScore(String.valueOf(ssn)), loanAmount, loanDuration), Message.class);

        channel.queueDeclare(SENDING_QUEUE, false, false, false, null);
        channel.basicPublish("", SENDING_QUEUE, null, message.getBytes());
        System.out.println(" [x] Sent: " + message);

        rabbitConnection.closeChannelAndConnection();
    }

    private static void getInputData() {
        System.out.println("SSN:");
        ssn = validateSSN(SCANNER.next());
        System.out.println("");
        System.out.println("Loan amount:");
        loanAmount = validateLoanAmount(SCANNER.next());
        System.out.println("");
        System.out.println("Loan duration (days):");
        loanDuration = Integer.parseInt(SCANNER.next());
        System.out.println("");
    }

    private static long validateSSN(String ssn) {
        String ssnAsString = String.valueOf(ssn);

        while (true) {
            if (ssnAsString.length() == 10) {
                break;
//            } else if (ssn.length() == 10) {
//                SSnAsString = SSnAsString.substring(0, 6) + '-' + SSnAsString.substring(6, SSnAsString.length());
//                break; 
            } else if (ssnAsString.matches("[0-9]+") && (ssnAsString.length() > 10 || ssnAsString.length() < 10)) {
                System.out.println("Incorrect amount of digits. Try again.");
                System.out.println("SSN:");
                return validateSSN(SCANNER.next());
            } else {
                System.out.println("Incorrect SSN. Try again.");
                System.out.println("SSN:");
                return validateSSN(SCANNER.next());
            }
        }
        return Long.parseLong(ssnAsString);
    }

    private static double validateLoanAmount(String loanAmount) {
        while (true) {
            // loanAmount can be just a dot or just a comma
            if (!(loanAmount.equals(".") || loanAmount.equals(","))) {
                // it can contain numbers and dots and commas
                if (loanAmount.matches("[0-9,.]+")) {
                    Double result = Double.parseDouble(loanAmount.replace(",", "."));
                    // result cant be less than 0
                    if (result > 0) {
                        return result;
                    }
                }
            }
            System.out.println("Incorrect loan amount. Try again.");
            System.out.println("Loan amount");
            return validateLoanAmount(SCANNER.next());
        }
    }

    private static int creditScore(java.lang.String ssn) {
        ssn = ssn.substring(0, 6) + '-' + ssn.substring(6, ssn.length());
        org.bank.credit.web.service.CreditScoreService_Service service = new org.bank.credit.web.service.CreditScoreService_Service();
        org.bank.credit.web.service.CreditScoreService port = service.getCreditScoreServicePort();
        return port.creditScore(ssn);
    }
}
