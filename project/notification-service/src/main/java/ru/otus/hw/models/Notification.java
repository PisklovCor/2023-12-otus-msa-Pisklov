package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private long id;

    private long accountId;

    private String email;

    private String message;

    private String title;

    private String author;
}
