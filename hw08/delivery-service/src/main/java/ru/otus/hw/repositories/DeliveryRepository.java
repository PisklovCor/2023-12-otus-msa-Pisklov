package ru.otus.hw.repositories;

import ru.otus.hw.dto.Status;
import ru.otus.hw.models.Delivery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {

    Delivery create(Delivery delivery);

    Optional<Delivery> findById(long id);

    Optional<Delivery> findByUUID(UUID uuid);

    Optional<Delivery> findByLogin(String login);

    List<Delivery> findAll();

    void updateDeliveryStatus(long id, Status status);
}
