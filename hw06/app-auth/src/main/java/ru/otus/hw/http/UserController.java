package ru.otus.hw.http;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.converters.UsersConverter;
import ru.otus.hw.dto.LoginDto;
import ru.otus.hw.dto.ResponseDto;
import ru.otus.hw.dto.AuthUserDto;
import ru.otus.hw.exceptions.AuthenticationException;
import ru.otus.hw.exceptions.AuthUserNotFoundException;
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

    @GetMapping("/sessions")
    public Map<UUID, AuthUser> getSessions() {
        return sessions;
    }

    @PostMapping("/register")
    public AuthUser register(@RequestBody AuthUserDto authUserDto) {
        return usersService.create(usersConverter.mapDtoToModel(authUserDto));
    }

    @PostMapping("/login")
    public ResponseDto login(HttpServletResponse response, @RequestBody LoginDto loginDto) throws Exception {

        var authUser = usersService.findByLoginPassword(loginDto.getLogin(), loginDto.getPassword());

        if (authUser.isPresent()) {

            final UUID uuid = UUID.randomUUID();
            sessions.put(uuid, authUser.get());
            response.addCookie(new Cookie("session_id", uuid.toString()));
            return ResponseDto.builder().status("Пользователь залогинился").build();

        } else {
            throw new AuthUserNotFoundException("Not found AuthUser");
        }
    }

    @GetMapping("/signin")
    public ResponseDto signin() {
        return ResponseDto.builder().status("Please go to login and provide Login/Password").build();
    }

    @PostMapping("/logout")
    public ResponseDto logoutGet(HttpServletResponse response) {

        response.addCookie(new Cookie("session_id", null));
        return ResponseDto.builder().status("Пользователь разлогинился").build();
    }

    @PostMapping("/auth")
    public ResponseDto auth(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_id")) {

                    if (!cookie.getValue().isBlank() && sessions.containsKey(UUID.fromString(cookie.getValue()))) {

                        AuthUser authUser = sessions.get(UUID.fromString(cookie.getValue()));

                        response.setHeader("X-UserId", String.valueOf(authUser.getId()));
                        response.setHeader("X-User", authUser.getLogin());
                        response.setHeader("X-Email", authUser.getEmail());
                        response.setHeader("X-First-Name", authUser.getFirstName());
                        response.setHeader("X-Last-Name", authUser.getLastName());

                        return ResponseDto.builder().status("Пользователь авторизован").build();
                    }
                }
            }
        }

        throw new AuthenticationException("Error authentication");
    }
}
