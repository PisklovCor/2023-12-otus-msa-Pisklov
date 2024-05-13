package ru.otus.hw.repositories;

import ru.otus.hw.models.AuthUser;

import java.util.Optional;

public interface UsersRepository {

    Optional<AuthUser> findById(String id);

    AuthUser create(String id, String login);
}
