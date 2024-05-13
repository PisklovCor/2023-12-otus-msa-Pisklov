package ru.otus.hw.http;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.converters.UsersConverter;
import ru.otus.hw.dto.ResponseDto;
import ru.otus.hw.exceptions.AuthenticationException;
import ru.otus.hw.models.AuthUser;
import ru.otus.hw.services.UsersService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    private final UsersConverter usersConverter;

    private Map<UUID, AuthUser> sessions = new HashMap<>();

    @GetMapping("/health")
    public ResponseDto getHealth() {
        return ResponseDto.builder().status("OK").build();
    }

    @PostMapping("/users/me")
    public AuthUser getMe(HttpServletRequest request) {

        var userId = request.getHeader("X-UserId");

        if (userId == null) {
            throw new AuthenticationException("Error authentication");
        }

        var user = usersService.findById(userId);

        return user.get();
    }

    @PutMapping("/users/me")
    public AuthUser putMe(HttpServletRequest request) {

        var userId = request.getHeader("X-UserId");

        if (userId == null) {
            throw new AuthenticationException("Error authentication");
        }

        var login = request.getHeader("X-User");

        if (login == null) {
            throw new AuthenticationException("Error authentication");
        }

        var user = usersService.create(userId, login);

        return user;
    }
}
