package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.User;

public class UserUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try (SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory()) {
            var userRepository = new UserRepository(new CrudRepository(sf));
            var user = new User();
            user.setLogin("admin");
            user.setPassword("admin");
            userRepository.create(user);
            System.out.println("All users:");
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
            userRepository.findById(user.getId()).ifPresent(System.out::println);
            userRepository.delete(user.getId());
            System.out.println("All users after deleting:");
            userRepository.findAllOrderById().forEach(System.out::println);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
