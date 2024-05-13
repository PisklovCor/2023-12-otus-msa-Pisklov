package ru.otus.hw.http;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.AuthUserDto;
import ru.otus.hw.dto.ResponseDto;
import ru.otus.hw.exceptions.AuthenticationException;
import ru.otus.hw.models.AuthUser;
import ru.otus.hw.services.UsersService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    @GetMapping("/health")
    public ResponseDto getHealth() {
        return ResponseDto.builder().status("OK").build();
    }

    @PostMapping("/users/me")
    public AuthUserDto getMe(HttpServletRequest request) {

        var userId = request.getHeader("X-UserId");

        if (userId == null) {
            throw new AuthenticationException("Error authentication");
        }

        var user = usersService.findById(userId);

        AuthUser authUser = user.get();

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setId(authUser.getId());
        authUserDto.setLogin(authUser.getLogin());
        authUserDto.setEmail(request.getHeader("X-Email"));
        authUserDto.setFirstName(request.getHeader("X-First-Name"));
        authUserDto.setLastName(request.getHeader("X-Last-Name"));
        authUserDto.setAge(authUser.getAge());

        return authUserDto;
    }

    @PostMapping("/users/{userId}")
    public AuthUserDto getUsers(HttpServletRequest request, @PathVariable long userId) {

        var headerUserId = request.getHeader("X-UserId");

        if (headerUserId == null) {
            throw new AuthenticationException("Error authentication");
        }

        if (!(Integer.parseInt(headerUserId) == userId)) {
            throw new AuthenticationException("Error authentication");
        }

        var user = usersService.findById(headerUserId);

        AuthUser authUser = user.get();

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setId(authUser.getId());
        authUserDto.setLogin(authUser.getLogin());
        authUserDto.setEmail(request.getHeader("X-Email"));
        authUserDto.setFirstName(request.getHeader("X-First-Name"));
        authUserDto.setLastName(request.getHeader("X-Last-Name"));
        authUserDto.setAge(authUser.getAge());

        return authUserDto;
    }

    @PutMapping("/users/me")
    public AuthUserDto putMe(HttpServletRequest request) {

        var userId = request.getHeader("X-UserId");

        if (userId == null) {
            throw new AuthenticationException("Error authentication");
        }

        var login = request.getHeader("X-User");

        if (login == null) {
            throw new AuthenticationException("Error authentication");
        }

        var user = usersService.create(userId, login);

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setId(user.getId());
        authUserDto.setLogin(user.getLogin());
        authUserDto.setEmail(request.getHeader("X-Email"));
        authUserDto.setFirstName(request.getHeader("X-First-Name"));
        authUserDto.setLastName(request.getHeader("X-Last-Name"));
        authUserDto.setAge(user.getAge());

        return authUserDto;
    }
}
