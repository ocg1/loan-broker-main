package config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;

/**
 *
 * @author Luke
 */
public class RabbitConnection {

    private final String HOST = "datdb.cphbusiness.dk";
    private final String USER_NAME = "student";
    private final String PASSWORD = "cph";
    private final Integer PORT = 5672;
    private boolean localhost = false;

    private Channel channel;
    private ConnectionFactory factory;
    private Connection connection;

    public RabbitConnection() {
    }

    /**
     * @return null if exception or if variable is not set. Else channel
     */
    public Channel makeConnection() {
        if (HOST != null && USER_NAME != null && PASSWORD != null) {
            try {
                factory = new ConnectionFactory();
                factory.setHost(HOST);
                if (!localhost) {
                    factory.setUsername(USER_NAME);
                    factory.setPassword(PASSWORD);
                    factory.setPort(PORT);
                }
                connection = factory.newConnection();
                channel = connection.createChannel();
            } catch (IOException exc) {
                System.out.println("Error in RabbitConnection class - makeConnection()");
                exc.printStackTrace();
                return null;
            }
            return this.channel;
        }
        return null;
    }

    public String getHost() {
        return HOST;
    }

    public String getUserName() {
        return USER_NAME;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public Channel getChannel() {
        return channel;
    }

    public ConnectionFactory getFactory() {
        return factory;
    }

    public void closeChannelAndConnection() {
        if (connection != null && channel != null) {
            try {
                channel.close();
                connection.close();
            } catch (IOException ex) {
                System.out.println("Error in class RabbitConnection - function closeChannelAndConnection");
            }
        }
    }
}
