package ru.otus.hw.services;

import ru.otus.hw.dto.in.JmsMessageOrder;
import ru.otus.hw.dto.in.JmsMessageStore;
import ru.otus.hw.dto.in.PaymentDto;
import ru.otus.hw.dto.Status;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PaymentDto create(JmsMessageOrder dto);

    PaymentDto findByUUID(UUID uuid);

    PaymentDto findByLogin(String login);

    List<PaymentDto> findAll();

    void updatePaymentStatus(long id, Status status);

    void updatePaymentStatusAndMessageSend(JmsMessageStore jmsMessageStore);
}
