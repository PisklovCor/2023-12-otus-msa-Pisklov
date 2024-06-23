package ru.otus.hw.repositories;

import ru.otus.hw.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order createOrder(Order order);

    List<Order> findAll();

    List<Order> findOrderByAccountId(long accountId);

    Optional<Order> findOrderByTitleAndAuthor(String title, String author);
}
