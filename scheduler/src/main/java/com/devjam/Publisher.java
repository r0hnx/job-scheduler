package com.devjam;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Publisher {
    private final static String QUEUE_NAME = "flow.executor";
    private static ConnectionFactory factory;

    static {
        factory = new ConnectionFactory();
        factory.setHost("localhost"); // Replace with your RabbitMQ server host
        factory.setPort(5672);
        factory.setUsername("user"); // Replace with your RabbitMQ server credentials
        factory.setPassword("password");
        factory.setVirtualHost("/");
    }
    
    public static void publish(String payload) {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, payload.getBytes());
            
            System.out.println(" [x] Sent '" + payload + "'");
            
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
