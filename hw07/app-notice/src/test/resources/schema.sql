create table notifications
(
    id        bigserial,
    login     varchar(255) NOT NULL,
    account_invoice   uuid NOT NULL,
    sum_order int,
    message   varchar(255),
    primary key (id)
);