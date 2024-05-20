package ru.otus.hw.repositories;

import ru.otus.hw.models.Account;

import java.util.Optional;

public interface AccountRepository {

    Account createAccount(Account account);

    Optional<Account> findAccountByLogin(String login);

    Account updateMoney(Account account, Integer money);
}
