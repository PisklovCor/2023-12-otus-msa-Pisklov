package ru.otus.hw.services;

import ru.otus.hw.models.Account;

import java.util.Optional;

public interface AccountService {

    Account createAccount(Account account);

    Optional<Account> findAccountByLogin(String login);

    Account updateMoney(String login, Integer money);

}
