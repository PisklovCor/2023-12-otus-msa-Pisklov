package ru.otus.hw.repositories;

import ru.otus.hw.models.Notification;

import java.util.List;

public interface NotificationRepository {

    Notification createOrder(Notification notification);

    List<Notification> findAll();

    List<Notification> findNotificationByAccountId(long accountId);
}
