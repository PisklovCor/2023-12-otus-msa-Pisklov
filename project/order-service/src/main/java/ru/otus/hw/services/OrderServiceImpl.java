package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.CreatOrderDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Order;
import ru.otus.hw.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        return orderRepository.createOrder(order);
    }

    @Override
    public Order updateOrder(long orderId, String status) {

        var optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isEmpty()) {
            throw new EntityNotFoundException("One user with ids %s not found".formatted(orderId));
        }

        return orderRepository.updateOrder(optionalOrder.get(), status);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findOrderByAccountId(long accountId) {
        return orderRepository.findOrderByAccountId(accountId);
    }

    @Override
    public Optional<Order> findOrderByTitleAndAuthor(CreatOrderDto dto) {
        return orderRepository.findOrderByTitleAndAuthor(dto.getTitle(), dto.getAuthor());
    }

    @Override
    public void deleteOrder(long orderId) {
        orderRepository.deleteOrder(orderId);
    }
}
