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
import ru.otus.hw.models.Store;
import ru.otus.hw.services.StoreService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/api/store/health-service")
    public ResponseEntity<?> getHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/store/uuid/{uuid}")
    public ResponseEntity<Store> getStoreUUID(@PathVariable UUID uuid) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(storeService.findByUUID(uuid));
    }

    @GetMapping("/api/store/login/{login}")
    public ResponseEntity<Store> getStoreLogin(@PathVariable String login) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(storeService.findByLogin(login));
    }

    @GetMapping("/api/store/all-store")
    public ResponseEntity<List<Store>> getAllStore() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(storeService.findAll());
    }

    @PutMapping("/api/store/update-status/{id}")
    public ResponseEntity<?> getAllStore(@PathVariable long id, @RequestBody Status status) {
        storeService.updateStoreStatus(id, status);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
