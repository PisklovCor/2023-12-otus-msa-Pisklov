package ru.otus.hw.repositories;

import ru.otus.hw.models.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    Notification create(Notification notification);

    List<Notification> findAll();

    Optional<Notification> findByLogin(String login);
}
