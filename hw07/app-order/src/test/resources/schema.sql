create table order_table
(
    id        bigserial,
    login     varchar(255) NOT NULL,
    account_invoice   uuid NOT NULL,
    sum_order int,
    status    boolean,
    primary key (id)
);