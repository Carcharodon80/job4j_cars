package ru.job4j.cars.model.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    public User create(User user) {
        return user;
    }

    public void Update(User user) {

    }

    public void delete(int userId) {

    }

    public List<User> findAllOrderById() {
        return List.of();
    }

    public Optional<User> findById(int userId) {
        return Optional.empty();
    }

    public List<User> findByLikeLogin(String key) {
        return List.of();
    }

    public Optional<User> findByLogin(String login) {
        return Optional.empty();
    }
}
