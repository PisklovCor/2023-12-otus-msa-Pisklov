package ru.otus.hw.http;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.dto.OrderDto;
import ru.otus.hw.dto.ResponseDto;
import ru.otus.hw.models.Order;
import ru.otus.hw.services.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/health")
    public ResponseDto getHealth() {
        return ResponseDto.builder().status("OK").build();
    }

    @PostMapping("/api/create-order")
    public Order createOrder(@RequestBody OrderDto orderDto) {
        return orderService.create(orderDto.getLogin(), orderDto.getSum());
    }

    @GetMapping("api/order/{login}")
    public Order getOrder(@PathVariable String login) {
        return orderService.findByLogin(login).get();
    }

    @GetMapping("api/all-order")
    public List<Order> getAllOrder() {
        return orderService.findAll();
    }
}
