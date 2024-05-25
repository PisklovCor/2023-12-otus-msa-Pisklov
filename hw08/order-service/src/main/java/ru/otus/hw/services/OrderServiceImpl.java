package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.client.ArtemisProducer;
import ru.otus.hw.converters.OrderConverter;
import ru.otus.hw.dto.CreateOrderDto;
import ru.otus.hw.dto.JmsMessageOrder;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.dto.Status;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Order;
import ru.otus.hw.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.otus.hw.dto.Status.CREATED;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderConverter orderConverter;

    private final ArtemisProducer artemisProducer;

    @Override
    public OrderDto create(CreateOrderDto dto) {

        log.info("Получен заказ от пользователя [{}] на сумму [{}]", dto.getLogin(), dto.getSumOrder());

        Order order = new Order();
        order.setLogin(dto.getLogin());
        order.setAccountInvoice(UUID.randomUUID());
        order.setDescriptionOrder(dto.getDescriptionOrder());
        order.setSumOrder(dto.getSumOrder());
        order.setStatus(CREATED.toString());

        log.info("Будет создана запись о заказе [{}]", order);

        final OrderDto orderDto = orderConverter.mapModelToDto(orderRepository.create(order));

        sendMessageJms(orderDto);

        return orderDto;
    }

    @Override
    public OrderDto findByUUID(UUID uuid) {
        var order = orderRepository.findByUUID(uuid);
        return orderConverter.mapModelToDto(order.orElseThrow(
                () -> new EntityNotFoundException("Order with uuid %s not found".formatted(uuid))));
    }

    @Override
    public OrderDto findByLogin(String login) {
        var order = orderRepository.findByLogin(login);
        return orderConverter.mapModelToDto(order.orElseThrow(
                () -> new EntityNotFoundException("Order with id %s not found".formatted(login))));
    }

    @Override
    public List<OrderDto> findAll() {
        List<OrderDto> orderDtoList =  new ArrayList<>();

        orderRepository.findAll().forEach(o -> orderDtoList.add(orderConverter.mapModelToDto(o)));

        return orderDtoList;
    }

    @Override
    public void updateOrderStatus(long id, Status status) {
        orderRepository.updateOrderStatus(id, status);
    }

    private void sendMessageJms(OrderDto orderDto) {

        try {
            JmsMessageOrder jmsMessageOrder = new JmsMessageOrder();
            jmsMessageOrder.setOrderId(orderDto.getId());
            jmsMessageOrder.setCreatedAtOrder(orderDto.getCreatedAt());
            jmsMessageOrder.setLogin(orderDto.getLogin());
            jmsMessageOrder.setAccountInvoice(orderDto.getAccountInvoice());
            jmsMessageOrder.setDescriptionOrder(orderDto.getDescriptionOrder());
            jmsMessageOrder.setSumOrder(orderDto.getSumOrder());

            artemisProducer.sendMessage(jmsMessageOrder);
            log.info("Сообщение успешно отправлено в брокер [{}]", jmsMessageOrder);
        } catch (Exception e) {
            log.error("Failed to send message to broker:" + e.getMessage());
        }
    }
}
