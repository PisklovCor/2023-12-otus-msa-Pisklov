--liquibase formatted sql

--changeset pisklov:2024-05-20-001-order
insert into order_table(login, account_invoice, sum_order, status)
values ('admin', '32fff079-610b-466c-a094-241a98eca2f0', 1000, true);