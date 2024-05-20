package ru.otus.hw.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.configuration.PropertiesConfiguration;
import ru.otus.hw.dto.JmsMessage;
import ru.otus.hw.services.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtemisProducer {

    private final JmsTemplate jmsTemplate;

    private final PropertiesConfiguration configuration;

    private final NotificationService notificationService;

    public void sendMessage(JmsMessage message) {
        jmsTemplate.convertAndSend(configuration.getDestinationSend(), message);
    }

    @JmsListener(destination = "${application.destinationListener}")
    public void receiveMessage(JmsMessage message) {
        log.info("Сообщение получено: " + message);
        notificationService.create(message);
    }
}
