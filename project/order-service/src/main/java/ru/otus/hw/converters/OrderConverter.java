package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CreatOrderDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.models.Order;

@Component
public class OrderConverter {
    public Order mapDtoToModel(CreatOrderDto dto) {
        Order order = new Order();
        order.setAccountId(dto.getAccountId());
        order.setEmail(dto.getEmail());
        order.setTitle(dto.getTitle());
        order.setAuthor(dto.getAuthor());
        return order;
    }

    public OrderDto mapModelToDto(Order model) {
        OrderDto dto = new OrderDto();
        dto.setAccountId(model.getAccountId());
        dto.setEmail(model.getEmail());
        dto.setTitle(model.getTitle());
        dto.setAuthor(model.getAuthor());
        dto.setStatus(model.getStatus());
        return dto;
    }
}
