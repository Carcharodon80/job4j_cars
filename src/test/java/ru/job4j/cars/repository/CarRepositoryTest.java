package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;

import javax.persistence.PersistenceException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {
    private static CarRepository carRepository;

    @BeforeAll
    public static void wipeTable() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        carRepository = new CarRepository(new CrudRepository(sf));
        carRepository.findAllCars().forEach(carRepository::deleteCar);

    }

    @AfterEach
    public void wipeTableAfterEachTest() {
        carRepository.findAllCars().forEach(carRepository::deleteCar);
    }

    @Test
    public void whenAddCar() {
        Engine engine = new Engine();
        Car car = new Car();
        car.setEngine(engine);
        carRepository.create(car);
        assertEquals(carRepository.findCarById(car.getId()), car);
    }

    @Test
    public void whenAddCarWithoutEngine() {
        Car car = new Car();
        assertThrows(PersistenceException.class, () -> carRepository.create(car));
    }

    @Test
    public void whenFindCarById() {
        Car car = new Car();
        car.setEngine(new Engine());
        carRepository.create(car);
        assertEquals(carRepository.findCarById(car.getId()), car);
    }

    @Test
    public void whenNotFindCarById() {
        Car car = new Car();
        car.setEngine(new Engine());
        carRepository.create(car);
        assertThrows(NoSuchElementException.class, () -> carRepository.findCarById(car.getId() + 1));
    }

    @Test
    public void whenFindAllCars() {
        Car car1 = new Car();
        car1.setEngine(new Engine());
        Car car2 = new Car();
        car2.setEngine(new Engine());
        carRepository.create(car1);
        carRepository.create(car2);
        assertEquals(carRepository.findAllCars(), List.of(car1, car2));
    }

    @Test
    public void whenDeleteCar() {
        Car car = new Car();
        car.setEngine(new Engine());
        carRepository.create(car);
        carRepository.deleteCar(car);
        assertEquals(carRepository.findAllCars().size(), 0);
    }
}