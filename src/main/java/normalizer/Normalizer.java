package normalizer;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import config.Constants;
import config.RabbitConnection;
import entities.LoanResponse;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author williambech
 */
public class Normalizer {

    private final static String LISTENING_QUEUE = Constants.LISTENING_NORMALIZER;
    private final static String SENDING_QUEUE = Constants.AGGREGATOR_EXCHANGE;
    private final static String XML_TYPE = "xml";
    private final static String JSON_TYPE = "json";
    
    // Defines which bank sent the message
    public static String getBankName(String type, LoanResponse resp) {
        if (resp.getBank() != null) {
            return resp.getBank();
        }
        return "CphBusiness" + "." + type;
    }
    
    // Returns the message type: JSON or XML
    public static String getMessageType(String message) {
        if (message.startsWith("{")) {
            return JSON_TYPE;
        } else if (message.startsWith("<")) {
            return XML_TYPE;
        }
        return null;
    }

    public static void main(String[] argv) throws Exception {
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel listeningChannel = rabbitConnection.makeConnection();
        Channel sendingChannel = rabbitConnection.makeConnection();

        listeningChannel.queueDeclare(LISTENING_QUEUE, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(listeningChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" --- ");
                System.out.println(" [x] Received: " + message);
                
                LoanResponse response;
                String finalMessage = "";
                String bank;
                String type = getMessageType(message);
                Gson gson = new Gson();

                switch (type) {
                    case XML_TYPE:
                        JSONObject jsonMessage = XML.toJSONObject(message);
                        String result = gson.toJson(jsonMessage);
                        response = gson.fromJson(result, LoanResponse.class);
                        
                        bank = getBankName(type, response);
                        response.setBank(bank);
                        
                        finalMessage = gson.toJson(response);
                        break;
                case JSON_TYPE: 
                        response = gson.fromJson(message, LoanResponse.class);
                        
                        bank = getBankName(type, response);
                        response.setBank(bank);
                        
                        finalMessage = gson.toJson(response);
                        break;
                default:
                        System.out.println("We don't support that message type yet");
                        break;
                }

                if (!finalMessage.isEmpty()) {
                    sendingChannel.queueDeclare(SENDING_QUEUE, false, false, false, null);
                    sendingChannel.basicPublish("", SENDING_QUEUE, null, finalMessage.getBytes());
                    System.out.println(" [x] Sent: " + finalMessage);
                } else {
                    System.out.println("Nothing to send to the Aggregator");
                }
            }
        };
        listeningChannel.basicConsume(LISTENING_QUEUE, true, consumer);
    }
}