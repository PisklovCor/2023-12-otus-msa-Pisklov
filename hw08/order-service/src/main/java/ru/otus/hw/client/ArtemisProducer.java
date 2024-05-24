package ru.otus.hw.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.configuration.PropertiesConfiguration;
import ru.otus.hw.dto.JmsMessageOrder;
import ru.otus.hw.dto.JmsMessagePayment;
import ru.otus.hw.repositories.OrderRepository;

import static ru.otus.hw.dto.OrderStatus.WAIT;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtemisProducer {

    private final JmsTemplate jmsTemplate;

    private final PropertiesConfiguration configuration;

    private final OrderRepository orderRepository;

    public void sendMessage(JmsMessageOrder message) {
        jmsTemplate.convertAndSend(configuration.getDestinationSend(), message);
        log.info("Сообщение отправлено: " + message);
        orderRepository.updateOrderStatus(message.getOrderId(), WAIT);
    }

    @JmsListener(destination = "${application.destinationListener}")
    public void receiveMessage(JmsMessagePayment message) {
        log.info("Получено сообщение: " + message);
        orderRepository.updateOrderStatus(message.getOrderId(), message.getStatus());
    }
}
