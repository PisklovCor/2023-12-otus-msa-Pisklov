package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.NotificationDto;
import ru.otus.hw.models.Notification;

@Component
public class NotificationConverter {
    public Notification mapDtoToModel(NotificationDto dto) {
        Notification notification = new Notification();
        notification.setAccountId(dto.getAccountId());
        notification.setEmail(dto.getEmail());
        notification.setMessage(dto.getMessage());
        notification.setTitle(dto.getTitle());
        notification.setAuthor(dto.getAuthor());
        return notification;
    }

    public NotificationDto mapModelToDto(Notification model) {
        NotificationDto dto = new NotificationDto();
        dto.setAccountId(model.getAccountId());
        dto.setEmail(model.getEmail());
        dto.setMessage(model.getMessage());
        dto.setTitle(model.getTitle());
        dto.setAuthor(model.getAuthor());
        return dto;
    }
}
