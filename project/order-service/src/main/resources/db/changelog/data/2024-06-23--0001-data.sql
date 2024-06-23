--liquibase formatted sql

--changeset pisklov:2024-06-23-001-account
insert into table_order(account_id, title, author, status)
values (1, 'Physics of the Impossible', 'Michio Kaku', 'wait');