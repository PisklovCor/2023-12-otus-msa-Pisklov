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
import ru.otus.hw.models.Account;
import ru.otus.hw.services.AccountService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/api/account/health-service")
    public ResponseEntity<?> getHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/account/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(account));
    }

    @GetMapping("/api/account/login/{login}")
    public ResponseEntity<Account> getAccountLogin(@PathVariable String login) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountService.findByLogin(login));
    }

    @GetMapping("/api/account/all-account")
    public ResponseEntity<List<Account>> getAllPayment() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(accountService.findAll());
    }

    @PutMapping("/api/account/update-account")
    public ResponseEntity<?> getAllAccount(@RequestBody Account account) {
        accountService.updateAccount(account);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
