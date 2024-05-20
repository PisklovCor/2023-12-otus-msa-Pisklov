package ru.otus.hw.http;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.converters.AccountConverter;
import ru.otus.hw.dto.ResponseDto;
import ru.otus.hw.dto.AccountDto;
import ru.otus.hw.models.Account;
import ru.otus.hw.services.AccountService;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final AccountConverter accountConverter;

    @GetMapping("/health")
    public ResponseDto getHealth() {
        return ResponseDto.builder().status("OK").build();
    }

    @GetMapping("/api/account/{login}")
    public Account getAccount(@PathVariable String login) {
        return accountService.findAccountByLogin(login).get();
    }

    @PostMapping("/api/register")
    public Account registerAccount(@RequestBody AccountDto accountDto) {
        return accountService.createAccount(accountConverter.mapDtoToModel(accountDto));
    }

    @PutMapping("/api/refill/{login}")
    public Account refill(@PathVariable String login, @RequestBody Integer money) {
        return accountService.updateMoney(login, money);
    }
}
