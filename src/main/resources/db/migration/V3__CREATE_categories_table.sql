create table categories
(
    id          uuid primary key not null default uuid_generate_v1(),
    title       varchar(80) not null,
    description varchar(255),
    user_id     uuid not null references users (id) on delete cascade
);

create index idx_categories_user_id on categories (user_id);