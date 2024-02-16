package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
    private static UserRepository userRepository;

    @BeforeAll
    public static void wipeTable() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        userRepository = new UserRepository(new CrudRepository(sf));
        userRepository.findAllUsers().forEach(userRepository::delete);
    }

    @AfterEach
    public void wipeTableAfterEachTest() {
        userRepository.findAllUsers().forEach(userRepository::delete);
    }

    @Test
    public void whenAddUser() {
        User user = new User();
        userRepository.create(user);
        assertEquals(userRepository.findById(user.getId()), user);
    }

    @Test
    public void whenUpdateUser() {
        User user = new User();
        userRepository.create(user);
        user.setLogin("login");
        userRepository.update(user);
        assertEquals(userRepository.findById(user.getId()), user);
    }

    @Test
    public void whenDeleteUser() {
        User user = new User();
        userRepository.create(user);
        userRepository.delete(user);
        assertEquals(userRepository.findAllUsers().size(), 0);
    }

    @Test
    public void whenFindByLikeLogin() {
        User user = new User();
        user.setLogin("login");
        userRepository.create(user);
        User user2 = new User();
        user2.setLogin("login1");
        userRepository.create(user2);
        User user3 = new User();
        user3.setLogin("log");
        userRepository.create(user3);
        assertEquals(userRepository.findByLikeLogin("in"), List.of(user, user2));
    }

    @Test
    public void whenFindByLogin() {
        User user = new User();
        user.setLogin("login");
        userRepository.create(user);
        User user2 = new User();
        user2.setLogin("log");
        userRepository.create(user2);
        assertEquals(userRepository.findByLogin("log"), user2);
    }

}