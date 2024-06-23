package ru.otus.hw.services;

import ru.otus.hw.dto.CreatOrderDto;
import ru.otus.hw.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createOrder(Order order);

    Order updateOrder(long orderId, String status);

    List<Order> findAll();

    List<Order> findOrderByAccountId(long accountId);

    Optional<Order> findOrderByTitleAndAuthor(CreatOrderDto dto);

    void deleteOrder(long orderId);
}
