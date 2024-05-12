package ru.otus.hw.http;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.converters.UsersConverter;
import ru.otus.hw.dto.ResponseDto;
import ru.otus.hw.dto.UsersBaseDto;
import ru.otus.hw.dto.UsersDto;
import ru.otus.hw.models.Users;
import ru.otus.hw.repositories.UsersRepository;
import ru.otus.hw.services.UsersService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UsersService usersService;

    private final UsersConverter usersConverter;

    @GetMapping("/health")
    public ResponseDto getHealth() {
        return ResponseDto.builder().status("OK").build();
    }

    @Operation(summary = "Get all users")
    @GetMapping("/user")
    public List<Users> findAll() {
        return usersService.findAll();
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/user/{userId}")
    public Users findById(@PathVariable long userId) {
        return usersService.findById(userId).get();
    }

    @Operation(summary = "Create user")
    @PostMapping("/user")
    public Users create(@RequestBody UsersBaseDto usersDto) {
        return usersService.save(usersConverter.mapDtoToModel(usersDto, 0));
    }

    @Operation(summary = "Update user")
    @PutMapping("/user/{userId}")
    public Users update(@RequestBody UsersBaseDto usersDto, @PathVariable long userId) {
        return usersService.save(usersConverter.mapDtoToModel(usersDto, userId));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/user/{userId}")
    public void deleteById(@PathVariable long userId) {
        usersService.deleteById(userId);
    }
}
