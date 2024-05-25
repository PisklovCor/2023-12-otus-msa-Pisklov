package ru.otus.hw.services;

import ru.otus.hw.models.Account;

import java.util.List;

public interface AccountService {

    Account create(Account account);

    Account findByLogin(String login);

    List<Account> findAll();

    void updateAccount(Account account);
}
