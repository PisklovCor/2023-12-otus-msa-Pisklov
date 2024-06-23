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
import ru.otus.hw.converters.OrderConverter;
import ru.otus.hw.dto.CreatOrderDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.services.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    private final OrderConverter converter;

    @Operation(summary = "Оставить заявку на добавление книги")
    @PostMapping("/api/order/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreatOrderDto dto) {

        var order = service.findOrderByTitleAndAuthor(dto);

        return order.map(value -> ResponseEntity.status(HttpStatus.ACCEPTED).body(converter.mapModelToDto(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CREATED).body(
                converter.mapModelToDto(service.createOrder(converter.mapDtoToModel(dto)))));

    }

    @Operation(summary = "Посмотреть все заявки")
    @GetMapping("/api/order")
    public ResponseEntity<List<OrderDto>> getAllOrder() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.findAll().stream().map(converter::mapModelToDto).toList());
    }

    @Operation(summary = "Посмотреть заявку пользоваетля")
    @GetMapping("/api/order/{accountId}")
    public ResponseEntity<List<OrderDto>> getOrder(@PathVariable long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                service.findAccountByAccountId(accountId).stream().map(converter::mapModelToDto).toList());
    }
}