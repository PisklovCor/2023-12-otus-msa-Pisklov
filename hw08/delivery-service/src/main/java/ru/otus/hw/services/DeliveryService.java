package ru.otus.hw.services;

import ru.otus.hw.dto.in.JmsMessageStoreToDelivery;
import ru.otus.hw.dto.out.JmsMessageDeliveryToStore;
import ru.otus.hw.dto.Status;
import ru.otus.hw.models.Delivery;

import java.util.List;
import java.util.UUID;

public interface DeliveryService {

    Delivery create(JmsMessageStoreToDelivery jmsMessageStoreToDelivery);

    Delivery findByUUID(UUID uuid);

    Delivery findByLogin(String login);

    List<Delivery> findAll();

    void updateDeliveryStatus(long id, Status status);
}
