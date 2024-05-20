package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Account;
import ru.otus.hw.repositories.AccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.createAccount(account);
    }


    @Override
    public Optional<Account> findAccountByLogin(String login) {
        var account = accountRepository.findAccountByLogin(login);

        if (account.isEmpty()) {
            throw new EntityNotFoundException("One user with account %s not found".formatted(login));
        }
         return account;
    }

    @Override
    public Account updateMoney(String login, Integer money) {
        var account = accountRepository.findAccountByLogin(login);

        if (account.isEmpty()) {
            throw new EntityNotFoundException("One user with account %s not found".formatted(login));
        }

        return accountRepository.updateMoney(account.get(), money);
    }
}
