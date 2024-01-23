package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CarRepository {
    private final CrudRepository crudRepository;

    public Car create(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    public Car findCarById(int carId) {
        return crudRepository.optional("from Car c where c.id = :id", Car.class, Map.of("id", carId)).get();
    }

    public List<Car> findAllCars() {
        return crudRepository.query("from Car c left join fetch c.owners order by c.id asc", Car.class);
    }

    public void deleteCar(Car car) {
        crudRepository.run("delete from Car where id = :id", Map.of("id", car.getId()));
    }
}
