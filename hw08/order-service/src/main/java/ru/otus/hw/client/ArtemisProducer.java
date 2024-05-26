package ru.otus.hw.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.configuration.PropertiesConfiguration;
import ru.otus.hw.dto.JmsMessageOrderToPayment;
import ru.otus.hw.dto.JmsMessagePaymentToOrder;
import ru.otus.hw.repositories.OrderRepository;

import static ru.otus.hw.dto.Status.WAIT;
import static ru.otus.hw.dto.Status.CONFIRMED;
import static ru.otus.hw.dto.Status.CANCELED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtemisProducer {

    private final JmsTemplate jmsTemplate;

    private final PropertiesConfiguration configuration;

    private final OrderRepository orderRepository;

    private final ObjectMapper objectMapper;

    public void sendMessage(JmsMessageOrderToPayment message) {
        jmsTemplate.convertAndSend(configuration.getDestinationSend(), message);
        log.info("Сообщение отправлено: " + message);
        orderRepository.updateOrderStatus(message.getOrderId(), WAIT);
    }

    @JmsListener(destination = "${application.destinationListener}")
    public void receiveMessage(Message message) throws JsonProcessingException {
        log.info("Получено сообщение: " + message);

        JmsMessagePaymentToOrder jmsMessagePaymentToOrder = objectMapper.readValue(
                ((ActiveMQTextMessage) message).getText(), JmsMessagePaymentToOrder.class);

        if (jmsMessagePaymentToOrder.getStatus() == CONFIRMED) {
            log.info("Saga завершилась успешно, сообщение: " + message);
            orderRepository.updateOrderStatus(jmsMessagePaymentToOrder.getOrderId(), CONFIRMED);
        } else {
            log.warn("Saga откатилась, сообщение: " + message);
            orderRepository.updateOrderStatus(jmsMessagePaymentToOrder.getOrderId(), CANCELED);
        }
    }
}
