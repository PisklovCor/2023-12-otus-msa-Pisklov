package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDto {

    private long id;

    private String login;

    private String password;

    private String email;

    private String firstName;

    private String lastName;
}
