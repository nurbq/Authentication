create table users
(
    id           bigint       not null
        constraint user_id_pk
            primary key,
    email        varchar(255) not null
        constraint user_email_unq
            unique,
    password     text         not null,
    created_time timestamp    not null
);

