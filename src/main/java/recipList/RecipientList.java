
package recipList;

import com.rabbitmq.client.Channel;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import entities.Message;
import config.RabbitConnection;
import config.Constants;

import java.io.IOException;

/**
 *
 * @author williambech
 */
public class RecipientList {
    static final String LISTENING_QUEUE = Constants.LISTENING_RECIP_LIST;
    static final String EXCHANGE = Constants.TRANSLATOR_EXCHANGE;

    // JSON banks
    static final String OUR_JSON = Constants.OUR_JSON;  
    static final String SCHOOL_JSON = Constants.SCHOOL_JSON;

    // XML banks
    static final String OUR_XML = Constants.OUR_XML;
    static final String SCHOOL_XML = Constants.SCHOOL_XML;

    public static void main(String[] argv) throws Exception {
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel listeningChannel = rabbitConnection.makeConnection();
        Channel sendingChannel = rabbitConnection.makeConnection();

        listeningChannel.queueDeclare(LISTENING_QUEUE, false, false, false, null);
        sendingChannel.exchangeDeclare(EXCHANGE, "direct");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(listeningChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                
                String data = new String(body, "UTF-8");
                System.out.println(" [x] Received: " + data);
                
                Gson gson = new Gson();
                Message message = gson.fromJson(data, Message.class);
                
                Message request = new Message(
                    message.getSsn(),
                    message.getCreditScore(),
                    message.getLoanAmount(),
                    message.getLoanDuration()
                );
                        
                String newMessage = gson.toJson(request, Message.class);
                
                String[] banks = message.getBanks();
                for (String bank : banks) {
                    switch (bank) {
                        case OUR_JSON:
                            sendingChannel.basicPublish(EXCHANGE, OUR_JSON, null, newMessage.getBytes());
                            System.out.println(" [x] Sent to Gringotts JSON Translator: " + request);
                            break;
                        case OUR_XML:
                            sendingChannel.basicPublish(EXCHANGE, OUR_XML, null, newMessage.getBytes());
                            System.out.println(" [x] Sent to Bum Bank XML Translator: " + request);
                            break;
                        case SCHOOL_XML:
                            sendingChannel.basicPublish(EXCHANGE, SCHOOL_XML, null, newMessage.getBytes());
                            System.out.println(" [x] Sent to CphBusiness XML Translator: " + request);
                            break;
                        case SCHOOL_JSON:
                            sendingChannel.basicPublish(EXCHANGE, SCHOOL_JSON, null, newMessage.getBytes());
                            System.out.println(" [x] Sent to CphBusiness JSON Translator: " + request);
                            break;
                        default:
                            System.out.println("Couldn't find a bank :( ");
                    }
                }
            }
        };
        listeningChannel.basicConsume(LISTENING_QUEUE, true, consumer);
    }
}
