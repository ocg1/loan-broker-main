package banks;

import com.google.gson.Gson;
import com.rabbitmq.client.*;

import entities.Message;
import entities.LoanResponse;
import config.RabbitConnection;
import config.Constants;

import java.io.IOException;

/**
 *
 * @author Luke
 */
public class GringottsBankJSON {
    static final String SENDING_QUEUE = Constants.SENDING_BANKS;
    static final String LISTENING_QUEUE = Constants.LISTENING_OUR_JSON_BANK;
    static final String BANK_NAME = Constants.OUR_JSON;
    
    public static void main(String[] args) throws IOException {
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel listeningChannel = rabbitConnection.makeConnection();
        Channel sendingChannel = rabbitConnection.makeConnection();
        
        listeningChannel.queueDeclare(LISTENING_QUEUE, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        
        Consumer consumer = new DefaultConsumer(listeningChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                
                String data = new String(body, "UTF-8");
                System.out.println(" [x] Received: " + data);
                
                Gson gson = new Gson();
                Message message = gson.fromJson(data, Message.class);
                
                double interestRate = getInterestRate(message);
                
                LoanResponse loanResponse = new LoanResponse(message.getSsn(), interestRate, BANK_NAME);
                String response = gson.toJson(loanResponse, LoanResponse.class);
                
                sendingChannel.queueDeclare(SENDING_QUEUE, false, false, false, null);
                sendingChannel.basicPublish("", SENDING_QUEUE, null, response.getBytes());
                System.out.println(" [x] Sent: " + response);
            }
        };
        listeningChannel.basicConsume(LISTENING_QUEUE, true, consumer);
    }
    
    private static double getInterestRate(Message m) {
        double interestRate = 14;

        if (m.getCreditScore() > 600) {
            interestRate -= 4.5;
        } else if (m.getCreditScore() < 601 && m.getCreditScore() > 500) {
            interestRate -= 2.7;
        } else if (m.getCreditScore() < 501 && m.getCreditScore() > 400) {
            interestRate -= 0.9;
        }

        int durationCut = m.getLoanDuration() / 360;

        interestRate -= (durationCut * 0.18);

        double amountCut = m.getLoanAmount() / 100000;

        interestRate -= (amountCut * 0.18);
        
        return interestRate;
    }
}
