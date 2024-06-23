package ru.otus.hw.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.configuration.PropertiesConfiguration;
import ru.otus.hw.dto.out.NotificationMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtemisProducer {

    private final JmsTemplate jmsTemplate;

    private final PropertiesConfiguration configuration;

    public void sendMessage(NotificationMessage message) {
        log.info("Сообщение отправляется Artemis [{}]", message);
        jmsTemplate.convertAndSend(configuration.getDestinationSend(), message);
        log.info("Сообщение отправлено");
    }
}
