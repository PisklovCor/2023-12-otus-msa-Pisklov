package ru.otus.hw.repositories;

import ru.otus.hw.dto.Status;
import ru.otus.hw.models.Payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment create(Payment payment);

    Optional<Payment> findById(long id);

    Optional<Payment> findByUUID(UUID uuid);

    Optional<Payment> findByLogin(String login);

    List<Payment> findAll();

    void updatePaymentStatus(long id, Status status);
}
