create table users
(
    id         uuid primary key not null default uuid_generate_v1(),
    name       varchar(255) not null,
    email      text not null,
    password   text not null
);

create unique index idx_users_email on users(email);