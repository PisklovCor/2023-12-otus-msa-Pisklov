--liquibase formatted sql

--changeset pisklov:2024-05-20-001-table-account
create table account
(
id        bigserial,
login     varchar(255) NOT NULL UNIQUE,
invoice   uuid DEFAULT gen_random_uuid(),
money     int,
full_name varchar(255),
primary key (id)
);
