package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.client.LibraryClient;
import ru.otus.hw.dto.CreatOrderDto;
import ru.otus.hw.dto.out.CreatBookApiOrderDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Order;
import ru.otus.hw.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final LibraryClient libraryClient;

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

        Order order = orderRepository.updateOrder(optionalOrder.get(), status);

        if (status.equals("added")) {
            CreatBookApiOrderDto creatBookApiOrderDto = new CreatBookApiOrderDto();
            creatBookApiOrderDto.setAccountId(order.getAccountId());
            creatBookApiOrderDto.setEmail(order.getEmail());
            creatBookApiOrderDto.setTitle(order.getTitle());
            creatBookApiOrderDto.setAuthor(order.getAuthor());
            creatBookApiOrderDto.setRating(ThreadLocalRandom.current().nextInt(1, 11));

            log.info("Отправляем книгу в бибилиотеку=[{}]", creatBookApiOrderDto);
            libraryClient.sendLibraryServiceBook(creatBookApiOrderDto);
        }

        return order;
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
