package ru.otus.hw.services;

import ru.otus.hw.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order create(String login, Integer sum);

    Optional<Order> findByLogin(String login);

    List<Order> findAll();
}
