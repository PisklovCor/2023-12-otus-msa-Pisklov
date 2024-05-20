package ru.otus.hw.repositories;

import ru.otus.hw.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order create(Order order);

    Optional<Order> findByLogin(String id);

    List<Order> findAll();
}
