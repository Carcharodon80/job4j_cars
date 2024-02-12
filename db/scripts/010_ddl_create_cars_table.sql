create table cars(
    id serial primary key,
    name text,
    engine_id int not null unique references engines(id)
)