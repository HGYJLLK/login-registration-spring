package com.example.stusyshomework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    private static final String TOPIC_NAME = "chat.topic";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void sendChatMessage(String senderId, String content) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        String message = String.format("[%s] %s: %s", timestamp, senderId, content);
        logger.info("Sending message to topic: {}", message);
        jmsTemplate.convertAndSend(TOPIC_NAME, message);
        logger.info("Message sent successfully");
    }
}
