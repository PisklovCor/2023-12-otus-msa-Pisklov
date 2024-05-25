--liquibase formatted sql

--changeset pisklov:2024-05-23-001-payment
insert into payment_table(order_id, login, account_invoice, description_order, sum_order, status)
values (1, 'admin', '32fff079-610b-466c-a094-241a98eca2f0', 'Заказ для инициализации', 1000, 'CONFIRMED');

--changeset pisklov:2024-05-23-002-account
insert into account_table(login, balance)
values ('admin', 10000);