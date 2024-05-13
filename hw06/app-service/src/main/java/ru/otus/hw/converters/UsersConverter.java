package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AuthUserDto;
import ru.otus.hw.models.AuthUser;

@Component
public class UsersConverter {
    public AuthUser mapDtoToModel(AuthUserDto dto) {
        AuthUser authUser = new AuthUser();
        authUser.setLogin(dto.getLogin());
        authUser.setAge(dto.getAge());
        return authUser;
    }
}
