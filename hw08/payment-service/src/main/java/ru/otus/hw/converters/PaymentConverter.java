package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.in.PaymentDto;
import ru.otus.hw.models.Payment;

@Component
public class PaymentConverter {

    public PaymentDto mapModelToDto(Payment model) {
        PaymentDto dto = new PaymentDto();
        dto.setId(model.getId());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setOrderId(model.getOrderId());
        dto.setLogin(model.getLogin());
        dto.setAccountInvoice(model.getAccountInvoice());
        dto.setDescriptionOrder(model.getDescriptionOrder());
        dto.setSumOrder(model.getSumOrder());
        dto.setStatus(model.getStatus());
        return dto;
    }
}
