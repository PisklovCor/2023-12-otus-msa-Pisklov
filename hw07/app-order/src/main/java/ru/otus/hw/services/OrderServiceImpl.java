package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.client.AccountClient;
import ru.otus.hw.client.ArtemisProducer;
import ru.otus.hw.dto.AccountDto;
import ru.otus.hw.dto.JmsMessage;
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

    private final ArtemisProducer artemisProducer;

    @Override
    public Order create(String login, Integer sum) {

        log.info("Получен заказ от пользователя [{}] на сумму [{}]", login, sum);

        AccountDto accountDto = accountClient.getAccountInfo(login);

        log.info("Найден пользователь [{}]", accountDto);

        Order order = new Order();
        order.setLogin(login);
        order.setAccountInvoice(accountDto.getInvoice());
        order.setSumOrder(sum);

        boolean orderStatus = financialTransactions(accountDto, sum);
        order.setStatus(orderStatus);

        log.info("Будет создана запись о заказе [{}]", order);

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

    private boolean financialTransactions(AccountDto accountDto, Integer sum) {

        final String login = accountDto.getLogin();
        final Integer originalMoney = accountDto.getMoney();

        if (accountDto.getMoney() > sum) {
            log.info("У пользователя есть деньги на заказ [{}]", originalMoney);

            accountClient.withdrawal(login, sum);
            log.info("У пользователя [{}] списываются деньги в размере [{}]", login, sum);

            return tryingSendAnUrgentMessage(login, originalMoney, sum);
        } else {
            log.info("У пользователя не хватает денег на заказ");
            sendMessageJms(accountDto, false, sum);
            return false;
        }
    }

    private boolean tryingSendAnUrgentMessage(String login, Integer originalMoney, Integer sum) {

        AccountDto checkAccountDto = accountClient.getAccountInfo(login);

         if (originalMoney > checkAccountDto.getMoney()) {
             log.info("У пользователя [{}] списаны деньги, баланс [{}], формирует сообщение",
                     login, checkAccountDto.getMoney());
             sendMessageJms(checkAccountDto, true, sum);
             return true;
         } else {
             log.error("У пользователя [{}] деньги не списаны, баланс [{}]б формирует сообщение",
                     login, checkAccountDto.getMoney());
             sendMessageJms(checkAccountDto, false, sum);
             return false;
         }
    }

    private void sendMessageJms(AccountDto checkAccountDto, boolean status, Integer sum) {

        try {
            JmsMessage jmsMessage = new JmsMessage();
            jmsMessage.setLogin(checkAccountDto.getLogin());
            jmsMessage.setAccountInvoice(checkAccountDto.getInvoice());
            jmsMessage.setSum(sum);
            jmsMessage.setStatus(status);

            artemisProducer.sendMessage(jmsMessage);
            log.info("Сообщение успешно отправлено в брокер [{}]", jmsMessage);
        } catch (Exception e) {
            log.error("Failed to send message to broker:" + e.getMessage());
        }
    }
}
