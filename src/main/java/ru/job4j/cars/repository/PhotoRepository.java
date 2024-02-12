package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Photo;

@AllArgsConstructor
public class PhotoRepository {
    private final CrudRepository crudRepository;

    public Photo create(Photo photo) {
        crudRepository.run(session -> session.persist(photo));
        return photo;
    }
}
