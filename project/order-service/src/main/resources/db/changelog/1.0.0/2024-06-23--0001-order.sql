--liquibase formatted sql

--changeset pisklov:2024-06-23-001-table-order
create table table_order
(
id        bigserial,
account_id   int,
title       varchar(255) NOT NULL,
author      varchar(255) NOT NULL,
status      varchar(255) NOT NULL,
primary key (id)
);
