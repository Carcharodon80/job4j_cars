alter table auto_post
    add car_id int references cars (id);