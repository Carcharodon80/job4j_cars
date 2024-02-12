create table participates (
    id serial primary key,
    post_id int not null references auto_post(id) on delete cascade,
    user_id int not null references auto_user(id) on delete cascade,
    unique (post_id, user_id)
)