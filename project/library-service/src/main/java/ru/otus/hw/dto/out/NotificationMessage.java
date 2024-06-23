package ru.otus.hw.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {

    private long accountId;

    private String email;

    private String message;

    private String title;

    private String author;

}
