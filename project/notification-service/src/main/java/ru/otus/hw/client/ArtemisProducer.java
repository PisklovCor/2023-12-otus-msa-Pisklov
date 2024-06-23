package ru.otus.hw.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.NotificationConverter;
import ru.otus.hw.dto.NotificationDto;
import ru.otus.hw.services.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArtemisProducer {

    private final ObjectMapper objectMapper;

    private final NotificationService service;

    private final NotificationConverter converter;


    @JmsListener(destination = "${application.destinationListener}")
    public void receiveMessageDelivery(Message message) throws JsonProcessingException {
        log.info("Получено сообщение: " + message);

        NotificationDto notificationDto = objectMapper.readValue(
                ((ActiveMQTextMessage) message).getText(), NotificationDto.class);

        service.createNotification(converter.mapDtoToModel(notificationDto));
        log.info("Сообщение обработано");
    }
}
