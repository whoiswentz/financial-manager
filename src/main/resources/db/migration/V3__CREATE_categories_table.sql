create table categories
(
    id          uuid primary key not null default uuid_generate_v1(),
    title       varchar(80) not null,
    description varchar(255)
);