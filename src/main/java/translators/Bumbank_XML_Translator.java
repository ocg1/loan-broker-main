package translators;

import com.rabbitmq.client.*;

import config.Constants;
import config.RabbitConnection;

import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;

/**
 *
 * @author Luke
 */
public class Bumbank_XML_Translator {

    private static final String EXCHANGE = Constants.TRANSLATOR_EXCHANGE;
    private static final String SEVERITY = Constants.OUR_XML;

    public static void main(String[] args) throws IOException {
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel channel = rabbitConnection.makeConnection();

        channel.exchangeDeclare(EXCHANGE, "direct");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE, SEVERITY);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    String data = new String(body, "UTF-8");
                    System.out.println(" [x] Received: " + data);

                    String xml = jsonToXml(data);
                    System.out.println(xml);

                    request(xml);
                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    private static String jsonToXml(String data) {
        JSONObject json = new JSONObject(data);
        return "<LoanRequest>" + XML.toString(json) + "</LoanRequest>";
    }

    private static String request(String payload) throws Exception {
        com.mycompany.bumbankxml.RequestLoan service = new com.mycompany.bumbankxml.RequestLoan();
        com.mycompany.bumbankxml.BumBankXML port = service.getBumBankXMLPort();
        String feedback = port.request(payload);
        System.out.println(feedback);
        return feedback;
    }
}
