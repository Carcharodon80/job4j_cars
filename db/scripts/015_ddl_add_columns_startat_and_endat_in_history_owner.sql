alter table history_owner
    add column start_at timestamp without time zone,
    add column end_at   timestamp without time zone;