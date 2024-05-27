package ru.otus.hw.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.configuration.PropertiesConfiguration;
import ru.otus.hw.dto.out.JmsMessageDeliveryToStore;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtemisProduceSender {

    private final JmsTemplate jmsTemplate;

    private final PropertiesConfiguration configuration;


    public void sendMessageStore(JmsMessageDeliveryToStore message) {
        jmsTemplate.convertAndSend(configuration.getDestinationSendStore(), message);
        log.info("Сообщение отправлено: " + message);
    }
}
