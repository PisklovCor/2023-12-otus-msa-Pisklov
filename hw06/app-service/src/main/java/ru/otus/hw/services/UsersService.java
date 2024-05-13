package ru.otus.hw.services;

import ru.otus.hw.models.AuthUser;

import java.util.Optional;

public interface UsersService {

    Optional<AuthUser> findById(String id);

    AuthUser create(String id, String login);

}
