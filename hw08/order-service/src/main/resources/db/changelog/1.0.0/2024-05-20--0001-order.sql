--liquibase formatted sql

--changeset pisklov:2024-05-23-001-table-order
create table order_table
(
    id                bigserial,
    created_at        date NOT NULL DEFAULT CURRENT_DATE,
    login             varchar(255) NOT NULL,
    account_invoice   uuid NOT NULL,
    description_order varchar(255) NOT NULL,
    sum_order         int,
    status            varchar(255) NOT NULL,
    primary key (id)
);