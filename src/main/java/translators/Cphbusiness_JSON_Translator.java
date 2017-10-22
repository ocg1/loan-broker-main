package translators;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import config.Constants;
import config.RabbitConnection;
import java.io.IOException;

/**
 *
 * @author Luke
 */
public class Cphbusiness_JSON_Translator {

    static final String EXCHANGE = Constants.TRANSLATOR_EXCHANGE;
    static final String SENDING_QUEUE = Constants.SENDING_SCHOOL_JSON_TRANSLATOR;
    static final String SEVERITY = Constants.SCHOOL_JSON;
    static final String REPLY_TO_HEADER = Constants.SENDING_BANKS;

    static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        RabbitConnection listeningRabbitConnection = new RabbitConnection();
        Channel listeningChannel = listeningRabbitConnection.makeConnection();

        RabbitConnection sendingRabbitConnection = new RabbitConnection();
        Channel sendingChannel = sendingRabbitConnection.makeConnection();

        listeningChannel.exchangeDeclare(EXCHANGE, "direct");
        String queueName = listeningChannel.queueDeclare().getQueue();
        listeningChannel.queueBind(queueName, EXCHANGE, SEVERITY);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(listeningChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received: " + message);

                AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                        .contentType("application/json")
                        .deliveryMode(1)
                        .replyTo(REPLY_TO_HEADER)
                        .build();

                sendingChannel.queueDeclare(REPLY_TO_HEADER, false, false, false, null);
                
                String replyKey = "json";

                sendingChannel.basicPublish(SENDING_QUEUE, replyKey, props, message.getBytes());
                System.out.println(" [x] Sent: " + message);
            }
        };
        listeningChannel.basicConsume(queueName, true, consumer);
    }
}
