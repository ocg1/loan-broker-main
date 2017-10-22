package getBanks;

import com.google.gson.Gson;
import com.rabbitmq.client.*;

import entities.Message;
import config.RabbitConnection;
import config.Constants;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author williambech
 */
public class GetBanks {

    private final static String LISTENING_QUEUE = Constants.LISTENING_GET_BANKS;
    private final static String SENDING_QUEUE = Constants.SENDING_GET_BANKS;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.util.concurrent.TimeoutException
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel consumerChannel = rabbitConnection.makeConnection();
        Channel producerChannel = rabbitConnection.makeConnection();

        consumerChannel.queueDeclare(LISTENING_QUEUE, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(consumerChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                String data = new String(body, "UTF-8");
                System.out.println(" [x] Received: " + data);

                Gson gson = new Gson();
                Message message = gson.fromJson(data, Message.class);

                String[] banks = getBanks(
                    message.getSsn(),
                    message.getCreditScore(),
                    message.getLoanAmount(),
                    message.getLoanDuration()
                );
                message.setBanks(banks);

                // Previous message enriched with an array of banks available
                String newMessage = gson.toJson(message, Message.class);

                producerChannel.queueDeclare(SENDING_QUEUE, false, false, false, null);
                producerChannel.basicPublish("", SENDING_QUEUE, null, newMessage.getBytes());
                System.out.println(" [x] Sent: " + newMessage);
                
//                rabbitConnection.closeChannelAndConnection();
            }
        };
        consumerChannel.basicConsume(LISTENING_QUEUE, true, consumer);
    }

    private static String[] getBanks(long ssn, int creditScore, double loanAmount, int loanDuration) {
//        String[] banks = {"cphbusiness.bankXML","Gringotts", "BumBank", "cphbusiness.bankJSON"};
        String[] banks = {};

        String ssnToString = String.valueOf(ssn);
        try {
            rulebase.RuleBase_Service service = new rulebase.RuleBase_Service();
            rulebase.RuleBase port = service.getRuleBasePort();
            java.lang.String result = port.getBanks(ssnToString, creditScore, loanAmount, loanDuration);
            // convert string to string array
            banks = result.split(",");
        } catch (Exception ex) {
            System.out.println("ERROR: Getting banks, run the server.");
        }
        return banks;
    }
}
