package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.client.ArtemisProduceSender;
import ru.otus.hw.dto.in.JmsMessageDeliveryToStore;
import ru.otus.hw.dto.in.JmsMessagePaymentToStore;
import ru.otus.hw.dto.Status;
import ru.otus.hw.dto.out.JmsMessageStoreToPayment;
import ru.otus.hw.dto.out.JmsMessageStoreToDelivery;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Store;
import ru.otus.hw.repositories.StoreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.otus.hw.dto.Status.CREATED;
import static ru.otus.hw.dto.Status.WAIT;
import static ru.otus.hw.dto.Status.CANCELED;
import static ru.otus.hw.dto.Status.CONFIRMED;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    private final ArtemisProduceSender artemisProduceSender;

    @Override
    public Store create(JmsMessagePaymentToStore dto) {

        log.info("Получен товар от пользователя [{}] на сумму [{}]", dto.getLogin(), dto.getSumOrder());

        if (dto.getDescriptionOrder().equals("Rollback transactions in the Store service")) {
            log.warn("Проверка принудительного отката");
            JmsMessageStoreToPayment message = new JmsMessageStoreToPayment();
            message.setPaymentId(dto.getPaymentId());
            message.setOrderId(dto.getOrderId());
            message.setStatus(CANCELED);
            artemisProduceSender.sendMessagePayment(message);
            return null;
        }

        Store store = new Store();
        store.setPaymentId(dto.getPaymentId());
        store.setOrderId(dto.getOrderId());
        store.setLogin(dto.getLogin());
        store.setAccountInvoice(dto.getAccountInvoice());
        store.setDescriptionOrder(dto.getDescriptionOrder());
        store.setSumOrder(dto.getSumOrder());
        store.setStatus(CREATED.toString());

        log.info("Будет создана запись о товаре");
        final Store newStore = storeRepository.create(store);
        sendMessageJms(newStore);
        storeRepository.updateStoreStatus(newStore.getId(), WAIT);

        return newStore;
    }

    @Override
    public Store findByUUID(UUID uuid) {
        var store = storeRepository.findByUUID(uuid);
        return store.orElseThrow(
                () -> new EntityNotFoundException("Store with uuid %s not found".formatted(uuid)));
    }

    @Override
    public Store findByLogin(String login) {
        var store = storeRepository.findByLogin(login);
        return store.orElseThrow(
                () -> new EntityNotFoundException("Store with id %s not found".formatted(login)));
    }

    @Override
    public List<Store> findAll() {
        List<Store> paymentDtoList =  new ArrayList<>();

        storeRepository.findAll().forEach(o -> paymentDtoList.add(o));

        return paymentDtoList;
    }

    @Override
    public void updateStoreStatus(long id, Status status) {
        storeRepository.updateStoreStatus(id, status);
    }

    @Override
    public void updateStoreStatusAndMessageSend(JmsMessageDeliveryToStore jmsMessageDeliveryToStore) {
        updateStoreStatus(jmsMessageDeliveryToStore.getStoreId(), jmsMessageDeliveryToStore.getStatus());
        log.info("Состояние платежа обновлено");
        JmsMessageStoreToPayment message = new JmsMessageStoreToPayment();
        message.setPaymentId(jmsMessageDeliveryToStore.getPaymentId());
        message.setStatus(jmsMessageDeliveryToStore.getStatus());
        if (jmsMessageDeliveryToStore.getStatus() == CONFIRMED) {
            log.info("Saga завершилась успешно, сообщение: " + message);
            artemisProduceSender.sendMessagePayment(message);
        } else {
            log.warn("Saga откатилась, сообщение: " + message);
            artemisProduceSender.sendMessagePayment(message);
        }
    }

    private void sendMessageJms(Store store) {

        try {
            JmsMessageStoreToDelivery jmsMessageStoreToDelivery = new JmsMessageStoreToDelivery();
            jmsMessageStoreToDelivery.setStoreId(store.getId());
            jmsMessageStoreToDelivery.setPaymentId(store.getId());
            jmsMessageStoreToDelivery.setOrderId(store.getOrderId());
            jmsMessageStoreToDelivery.setLogin(store.getLogin());
            jmsMessageStoreToDelivery.setDescriptionOrder(store.getDescriptionOrder());
            artemisProduceSender.sendMessageDelivery(jmsMessageStoreToDelivery);
            log.info("Сообщение успешно отправлено в брокер [{}]", jmsMessageStoreToDelivery);
        } catch (Exception e) {
            log.error("Failed to send message to broker:" + e.getMessage());
        }
    }
}
