--liquibase formatted sql

--changeset pisklov:2024-05-23-001-table-payment
create table payment_table
(
    id                bigserial,
    created_at        date NOT NULL DEFAULT CURRENT_DATE,
    order_id          int NOT NULL UNIQUE,
    login             varchar(255) NOT NULL,
    account_invoice   uuid NOT NULL,
    description_order varchar(255) NOT NULL,
    sum_order         int,
    status            varchar(255) NOT NULL,
    primary key (id)
);

--changeset pisklov:2024-05-23-002-table-account
create table account_table
(
    id              bigserial,
    created_at      date NOT NULL DEFAULT CURRENT_DATE,
    login           varchar(255) NOT NULL UNIQUE,
    balance         int,
    primary key (id)
);