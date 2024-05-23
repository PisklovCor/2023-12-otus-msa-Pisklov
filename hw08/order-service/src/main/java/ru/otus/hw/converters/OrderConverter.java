package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.models.Order;

@Component
public class OrderConverter {

    public OrderDto mapModelToDto(Order model) {
        OrderDto dto = new OrderDto();
        dto.setId(model.getId());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setLogin(model.getLogin());
        dto.setAccountInvoice(model.getAccountInvoice());
        dto.setDescriptionOrder(model.getDescriptionOrder());
        dto.setSumOrder(model.getSumOrder());
        dto.setStatus(model.getStatus());
        return dto;
    }
}
