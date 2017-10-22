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

    private String host = "datdb.cphbusiness.dk";
    private String userName = "student";
    private String password = "cph";
    private Integer port = 5672;
    private boolean localhost = true;

    private Channel channel;
    private ConnectionFactory factory;
    private Connection connection;

    public RabbitConnection() {
    }

    /**
     * @return null if exception or if variable is not set. Else channel
     */
    public Channel makeConnection() {
        if (host != null && userName != null && password != null) {
            try {
                factory = new ConnectionFactory();
                factory.setHost(host);
                if (!localhost) {
                    factory.setUsername(userName);
                    factory.setPassword(password);
                    factory.setPort(port);
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
        return host;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
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
