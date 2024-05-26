package ru.otus.hw.services;

import ru.otus.hw.dto.in.JmsMessageDeliveryToStore;
import ru.otus.hw.dto.in.JmsMessagePaymentToStore;
import ru.otus.hw.dto.Status;
import ru.otus.hw.models.Store;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    Store create(JmsMessagePaymentToStore dto);

    Store findByUUID(UUID uuid);

    Store findByLogin(String login);

    List<Store> findAll();

    void updateStoreStatus(long id, Status status);

    void updateStoreStatusAndMessageSend(JmsMessageDeliveryToStore jmsMessageDeliveryToStore);
}
