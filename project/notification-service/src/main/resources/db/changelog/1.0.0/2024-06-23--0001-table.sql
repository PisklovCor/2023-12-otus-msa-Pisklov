--liquibase formatted sql

--changeset pisklov:2024-06-23-001-table-notification
create table notification
(
id        bigserial,
account_id   int,
email       varchar(255) NOT NULL,
message       varchar(255) NOT NULL,
title       varchar(255) NOT NULL,
author      varchar(255) NOT NULL,
primary key (id)
);
