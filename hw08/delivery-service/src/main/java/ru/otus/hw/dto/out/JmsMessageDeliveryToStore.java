package ru.otus.hw.dto.out;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.dto.Status;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JmsMessageDeliveryToStore {

    @JsonProperty("storeId")
    private long storeId;

    @JsonProperty("paymentId")
    private long paymentId;

    @JsonProperty("orderId")
    private long orderId;

    @JsonProperty("status")
    private Status status;

}
