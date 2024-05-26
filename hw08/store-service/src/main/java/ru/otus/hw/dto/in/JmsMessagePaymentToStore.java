package ru.otus.hw.dto.in;


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
public class JmsMessagePaymentToStore {

    @JsonProperty("paymentId")
    private long paymentId;

    @JsonProperty("orderId")
    private long orderId;

    @JsonProperty("createdAtOrder")
    private Date createdAtOrder;

    @JsonProperty("login")
    private String login;

    @JsonProperty("accountInvoice")
    private UUID accountInvoice;

    @JsonProperty("descriptionOrder")
    private String descriptionOrder;

    @JsonProperty("sumOrder")
    private Integer sumOrder;

}
