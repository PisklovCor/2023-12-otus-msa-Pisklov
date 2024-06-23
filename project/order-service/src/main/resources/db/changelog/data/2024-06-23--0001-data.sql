--liquibase formatted sql

--changeset pisklov:2024-06-23-001-account
insert into table_order(account_id, email, title, author, status)
values (1, 'admin@mail.ru', 'Physics of the Impossible', 'Michio Kaku', 'wait');