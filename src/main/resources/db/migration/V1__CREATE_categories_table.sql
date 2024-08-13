create table categories
(
    id          uuid primary key default gen_random_uuid(),
    title       varchar(80) not null,
    description varchar(255),
);