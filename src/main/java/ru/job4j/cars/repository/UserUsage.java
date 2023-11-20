package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;

import java.util.List;

public class UserUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try (SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory()) {
            var crudRepository = new CrudRepository(sf);
            var userRepository = new UserRepository(crudRepository);
            var postRepository = new PostRepository(crudRepository);

            var user = new User();
            user.setLogin("admin");
            user.setPassword("admin");

            var post1 = new Post();
            post1.setUser(user);
            post1.setDescription("New post 1");
            post1.setPrice(10000);
            var post2 = new Post();
            post2.setUser(user);
            post2.setDescription("New post 2");
            post2.setPrice(20000);
            userRepository.create(user);
            postRepository.create(post1);
            postRepository.create(post2);

            post1.setParticipates(List.of(user));
            postRepository.update(post1);

            postRepository.changePrice(post1, 50000);
            postRepository.changePrice(post1, 10000000);
            postRepository.changePrice(post2, 1000);

            System.out.println("All users:");
            userRepository.findAllOrderById().forEach(System.out::println);
            System.out.println("All posts:");
            postRepository.findAllPosts().forEach(System.out::println);

            userRepository.delete(user);
            System.out.println("All users after deleting:");
            userRepository.findAllOrderById().forEach(System.out::println);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
