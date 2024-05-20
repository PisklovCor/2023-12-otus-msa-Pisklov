package ru.otus.hw.services;

import ru.otus.hw.dto.JmsMessage;
import ru.otus.hw.models.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {

    Notification create(JmsMessage notification);

    List<Notification> findAll();

    Optional<Notification> findByLogin(String login);

}
