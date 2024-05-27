package ru.otus.hw.http;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.dto.Status;
import ru.otus.hw.models.Delivery;
import ru.otus.hw.services.DeliveryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/api/delivery/health-service")
    public ResponseEntity<?> getHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/delivery/uuid/{uuid}")
    public ResponseEntity<Delivery> getPaymentUUID(@PathVariable UUID uuid) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deliveryService.findByUUID(uuid));
    }

    @GetMapping("/api/delivery/login/{login}")
    public ResponseEntity<Delivery> getPaymentLogin(@PathVariable String login) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deliveryService.findByLogin(login));
    }

    @GetMapping("/api/delivery/all-store")
    public ResponseEntity<List<Delivery>> getAllPayment() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deliveryService.findAll());
    }

    @PutMapping("/api/store/update-status/{id}")
    public ResponseEntity<?> getAllOrder(@PathVariable long id, @RequestBody Status status) {
        deliveryService.updateDeliveryStatus(id, status);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
