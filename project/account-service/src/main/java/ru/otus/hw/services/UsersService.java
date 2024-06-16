package ru.otus.hw.services;

import ru.otus.hw.models.AuthUser;

import java.util.Optional;

public interface UsersService {

    Optional<AuthUser> findByLoginPassword(String login, String password);

    AuthUser create(AuthUser authUser);

}
