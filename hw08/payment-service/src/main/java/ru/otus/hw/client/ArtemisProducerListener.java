package ru.otus.hw.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.in.JmsMessageOrderToPayment;
import ru.otus.hw.dto.in.JmsMessageStoreToPayment;
import ru.otus.hw.services.PaymentService;

import static ru.otus.hw.dto.Status.CONFIRMED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtemisProducerListener {

    private final PaymentService paymentService;

    private final ObjectMapper objectMapper;

    @JmsListener(destination = "${application.destinationListenerOrder}")
    public void receiveMessageOrder(Message message) throws JsonProcessingException {
        log.info("Получено сообщение: " + message);
        paymentService.create(objectMapper.readValue(
                ((ActiveMQTextMessage) message).getText(), JmsMessageOrderToPayment.class));
    }

    @JmsListener(destination = "${application.destinationListenerStore}")
    public void receiveMessageStore(Message message) throws JsonProcessingException {
        log.info("Получено сообщение: " + message);

        JmsMessageStoreToPayment jmsMessageStoreToPayment = objectMapper.readValue(
                ((ActiveMQTextMessage) message).getText(), JmsMessageStoreToPayment.class);

        if (jmsMessageStoreToPayment.getStatus() == CONFIRMED) {
            log.info("Saga завершилась успешно, сообщение: " + message);
            paymentService.updatePaymentStatusAndMessageSend(jmsMessageStoreToPayment);
        } else {
            log.warn("Saga откатилась, сообщение: " + message);
            paymentService.updatePaymentStatusAndMessageSend(jmsMessageStoreToPayment);
        }

    }
}
