package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Users;
import ru.otus.hw.repositories.UsersRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Override
    public Optional<Users> findById(long id) {
        var user = usersRepository.findById(id);

        if (user.isEmpty()) {
            throw new EntityNotFoundException("One user with ids %s not found".formatted(id));
        }
         return user;
    }

    @Override
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    @Override
    public Users save(Users users) {
        return usersRepository.save(users);
    }

    @Override
    public void deleteById(long id) {
        usersRepository.deleteById(id);
    }
}
