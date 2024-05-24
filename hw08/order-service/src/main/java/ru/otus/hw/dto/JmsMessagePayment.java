package ru.otus.hw.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JmsMessagePayment {

    @JsonProperty("orderId")
    private long orderId;

    @JsonProperty("status")
    private OrderStatus status;
}
