package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.JmsMessage;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Notification;
import ru.otus.hw.repositories.NotificationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification create(JmsMessage message) {

        Notification notification = new Notification();
        notification.setLogin(message.getLogin());
        notification.setAccountInvoice(message.getAccountInvoice());
        notification.setSumOrder(message.getSum());
        notification.setMessage(creatingMessage(message));

        return notificationRepository.create(notification);
    }

    @Override
    public List<Notification> findAll() {
         return  notificationRepository.findAll();
    }

    @Override
    public Optional<Notification> findByLogin(String login) {
        var notification = notificationRepository.findByLogin(login);

        if (notification.isEmpty()) {
            throw new EntityNotFoundException("One notification with logins %s not found".formatted(login));
        }
        return notification;
    }

    private String creatingMessage(JmsMessage message) {

        if (message.isStatus()) {
            return "Добрый день, %s, Ваш заказ на сумму %d создан и оплачен".formatted(
                    message.getLogin(),
                    message.getSum());
        } else {
            return ("Добрый день, %s, Ваш заказ на сумму %d отклонен." +
                    " Проверьте пожалуйста состояние Вашего счета").formatted(
                    message.getLogin(),
                    message.getSum());
        }
    }
}
