package com.example.stusyshomework.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageListener.class);

    public static final List<String> MESSAGE_LOG = new CopyOnWriteArrayList<>();

    @JmsListener(destination = "chat.topic", containerFactory = "jmsListenerContainerTopic")
    public void receiveMessage(String message) {
        logger.info("Received message: {}", message);
        MESSAGE_LOG.add(message);
        logger.info("Message added to log. Total messages: {}", MESSAGE_LOG.size());
    }
}
