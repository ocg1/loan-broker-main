package translators;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import entities.Message;
import config.Constants;
import config.RabbitConnection;
import entities.LoanXMLRequest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Luke
 */
public class Cphbusiness_XML_Translator {

    static final String EXCHANGE = Constants.TRANSLATOR_EXCHANGE;
    static final String SENDING_QUEUE = Constants.SENDING_SCHOOL_XML_TRANSLATOR;
    static final String SEVERITY = Constants.SCHOOL_XML;
    static final String REPLY_TO_HEADER = Constants.SENDING_BANKS;

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
                try {
                    String data = new String(body, "UTF-8");
                    System.out.println(" [x] Received: " + data);

                    Gson gson = new Gson();
                    Message m = gson.fromJson(data, Message.class);

                    String dashRemoved = String.valueOf(m.getSsn()).replace("-", "");
                    String dateFormat = dateAdder(m.getLoanDuration());

                    LoanXMLRequest result = new LoanXMLRequest(
                            Integer.parseInt(dashRemoved),
                            m.getCreditScore(),
                            m.getLoanAmount(),
                            dateFormat
                    );

                    JAXBContext jaxbContext = JAXBContext.newInstance(LoanXMLRequest.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                    StringWriter sw = new StringWriter();
                    jaxbMarshaller.marshal(result, sw);
                    String xmlString = sw.toString();

                    xmlString = xmlString.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                    System.out.println(xmlString);

                    AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                            .contentType("application/xml")
                            .deliveryMode(1)
                            .replyTo(REPLY_TO_HEADER)
                            .build();

                    sendingChannel.queueDeclare(REPLY_TO_HEADER, false, false, false, null);
                    
                    String replyKey = "xml";

                    sendingChannel.basicPublish(SENDING_QUEUE, replyKey, props, xmlString.getBytes());
                } catch (JAXBException ex) {
                    Logger.getLogger(Cphbusiness_XML_Translator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        listeningChannel.basicConsume(queueName, true, consumer);
    }

    private static String dateAdder(int days) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date()); // use today date
        calendar.add(Calendar.DATE, days); // add days

        String output = simpleDateFormat.format(calendar.getTime()) + " 01:00:00.0 CET"; // add hardcoded time

        return output;
    }
}
