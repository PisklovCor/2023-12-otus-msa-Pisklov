package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Account;
import ru.otus.hw.repositories.AccountRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account create(Account account) {

        log.info("Будет создана запись о аккаунте");

        return accountRepository.create(account);
    }

    @Override
    public Account findByLogin(String login) {
        var account = accountRepository.findByLogin(login);
        return account.orElseThrow(
                () -> new EntityNotFoundException("Account with login %s not found".formatted(login)));
    }

    @Override
    public List<Account> findAll() {

        return new ArrayList<>(accountRepository.findAll());
    }

    @Override
    public void updateAccount(Account account) {
        accountRepository.updateAccount(account);
    }
}
