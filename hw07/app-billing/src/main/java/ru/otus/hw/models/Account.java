package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private long id;

    private String login;

    private UUID invoice;

    private Integer money;

    private String fullName;
}
