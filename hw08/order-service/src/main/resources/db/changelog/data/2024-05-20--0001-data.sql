--liquibase formatted sql

--changeset pisklov:2024-05-23-001-order
insert into order_table(login, account_invoice, description_order, sum_order, status)
values ('admin', '32fff079-610b-466c-a094-241a98eca2f0', 'Заказ для инициализации', 1000, 'CONFIRMED');