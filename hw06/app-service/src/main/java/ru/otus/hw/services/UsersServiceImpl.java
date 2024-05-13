package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.AuthUser;
import ru.otus.hw.repositories.UsersRepository;

import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public Optional<AuthUser> findById(String id) {
        var user = usersRepository.findById(id);

        if (user.isEmpty()) {
            throw new EntityNotFoundException("One user with ids %s not found".formatted(id));
        }
         return user;
    }

    @Override
    public AuthUser create(String id , String login) {
        return usersRepository.create(id, login);
    }
}
