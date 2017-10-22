package translators;

import com.rabbitmq.client.*;

import config.Constants;
import config.RabbitConnection;

import java.io.IOException;

/**
 *
 * @author Luke
 */
public class Gringotts_JSON_Translator {
    private static final String EXCHANGE = Constants.TRANSLATOR_EXCHANGE;
    private static final String SENDING_QUEUE = Constants.SENDING_OUR_JSON_TRANSLATOR;
    private static final String SEVERITY = Constants.OUR_JSON;  

    public static void main(String[] args) throws IOException {
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel listeningChannel = rabbitConnection.makeConnection();
        Channel sendingChannel = rabbitConnection.makeConnection();
        
        listeningChannel.exchangeDeclare(EXCHANGE, "direct");
        String queueName = listeningChannel.queueDeclare().getQueue();
        listeningChannel.queueBind(queueName, EXCHANGE, SEVERITY);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        
        Consumer consumer = new DefaultConsumer(listeningChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received: " + message);
                
                sendingChannel.queueDeclare(SENDING_QUEUE, false, false, false, null);
                sendingChannel.basicPublish("", SENDING_QUEUE, null, message.getBytes());
                System.out.println(" [x] Sent: " + message);
            }
        };
        listeningChannel.basicConsume(queueName, true, consumer);
    }
}
