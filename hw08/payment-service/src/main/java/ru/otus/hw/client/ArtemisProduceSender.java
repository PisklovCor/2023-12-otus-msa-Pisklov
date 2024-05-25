package ru.otus.hw.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.configuration.PropertiesConfiguration;
import ru.otus.hw.dto.out.JmsMessagePaymentOrder;
import ru.otus.hw.dto.out.JmsMessagePaymentStore;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtemisProduceSender {

    private final JmsTemplate jmsTemplate;

    private final PropertiesConfiguration configuration;


    public void sendMessageOrder(JmsMessagePaymentOrder message) {
        jmsTemplate.convertAndSend(configuration.getDestinationSendOrder(), message);
        log.info("Сообщение отправлено: " + message);
    }

    public void sendMessageStore(JmsMessagePaymentStore message) {
        jmsTemplate.convertAndSend(configuration.getDestinationSendStore(), message);
        log.info("Сообщение отправлено: " + message);
    }
}
