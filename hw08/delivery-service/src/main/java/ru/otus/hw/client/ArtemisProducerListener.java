package ru.otus.hw.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.in.JmsMessageStoreToDelivery;
import ru.otus.hw.services.DeliveryService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtemisProducerListener {

    private final DeliveryService deliveryService;

    private final ObjectMapper objectMapper;

    @JmsListener(destination = "${application.destinationListenerStore}")
    public void receiveMessagePayment(Message message) throws JsonProcessingException {
        log.info("Получено сообщение: " + message);
        deliveryService.create(objectMapper.readValue(
                ((ActiveMQTextMessage) message).getText(), JmsMessageStoreToDelivery.class));
    }
}
