package ru.otus.hw.repositories;

import ru.otus.hw.dto.Status;
import ru.otus.hw.models.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order create(Order order);

    Optional<Order> findByUUID(UUID uuid);

    Optional<Order> findByLogin(String login);

    List<Order> findAll();

    void updateOrderStatus(long id, Status status);
}
