--liquibase formatted sql

--changeset pisklov:2024-06-08-001-book
insert into book_table(title, author, rating)
values ('Im Westen nichts Neues', 'Erich Maria Remarque', 9),
       ('The idiot', 'Fyodor Dostoyevsky', 7),
       ('For Whom the Bell Tolls', 'Ernest Miller Hemingway', 8)