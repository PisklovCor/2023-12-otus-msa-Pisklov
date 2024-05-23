package ru.otus.hw.services;

import ru.otus.hw.dto.CreateOrderDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.dto.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDto create(CreateOrderDto dto);

    OrderDto findByUUID(UUID uuid);

    OrderDto findByLogin(String login);

    List<OrderDto> findAll();

    void updateOrderStatus(long id, OrderStatus status);
}
