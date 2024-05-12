package ru.otus.hw.repositories;

import ru.otus.hw.models.Users;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {

    Optional<Users> findById(long id);

    List<Users> findAll();

    Users save(Users users);

    void deleteById(long id);
}
