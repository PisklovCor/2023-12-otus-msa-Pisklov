package ru.otus.hw.repositories;

import ru.otus.hw.models.AccountBook;

import java.util.List;
import java.util.Optional;

public interface AccountBookRepository {

    List<AccountBook> findAll();

    Optional<AccountBook> findByAccountId(long accountId);

    AccountBook create(AccountBook accountBook);

}
