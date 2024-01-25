alter table cars
    add column history_id int references history (id);