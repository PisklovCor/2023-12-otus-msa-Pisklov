package ru.otus.hw.http;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.dto.CreateOrderDto;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.dto.Status;
import ru.otus.hw.services.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/api/order/health-service")
    public ResponseEntity<?> getHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/order/create-order")
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(createOrderDto));
    }

    @GetMapping("/api/order/uuid/{uuid}")
    public ResponseEntity<OrderDto> getOrderUUID(@PathVariable UUID uuid) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findByUUID(uuid));
    }

    @GetMapping("/api/order/login/{login}")
    public ResponseEntity<OrderDto> getOrderLogin(@PathVariable String login) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findByLogin(login));
    }

    @GetMapping("/api/order/all-order")
    public ResponseEntity<List<OrderDto>> getAllOrder() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findAll());
    }

    @PutMapping("/api/order/update-status/{id}")
    public ResponseEntity<?> getAllOrder(@PathVariable long id, @RequestBody Status status) {
        orderService.updateOrderStatus(id, status);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
