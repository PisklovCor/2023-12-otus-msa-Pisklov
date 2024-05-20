--liquibase formatted sql

--changeset pisklov:2024-05-20-001-account
insert into account(login, money, full_name)
values ('admin', 1000, 'admin_admin');