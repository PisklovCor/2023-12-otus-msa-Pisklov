package ru.otus.hw.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.configuration.PropertiesConfiguration;
import ru.otus.hw.dto.out.JmsMessageStoreToPayment;
import ru.otus.hw.dto.out.JmsMessageStoreToDelivery;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtemisProduceSender {

    private final JmsTemplate jmsTemplate;

    private final PropertiesConfiguration configuration;


    public void sendMessagePayment(JmsMessageStoreToPayment message) {
        jmsTemplate.convertAndSend(configuration.getDestinationSendPayment(), message);
        log.info("Сообщение отправлено: " + message);
    }

    public void sendMessageDelivery(JmsMessageStoreToDelivery message) {
        jmsTemplate.convertAndSend(configuration.getDestinationSendDelivery(), message);
        log.info("Сообщение отправлено: " + message);
    }
}
