-- This migration adds user scoping to categories
-- Note: In a production scenario with existing data, you would need a data migration step
alter table categories add column user_id uuid references users (id) on delete cascade;
create index idx_categories_user_id on categories (user_id);
