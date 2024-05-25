package ru.otus.hw.repositories;

import ru.otus.hw.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Account create(Account account);

    Optional<Account> findByLogin(String login);

    List<Account> findAll();

    void updateAccount(Account account);
}
