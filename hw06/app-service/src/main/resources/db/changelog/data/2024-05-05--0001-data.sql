--liquibase formatted sql

--changeset pisklov:2024-05-013-001-user_profile
insert into user_profile(id, login, age)
values (1, 'admin', 33);