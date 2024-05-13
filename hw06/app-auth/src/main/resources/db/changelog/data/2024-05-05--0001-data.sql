--liquibase formatted sql

--changeset pisklov:2024-05-013-001-auth_user
insert into auth_user(login, password, email, first_name, last_name)
values ('admin', 'admin', 'admin', 'admin', 'admin');