--liquibase formatted sql

--changeset pisklov:2024-06-23-001-account
insert into notification(account_id, email, message, title, author)
values (1, 'admin@mail.ru', 'Сообщение инициализации', 'Сообщение инициализации', 'Сообщение инициализации');