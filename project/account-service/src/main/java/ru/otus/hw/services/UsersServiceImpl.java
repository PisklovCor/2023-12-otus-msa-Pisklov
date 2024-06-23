package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.AuthUser;
import ru.otus.hw.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public Optional<AuthUser> findByLoginPassword(String login, String password) {
        var user = usersRepository.findByLoginPassword(login, password);

        if (user.isEmpty()) {
            throw new EntityNotFoundException("One user with login %s not found".formatted(login));
        }
         return user;
    }

    @Override
    public List<AuthUser> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public AuthUser create(AuthUser authUser) {
        return usersRepository.create(authUser);
    }

    public void deleteAccount(long accountId) {
        usersRepository.deleteAccount(accountId);
    }
}
