package ru.otus.hw.dto.out;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.dto.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JmsMessagePaymentOrder {

    @JsonProperty("orderId")
    private long orderId;

    @JsonProperty("status")
    private Status status;
}
