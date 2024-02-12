create table photos
(
    id          serial primary key,
    name        text,
    path        text,
    description text,
    post_id     int references auto_post (id)
)