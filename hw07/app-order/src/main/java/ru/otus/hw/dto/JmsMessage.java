package ru.otus.hw.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JmsMessage {


    @JsonProperty("login")
    private String login;

    @JsonProperty("sum")
    private Integer sum;

    @JsonProperty("status")
    private boolean status;

}
