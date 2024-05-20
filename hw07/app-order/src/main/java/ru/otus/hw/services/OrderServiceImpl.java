package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.client.AccountClient;
import ru.otus.hw.dto.AccountDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Order;
import ru.otus.hw.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final AccountClient accountClient;

    @Override
    public Order create(String login, Integer sum) {

        log.info("Получен заказ от пользователя [{}] на сумму [{}]", login, sum);

        AccountDto accountDto = accountClient.getAccountInfo(login);

        log.info("Найден пользователь [{}]", accountDto);

        Order order = new Order();
        order.setLogin(login);
        order.setAccountInvoice(accountDto.getInvoice());
        order.setSumOrder(sum);

        if (accountDto.getMoney() > sum) {
            log.info("У пользоваетля есть деньги");
            order.setStatus(true);
        } else {
            log.info("У пользоваетля нет денег");
            order.setStatus(false);
        }

        //TODO: осмотр денег и отправка сообщения в брокер

        return orderRepository.create(order);
    }

    @Override
    public Optional<Order> findByLogin(String login) {
        var order = orderRepository.findByLogin(login);

        if (order.isEmpty()) {
            throw new EntityNotFoundException("One order with logins %s not found".formatted(login));
        }
        return order;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
