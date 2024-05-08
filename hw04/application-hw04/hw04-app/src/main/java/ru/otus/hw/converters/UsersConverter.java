package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.UsersBaseDto;
import ru.otus.hw.models.Users;

@Component
public class UsersConverter {
    public Users mapDtoToModel(UsersBaseDto dto, long userId) {
        Users users = new Users();
        users.setLogin(dto.getLogin());
        users.setFullName(dto.getFullName());

        if (userId > 0) {
            users.setId(userId);
        }

        return users;
    }
}
