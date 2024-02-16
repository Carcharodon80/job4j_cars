package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Photo;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class PhotoRepository {
    private final CrudRepository crudRepository;

    public Photo create(Photo photo) {
        crudRepository.run(session -> session.persist(photo));
        return photo;
    }

    public List<Photo> findAllPhotos() {
        return crudRepository.query("from Photo order by id asc", Photo.class);
    }

    public void deletePhoto(Photo photo) {
        crudRepository.run("delete from Photo where id = :id",
                Map.of("id", photo.getId()));
    }
}
