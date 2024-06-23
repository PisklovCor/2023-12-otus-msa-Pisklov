package ru.otus.hw.services;

import ru.otus.hw.models.Notification;

import java.util.List;

public interface NotificationService {

    Notification createNotification(Notification notification);

    List<Notification> findAll();

    List<Notification> findAccountByAccountId(long accountId);

}
