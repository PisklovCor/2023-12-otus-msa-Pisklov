package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private long id;

    private String login;

    private UUID accountInvoice;

    private Integer sumOrder;

    private String message;
}
