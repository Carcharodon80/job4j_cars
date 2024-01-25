create table if not exists history
(
    id       serial primary key,
    start_at timestamp without time zone,
    end_at   timestamp without time zone,
    owner_id int not null references owners(id)
)