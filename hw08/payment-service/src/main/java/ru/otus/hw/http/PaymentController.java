package ru.otus.hw.http;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.dto.in.PaymentDto;
import ru.otus.hw.dto.Status;
import ru.otus.hw.services.PaymentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/api/payment/health-service")
    public ResponseEntity<?> getHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/payment/uuid/{uuid}")
    public ResponseEntity<PaymentDto> getPaymentUUID(@PathVariable UUID uuid) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentService.findByUUID(uuid));
    }

    @GetMapping("/api/payment/login/{login}")
    public ResponseEntity<PaymentDto> getPaymentLogin(@PathVariable String login) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentService.findByLogin(login));
    }

    @GetMapping("/api/payment/all-payment")
    public ResponseEntity<List<PaymentDto>> getAllPayment() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentService.findAll());
    }

    @PutMapping("/api/payment/update-status/{id}")
    public ResponseEntity<?> getAllPayment(@PathVariable long id, @RequestBody Status status) {
        paymentService.updatePaymentStatus(id, status);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
