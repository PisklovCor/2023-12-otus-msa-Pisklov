--liquibase formatted sql

--changeset pisklov:2024-05-06-001-user
insert into users(login, full_name)
values ('admin', 'administrator'),
       ('authors', 'Fyodor Dostoyevsky');