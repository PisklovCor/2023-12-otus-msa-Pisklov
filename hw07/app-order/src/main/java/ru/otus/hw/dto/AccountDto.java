package ru.otus.hw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    @JsonProperty("login")
    private String login;

    @JsonProperty("invoice")
    private UUID invoice;

    @JsonProperty("money")
    private Integer money;

    @JsonProperty("fullName")
    private String fullName;
}
