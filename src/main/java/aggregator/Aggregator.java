/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aggregator;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import config.Constants;
import config.RabbitConnection;
import entities.LoanResponse;
import entities.Message;
import java.util.ArrayList;
import java.util.UUID;
/**
 *
 * @author williambech
 */
public class Aggregator {
    private final static String LISTENING_QUEUE = Constants.LISTENING_AGGREGATOR;
    private final static String SENDING_QUEUE = Constants.AGGREGATOR_EXCHANGE;

    private static Gson gson = new Gson();
    // This is used in order to group messages that are going to the same recipient
    private static ArrayList<AggregatorFactory> factories = new ArrayList<AggregatorFactory>();

    public static void main(String[] argv) throws Exception {
        RabbitConnection rabbitConnection = new RabbitConnection();
        Channel listeningChannel = rabbitConnection.makeConnection();
        Channel sendingChannel = rabbitConnection.makeConnection();

        listeningChannel.queueDeclare(LISTENING_QUEUE, false, false, false, null);
        listeningChannel.queueDeclare(SENDING_QUEUE, false, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(listeningChannel);
        
        listeningChannel.basicConsume(LISTENING_QUEUE, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery(1000);

            if (delivery != null) {
                String message = new String(delivery.getBody());
                LoanResponse msg = gson.fromJson(message, LoanResponse.class);
                
                System.out.println(" [x] Received '" + message + "'");
                // If this is the first message received create a new factory
                if (factories.size() < 1) {
                    AggregatorFactory aggFactory = new AggregatorFactory(msg.getSsn());
                    aggFactory.addMessage(msg);
                    factories.add(aggFactory);
                } else {
                    // Create a factory if there is no factory with equivalent ssn
                    for (int i = 0; i < factories.size(); i++) {
                        try {
                            if (factories.get(i).getSsn() == msg.getSsn()) {
                                factories.get(i).addMessage(msg);
                                break;
                            } else {
                                AggregatorFactory newFactory = new AggregatorFactory(msg.getSsn());
                                newFactory.addMessage(msg);
                                factories.add(newFactory);
                                break;
                            }
                        } catch (Exception exception) {
                            System.out.println(exception);
                        }
                    }
                }
            }
            // Check the time waited by factories and send them if they waited more than 10 seconds
            for (int j = 0; j < factories.size(); j++) {
                AggregatorFactory focusedFactory = factories.get(j);
                if (focusedFactory.getWaitTime() > 10) {
                    // GetBestBank() returns the message with the best bank deal
                    LoanResponse resultMessage = focusedFactory.getBestBank();
                    System.out.println(resultMessage);
                    String result = gson.toJson(resultMessage, LoanResponse.class);
                    System.out.println("Result: " + result);
//                    sendingChannel.basicPublish(SENDING_QUEUE, focusedFactory.ssn, null, result.getBytes());
                    sendingChannel.basicPublish("", SENDING_QUEUE, null, result.getBytes());

//                    sendingChannel.basicPublish(SENDING_QUEUE, null, null, result.getBytes());
                    // remove used factory from factories ArrayList
                    factories.remove(factories.get(j));
                }
            }
        }
    }
}
