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
import entities.LoanXMLResponse;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Normalizer {

    private final static String LISTENING_QUEUE = Constants.LISTENING_NORMALIZER;
    private final static String SENDING_QUEUE = Constants.AGGREGATOR_EXCHANGE;
    private final static String XML_TYPE = "xml";
    private final static String JSON_TYPE = "json";
    
    // Defines which bank sent the message
    public static String getBankName(String type, String bank) {
        if (bank != null) {
            return bank;
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

    private static LoanXMLResponse unmarshal(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(LoanXMLResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        StringReader reader = new StringReader(xml);

        LoanXMLResponse payload = (LoanXMLResponse) unmarshaller.unmarshal(reader);

        return payload;
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
                try {
                    String message = new String(body, "UTF-8");
                    System.out.println("---------------------------------------------------------------");
                    System.out.println(" [x] Received: " + message);
                    
                    String newMessage = "";
                    String bank;
                    String type = getMessageType(message);
                    Gson gson = new Gson();
                    
                    switch (type) {
                        case XML_TYPE:
                            LoanXMLResponse xmlResponse = unmarshal(message);
                            
                            bank = getBankName(type, xmlResponse.getBank());
                            xmlResponse.setBank(bank);
                            
                            newMessage = gson.toJson(xmlResponse);
                            break;
                        case JSON_TYPE:
                            LoanResponse response = gson.fromJson(message, LoanResponse.class);
                            
                            bank = getBankName(type, response.getBank());
                            response.setBank(bank);
                            
                            newMessage = gson.toJson(response);
                            break;
                        default:
                            System.out.println("We don't support that message type yet");
                            break;
                    }
                    
                    if (!newMessage.isEmpty()) {
                        sendingChannel.queueDeclare(SENDING_QUEUE, false, false, false, null);
                        sendingChannel.basicPublish("", SENDING_QUEUE, null, newMessage.getBytes());
                        System.out.println(" [x] Sent: " + newMessage);
                    } else {
                        System.out.println("Nothing to send to the Aggregator");
                    }
                } catch (JAXBException ex) {
                    System.out.println(ex);
                }
            }
        };
        listeningChannel.basicConsume(LISTENING_QUEUE, true, consumer);
    }
}