package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.client.ArtemisProduceSender;
import ru.otus.hw.converters.PaymentConverter;
import ru.otus.hw.dto.in.JmsMessageOrderToPayment;
import ru.otus.hw.dto.in.JmsMessageStoreToPayment;
import ru.otus.hw.dto.in.PaymentDto;
import ru.otus.hw.dto.Status;
import ru.otus.hw.dto.out.JmsMessagePaymentToOrder;
import ru.otus.hw.dto.out.JmsMessagePaymentToStore;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Account;
import ru.otus.hw.models.Payment;
import ru.otus.hw.repositories.PaymentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.otus.hw.dto.Status.CREATED;
import static ru.otus.hw.dto.Status.WAIT;
import static ru.otus.hw.dto.Status.CONFIRMED;
import static ru.otus.hw.dto.Status.CANCELED;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final AccountService accountService;

    private final PaymentConverter paymentConverter;

    private final ArtemisProduceSender artemisProduceSender;

    @Override
    public PaymentDto create(JmsMessageOrderToPayment dto) {

        log.info("Получен платеж от пользователя [{}] на сумму [{}]", dto.getLogin(), dto.getSumOrder());

        if (!checkingAccountBalance(dto)) {
            return null;
        }
        Payment payment = new Payment();
        payment.setOrderId(dto.getOrderId());
        payment.setLogin(dto.getLogin());
        payment.setAccountInvoice(dto.getAccountInvoice());
        payment.setDescriptionOrder(dto.getDescriptionOrder());
        payment.setSumOrder(dto.getSumOrder());
        payment.setStatus(CREATED.toString());

        log.info("Будет создана запись о платеже");
        final PaymentDto paymentDto = paymentConverter.mapModelToDto(paymentRepository.create(payment));
        sendMessageJms(paymentDto);
        paymentRepository.updatePaymentStatus(paymentDto.getId(), WAIT);

        return paymentDto;
    }

    @Override
    public PaymentDto findByUUID(UUID uuid) {
        var order = paymentRepository.findByUUID(uuid);
        return paymentConverter.mapModelToDto(order.orElseThrow(
                () -> new EntityNotFoundException("Payment with uuid %s not found".formatted(uuid))));
    }

    @Override
    public PaymentDto findByLogin(String login) {
        var order = paymentRepository.findByLogin(login);
        return paymentConverter.mapModelToDto(order.orElseThrow(
                () -> new EntityNotFoundException("Payment with id %s not found".formatted(login))));
    }

    @Override
    public List<PaymentDto> findAll() {
        List<PaymentDto> paymentDtoList =  new ArrayList<>();

        paymentRepository.findAll().forEach(o -> paymentDtoList.add(paymentConverter.mapModelToDto(o)));

        return paymentDtoList;
    }

    @Override
    public void updatePaymentStatus(long id, Status status) {
        paymentRepository.updatePaymentStatus(id, status);
    }

    @Override
    public void updatePaymentStatusAndMessageSend(JmsMessageStoreToPayment jmsMessageStoreToPayment) {
        updatePaymentStatus(jmsMessageStoreToPayment.getPaymentId(), jmsMessageStoreToPayment.getStatus());
        log.info("Состояние платежа обновлено");
        JmsMessagePaymentToOrder message = new JmsMessagePaymentToOrder();
        message.setOrderId(jmsMessageStoreToPayment.getOrderId());
        message.setStatus(jmsMessageStoreToPayment.getStatus());
        if (jmsMessageStoreToPayment.getStatus() == CONFIRMED) {
            log.info("Saga завершилась успешно, сообщение: " + message);
            artemisProduceSender.sendMessageOrder(message);
        } else {
            log.warn("Saga откатилась, сообщение: " + message);

            Payment payment = paymentRepository.findById(jmsMessageStoreToPayment.getPaymentId()).orElseThrow(
                    () -> new EntityNotFoundException("Payment with id %s not found"
                            .formatted(jmsMessageStoreToPayment.getPaymentId())));

            Account account = accountService.findByLogin(payment.getLogin());
            int originalBalance = account.getBalance();

            account.setBalance(originalBalance + payment.getSumOrder());
            accountService.updateAccount(account);
            artemisProduceSender.sendMessageOrder(message);
        }
    }

    private boolean checkingAccountBalance(JmsMessageOrderToPayment dto) {
        Account account = accountService.findByLogin(dto.getLogin());
        Integer balance = account.getBalance();
        Integer sumOrder = dto.getSumOrder();
        if (balance >= sumOrder) {
            log.info("С баланса снимаем сумму заказа");
            account.setBalance(balance - sumOrder);
            accountService.updateAccount(account);
            return true;
        } else {
            log.warn("Недостаточно денег, отменяем заказ");
            JmsMessagePaymentToOrder message = new JmsMessagePaymentToOrder();
            message.setOrderId(dto.getOrderId());
            message.setStatus(CANCELED);
            artemisProduceSender.sendMessageOrder(message);
            return false;
        }
    }

    private void sendMessageJms(PaymentDto paymentDto) {

        try {
            JmsMessagePaymentToStore jmsMessagePaymentToStore = new JmsMessagePaymentToStore();
            jmsMessagePaymentToStore.setPaymentId(paymentDto.getId());
            jmsMessagePaymentToStore.setOrderId(paymentDto.getOrderId());
            jmsMessagePaymentToStore.setCreatedAtOrder(paymentDto.getCreatedAt());
            jmsMessagePaymentToStore.setLogin(paymentDto.getLogin());
            jmsMessagePaymentToStore.setAccountInvoice(paymentDto.getAccountInvoice());
            jmsMessagePaymentToStore.setDescriptionOrder(paymentDto.getDescriptionOrder());
            jmsMessagePaymentToStore.setSumOrder(paymentDto.getSumOrder());
            artemisProduceSender.sendMessageStore(jmsMessagePaymentToStore);
            log.info("Сообщение успешно отправлено в брокер [{}]", jmsMessagePaymentToStore);
        } catch (Exception e) {
            log.error("Failed to send message to broker:" + e.getMessage());
        }
    }
}
