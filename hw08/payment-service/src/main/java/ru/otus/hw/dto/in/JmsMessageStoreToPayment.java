package ru.otus.hw.dto.in;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.dto.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JmsMessageStoreToPayment {

    @JsonProperty("paymentId")
    private long paymentId;

    @JsonProperty("orderId")
    private long orderId;

    @JsonProperty("login")
    private String login;

    @JsonProperty("status")
    private Status status;
}
