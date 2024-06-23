package ru.otus.hw.http;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.hw.converters.OrderConverter;
import ru.otus.hw.dto.CreatOrderDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.exceptions.IdempotentRequestsException;
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
                service.findOrderByAccountId(accountId).stream().map(converter::mapModelToDto).toList());
    }

    @Operation(summary = "Обновить заявку книгу")
    @PatchMapping("/api/order/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable long orderId, @RequestParam String status) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                converter.mapModelToDto(service.updateOrder(orderId, status)));
    }

    @Operation(summary = "Удалить заявку по ID")
    @DeleteMapping("/api/order/admin/{orderId}")
    public ResponseEntity<Void> deleteOrder(HttpServletRequest request, @PathVariable long orderId) {

        var userLogin = request.getHeader("X-User");

        if (userLogin == null) {
            throw new IdempotentRequestsException("Empty header");
        }

        if (!userLogin.equals("admin")) {
            throw new IdempotentRequestsException("Error user not ADMIN");
        }

        service.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
