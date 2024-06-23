package ru.otus.hw.repositories;

import ru.otus.hw.models.AuthUser;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {

    Optional<AuthUser> findByLoginPassword(String login, String password);

    List<AuthUser> findAll();

    AuthUser create(AuthUser authUser);

    void deleteAccount(long accountId);
}
