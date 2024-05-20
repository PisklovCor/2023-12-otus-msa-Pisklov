package ru.otus.hw.http;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.dto.ResponseDto;
import ru.otus.hw.models.Notification;
import ru.otus.hw.services.NotificationService;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/health")
    public ResponseDto getHealth() {
        return ResponseDto.builder().status("OK").build();
    }

    @GetMapping("/api/notifications")
    public List<Notification> getSessions() {
        return notificationService.findAll();
    }

    @GetMapping("/api/notification/{login}")
    public Notification getNoNotificationByLogin(@PathVariable String login) {
        return notificationService.findByLogin(login).get();
    }
}
