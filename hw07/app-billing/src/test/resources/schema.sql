create table account
(
    id        bigserial,
    login     varchar(255) NOT NULL UNIQUE,
    invoice   uuid DEFAULT random_uuid() UNIQUE,
    money     int,
    full_name varchar(255),
    primary key (id)
);