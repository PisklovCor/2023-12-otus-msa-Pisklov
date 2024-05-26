package ru.otus.hw.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.in.JmsMessageDeliveryToStore;
import ru.otus.hw.dto.in.JmsMessagePaymentToStore;
import ru.otus.hw.services.StoreService;

import static ru.otus.hw.dto.Status.CONFIRMED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtemisProducerListener {

    private final StoreService storeService;

    private final ObjectMapper objectMapper;

    @JmsListener(destination = "${application.destinationListenerPayment}")
    public void receiveMessagePayment(Message message) throws JsonProcessingException {
        log.info("Получено сообщение: " + message);
        storeService.create(objectMapper.readValue(
                ((ActiveMQTextMessage) message).getText(), JmsMessagePaymentToStore.class));
    }

    @JmsListener(destination = "${application.destinationListenerDelivery}")
    public void receiveMessageDelivery(Message message) throws JsonProcessingException {
        log.info("Получено сообщение: " + message);

        JmsMessageDeliveryToStore jmsMessageDeliveryToStore = objectMapper.readValue(
                ((ActiveMQTextMessage) message).getText(), JmsMessageDeliveryToStore.class);

        if (jmsMessageDeliveryToStore.getStatus() == CONFIRMED) {
            log.info("Saga завершилась успешно, сообщение: " + message);
            storeService.updateStoreStatusAndMessageSend(jmsMessageDeliveryToStore);
        } else {
            log.warn("Saga откатилась, сообщение: " + message);
            storeService.updateStoreStatusAndMessageSend(jmsMessageDeliveryToStore);
        }

    }
}
