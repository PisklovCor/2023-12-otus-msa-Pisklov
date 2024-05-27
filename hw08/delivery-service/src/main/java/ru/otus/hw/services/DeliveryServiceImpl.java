package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.client.ArtemisProduceSender;
import ru.otus.hw.dto.out.JmsMessageDeliveryToStore;
import ru.otus.hw.dto.Status;
import ru.otus.hw.dto.in.JmsMessageStoreToDelivery;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Delivery;
import ru.otus.hw.repositories.DeliveryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.otus.hw.dto.Status.CREATED;
import static ru.otus.hw.dto.Status.CANCELED;
import static ru.otus.hw.dto.Status.CONFIRMED;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final ArtemisProduceSender artemisProduceSender;

    @Override
    public Delivery create(JmsMessageStoreToDelivery message) {

        log.info("Получен товар от пользователя [{}]", message.getLogin(), message.getSumOrder());

        if (message.getDescriptionOrder().equals("Rollback transactions in the Delivery service")) {
            log.warn("Проверка принудительного отката");
            JmsMessageDeliveryToStore jmsMessageDeliveryToStore = new JmsMessageDeliveryToStore();
            jmsMessageDeliveryToStore.setStoreId(message.getStoreId());
            jmsMessageDeliveryToStore.setPaymentId(message.getPaymentId());
            jmsMessageDeliveryToStore.setStatus(CANCELED);
            artemisProduceSender.sendMessageStore(jmsMessageDeliveryToStore);
            log.warn("Saga откатилась, сообщение: " + jmsMessageDeliveryToStore);
            return null;
        }

        Delivery delivery = new Delivery();
        delivery.setStoreId(message.getStoreId());
        delivery.setPaymentId(message.getPaymentId());
        delivery.setOrderId(message.getOrderId());
        delivery.setLogin(message.getLogin());
        delivery.setAccountInvoice(UUID.randomUUID());
        delivery.setDescriptionOrder(message.getDescriptionOrder());
        delivery.setSumOrder(message.getSumOrder());
        delivery.setStatus(CREATED.toString());

        log.info("Будет создана запись о товаре");
        final Delivery newDelivery = deliveryRepository.create(delivery);
        sendMessageJms(newDelivery);
        deliveryRepository.updateDeliveryStatus(newDelivery.getId(), CONFIRMED);

        return newDelivery;
    }

    @Override
    public Delivery findByUUID(UUID uuid) {
        var store = deliveryRepository.findByUUID(uuid);
        return store.orElseThrow(
                () -> new EntityNotFoundException("Delivery with uuid %s not found".formatted(uuid)));
    }

    @Override
    public Delivery findByLogin(String login) {
        var store = deliveryRepository.findByLogin(login);
        return store.orElseThrow(
                () -> new EntityNotFoundException("Delivery with id %s not found".formatted(login)));
    }

    @Override
    public List<Delivery> findAll() {
        List<Delivery> deliveryDtoList =  new ArrayList<>();

        deliveryRepository.findAll().forEach(o -> deliveryDtoList.add(o));

        return deliveryDtoList;
    }

    @Override
    public void updateDeliveryStatus(long id, Status status) {
        deliveryRepository.updateDeliveryStatus(id, status);
    }

    private void sendMessageJms(Delivery delivery) {

        try {
            JmsMessageDeliveryToStore jmsMessageDeliveryToStore = new JmsMessageDeliveryToStore();
            jmsMessageDeliveryToStore.setStoreId(delivery.getStoreId());
            jmsMessageDeliveryToStore.setPaymentId(delivery.getPaymentId());
            jmsMessageDeliveryToStore.setStatus(CONFIRMED);
            artemisProduceSender.sendMessageStore(jmsMessageDeliveryToStore);
            log.info("Сообщение успешно отправлено в брокер [{}]", jmsMessageDeliveryToStore);
            log.info("Saga завершилась");
        } catch (Exception e) {
            log.error("Failed to send message to broker:" + e.getMessage());
        }
    }
}
