create table accounts
(
    id       uuid primary key not null default uuid_generate_v1(),
    name     varchar(255)     not null,
    type     varchar(50)      not null,
    balance  double precision not null default 0.0,
    user_id  uuid             not null references users (id)
);

create index idx_accounts_user_id on accounts (user_id);
