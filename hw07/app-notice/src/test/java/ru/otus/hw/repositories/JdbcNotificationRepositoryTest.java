package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Notification;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий на основе Jpa для работы с уведомлениями ")
@JdbcTest
@Import(JdbcNotificationRepository.class)
class JdbcNotificationRepositoryTest {

    @Autowired
    private NotificationRepository repository;

    @DisplayName("должен создать уведомление")
    @Test
    void create() {

        Notification notification = new Notification();
        notification.setLogin("test");
        notification.setAccountInvoice(UUID.randomUUID());
        notification.setSumOrder(5000);
        notification.setMessage("test");

        var insertNotification =  repository.create(notification);

        assertThat(insertNotification).isNotNull()
                .matches(n -> n.getId() > 0);

        assertThat(repository.findByLogin(insertNotification.getLogin()))
                .isPresent();
        assertThat(repository.findByLogin(insertNotification.getLogin()).get()
                .getSumOrder()).isEqualTo(insertNotification.getSumOrder());

    }

    @DisplayName("должен загрузить список всех уведомлений")
    @Test
    void findAll() {
        var notificationList = repository.findAll();

        assertThat(notificationList).isNotNull().size().isEqualTo(1);
    }

    @DisplayName("должен загрузить уведомление по логину аккаунта")
    @Test
    void findByLogin() {
        var notification =  repository.findByLogin("admin");

        assertThat(notification).isPresent();
        assertThat(notification.get().getLogin()).isEqualTo("admin");
    }
}