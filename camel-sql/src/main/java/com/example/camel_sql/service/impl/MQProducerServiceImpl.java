package com.example.camel_sql.service.impl;

import com.example.camel_sql.service.MQProducerService;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class MQProducerServiceImpl implements MQProducerService {

    @Value("${activemq.broker-url}")
    private String brokerUrl;

    @Value("${activemq.queue-name}")
    private String queueName;


    @Override
    public void processAndSendFile(MultipartFile file) throws Exception {
        String content = extractFileContent(file);
        sendToQueue(content);
    }

    private String extractFileContent(MultipartFile file) throws IOException, TikaException {
        Tika tika = new Tika();
        return tika.parseToString(file.getInputStream());
    }

    public void sendToQueue(String messageContent) {
        Connection connection = null;
        Session session = null;

        try {
            // Create a connection factory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

            // Create a connection
            connection = connectionFactory.createConnection();
            connection.start();

            // Create a session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (queue)
            Destination destination = session.createQueue(queueName);

            // Create a message producer
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            // Create and send the message
            TextMessage message = session.createTextMessage(messageContent);
            producer.send(message);

            System.out.println("Message sent to ActiveMQ queue: " + queueName);

        } catch (JMSException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send message to ActiveMQ", e);
        } finally {
            // Clean up resources
            try {
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
