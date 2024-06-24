package ru.otus.hw.http;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.converters.NotificationConverter;
import ru.otus.hw.dto.NotificationDto;
import ru.otus.hw.services.NotificationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    private final NotificationConverter converter;

    @Operation(summary = "Оставить заявку на добавление книги")
    @PostMapping("/api/notification/create")
    public ResponseEntity<NotificationDto> createOrder(@RequestBody NotificationDto dto) {


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converter.mapModelToDto(service.createNotification(converter.mapDtoToModel(dto))));

    }

    @Operation(summary = "Посмотреть все уведомления")
    @GetMapping("/api/notification")
    public ResponseEntity<List<NotificationDto>> getAllOrder() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.findAll().stream().map(converter::mapModelToDto).toList());
    }

    @Operation(summary = "Посмотреть уведомления пользоваетля")
    @GetMapping("/api/notification/{accountId}")
    public ResponseEntity<List<NotificationDto>> getOrder(@PathVariable long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                service.findAccountByAccountId(accountId).stream().map(converter::mapModelToDto).toList());
    }
}
