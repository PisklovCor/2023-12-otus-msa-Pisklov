package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    private long id;

    private Date createdAt;

    private long storeId;

    private long paymentId;

    private long orderId;

    private String login;

    private UUID accountInvoice;

    private String descriptionOrder;

    private Integer sumOrder;

    private String status;
}