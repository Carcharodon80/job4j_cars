package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;


public class UserUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try (SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory()) {
            var crudRepository = new CrudRepository(sf);
            var userRepository = new UserRepository(crudRepository);

            var user = new User();
            user.setLogin("admin");
            user.setPassword("admin");
            var postRepository = new PostRepository(crudRepository);
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

            /**System.out.println("All users:");
            userRepository.findAllOrderById().forEach(System.out::println);
            System.out.println("Users with 'e':");
            userRepository.findByLikeLogin("e").forEach(System.out::println);
            System.out.println("User with id:");
            userRepository.findById(user.getId()).ifPresent(System.out::println);
            System.out.println("User with login 'admin':");
            userRepository.findByLogin("admin").ifPresent(System.out::println);
            user.setPassword("password");
            userRepository.update(user);
            System.out.println("Updated user:");
            userRepository.findById(user.getId()).ifPresent(System.out::println);*/
            postRepository.delete(post1);
            postRepository.delete(post2);
            userRepository.delete(user);
            System.out.println("All users after deleting:");
            userRepository.findAllOrderById().forEach(System.out::println);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}

//todo доделать changePrice!!!!!!
//todo при создании и обновлении объявления добавить priceHistory, привязав эту priceHistory к post
//todo проверить связь post -> history

