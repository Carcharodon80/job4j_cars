package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final CrudRepository crudRepository;

    public User create(User user) {
        crudRepository.run(session -> session.persist(user));
        return user;
    }

    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    public void delete(User user) {
        crudRepository.run(
                "delete from User where id = :fId", Map.of("fId", user.getId())
        );
    }

    public List<User> findAllOrderById() {
        return crudRepository.query("from User order by id asc", User.class);
    }

    public Optional<User> findById(int userId) {
        return crudRepository.optional(
                "from User where id = :fId", User.class, Map.of("fId", userId)
        );
    }

    /**
     * Найти пользователей с указанным ключом в логине (в любой позиции)
     * @param key - ключ
     * @return список пользователей
     */
    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "from User where login like :fKey", User.class, Map.of("fKey", "%" + key + "%")
        );
    }

    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "from User where login = :fLogin", User.class, Map.of("fLogin", login)
        );
    }
}
