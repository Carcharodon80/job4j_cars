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
            PostRepository postRepository = new PostRepository(crudRepository);
            PhotoRepository photoRepository = new PhotoRepository(crudRepository);

            Engine engine1 = new Engine();
            engine1.setName("Engine1");
            Car car1 = new Car();
            car1.setName("Car1");
            car1.setEngine(engine1);
            Photo photo1 = new Photo();
            photo1.setName("photo1");
            Photo photo2 = new Photo();
            photo2.setName("photo2");
            Post post1 = new Post();
            post1.setCar(car1);
            post1.setPhotos(Set.of(photo1, photo2));

            postRepository.create(post1);

            System.out.println(postRepository.findPostsForLastDay());
            System.out.println(postRepository.findPostsByCarName("Car1"));
            System.out.println(postRepository.findPostsWithPhoto());

        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}

