package ru.otus.hw.http;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private static final String HEADER_X_ACCOUNT_ID = "X-Account-Id";

    private final UsersService usersService;

    private final UsersConverter usersConverter;

    private Map<UUID, AuthUser> sessions = new HashMap<>();

    @Operation(summary = "Получить все активные сессии пользователей")
    @GetMapping("api/account/sessions")
    public Map<UUID, AuthUser> getSessions() {
        return sessions;
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("api/account/register")
    public AuthUser register(@RequestBody AuthUserDto authUserDto) {
        return usersService.create(usersConverter.mapDtoToModel(authUserDto));
    }

    @Operation(summary = "Вход пользователя (создание сессиии)")
    @PostMapping("api/account/login")
    public ResponseDto login(HttpServletResponse response, @RequestBody LoginDto loginDto) throws Exception {

        var authUser = usersService.findByLoginPassword(loginDto.getLogin(), loginDto.getPassword());

        if (authUser.isPresent()) {

            if (sessions.containsValue(authUser.get())) {
                return ResponseDto.builder().status("Пользователь уже залогинился").build();
            }

            final UUID uuid = UUID.randomUUID();
            sessions.put(uuid, authUser.get());
            response.addCookie(new Cookie("session_id", uuid.toString()));
            return ResponseDto.builder().status("Пользователь залогинился").build();

        } else {
            throw new AuthUserNotFoundException("Not found AuthUser");
        }
    }

    @Operation(summary = "Авторизация пользоваетля (запись хедеров)")
    @PostMapping("api/account/auth")
    public ResponseDto auth(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_id") && !cookie.getValue().isBlank()
                        && sessions.containsKey(UUID.fromString(cookie.getValue()))) {

                    AuthUser authUser = sessions.get(UUID.fromString(cookie.getValue()));

                    response.setHeader(HEADER_X_ACCOUNT_ID, String.valueOf(authUser.getId()));
                    response.setHeader("X-User", authUser.getLogin());
                    response.setHeader("X-Email", authUser.getEmail());
                    response.setHeader("X-First-Name", authUser.getFirstName());
                    response.setHeader("X-Last-Name", authUser.getLastName());

                    return ResponseDto.builder().status("Пользователь авторизован").build();

                }
            }
        }

        throw new AuthenticationException("Error authentication");
    }

    @Operation(summary = "Выход пользователя (удаление сессии)")
    @PostMapping("api/account/logout")
    public ResponseDto logoutGet(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_id") && !cookie.getValue().isBlank()) {
                    sessions.remove(UUID.fromString(cookie.getValue()));
                }
            }
        }

        response.addCookie(new Cookie("session_id", null));
        return ResponseDto.builder().status("Пользователь разлогинился").build();
    }

    @Operation(summary = "Редирект для логина")
    @GetMapping("api/account/signin")
    public ResponseDto signinGet() {
        return ResponseDto.builder().status("Please go to login and provide Login/Password").build();
    }

    @Operation(summary = "Редирект для логина")
    @PostMapping("api/account/signin")
    public ResponseDto signinPost() {
        return ResponseDto.builder().status("Please go to login and provide Login/Password").build();
    }

    @Operation(summary = "Удалить аккаунт по ID")
    @DeleteMapping("/api/account/admin/{accountId}")
    public ResponseEntity<Void> deleteAccount(HttpServletRequest request, @PathVariable long accountId) {

        var userLogin = request.getHeader("X-User");

        if (userLogin == null) {
            throw new AuthUserNotFoundException("Empty header");
        }

        if (!userLogin.equals("admin")) {
            throw new AuthUserNotFoundException("Error user not ADMIN");
        }

        usersService.deleteAccount(accountId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
