package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Notification;
import ru.otus.hw.repositories.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(Notification notification) {
        return notificationRepository.createOrder(notification);
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> findAccountByAccountId(long accountId) {
        return notificationRepository.findNotificationByAccountId(accountId);
    }
}
