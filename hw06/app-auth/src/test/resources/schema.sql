create table auth_user (
    id        bigserial,
    login varchar(255),
    password varchar(255),
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    primary key (id)
);