create table transactions
(
    id          uuid primary key     not null default uuid_generate_v1(),
    amount      double precision     not null,
    description varchar(255),
    type        varchar(50)          not null,
    category_id uuid references categories (id),
    account_id  uuid                 not null references accounts (id),
    user_id     uuid                 not null references users (id),
    created_at  bigint               not null
);

create index idx_transactions_user_id on transactions (user_id);
create index idx_transactions_account_id on transactions (account_id);
