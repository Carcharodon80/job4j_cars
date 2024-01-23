package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.*;

import java.util.Set;

public class UserUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try (SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory()) {
            CrudRepository crudRepository = new CrudRepository(sf);
            UserRepository userRepository = new UserRepository(crudRepository);
            EngineRepository engineRepository = new EngineRepository(crudRepository);
            CarRepository carRepository = new CarRepository(crudRepository);
            OwnerRepository ownerRepository = new OwnerRepository(crudRepository);

            User user1 = new User();
            Engine engine1 = new Engine();
            engine1.setName("Engine1");
            Owner owner1 = new Owner();
            owner1.setName("owner1");
            owner1.setUser(user1);
            Car car1 = new Car();
            car1.setName("Car1");
            car1.setEngine(engine1);
            car1.setOwners(Set.of(owner1));
            car1.setOwner(owner1);

            userRepository.create(user1);
            engineRepository.create(engine1);
            carRepository.create(car1);

            System.out.println(engineRepository.findAllEngines());
            System.out.println(carRepository.findAllCars());
            System.out.println(ownerRepository.findAllOwners());

            carRepository.deleteCar(car1);
            engineRepository.deleteEngine(engine1);
            ownerRepository.deleteOwner(owner1);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}

