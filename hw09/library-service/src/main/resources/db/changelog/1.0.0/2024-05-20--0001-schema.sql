--liquibase formatted sql

--changeset pisklov:2024-06-08-001-book_table
create table book_table
(
    id          bigserial,
    title       varchar(255) NOT NULL,
    author      varchar(255) NOT NULL,
    rating      int,
    primary key (id)
);

--changeset pisklov:2024-06-08-002-account_book_table
create table account_book_table
(
    id          bigserial,
    account_id   int,
    book_id      int,
    primary key (id)
);