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

    /**
     * Сохранить в базе, если нет с таким логином (поле login в базе - помечено как уникальное)
     * @param user - пользователь
     * @return - пользователь с id
     */
    public User create(User user) {
        crudRepository.run(session -> session.persist(user));
        return user;
    }

    /**
     * Обновить пользователя
     * @param user - пользователь
     */
    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    /**
     * Удалить пользователя с указанным id
     * @param userId - id пользователя
     */
    public void delete(int userId) {
        crudRepository.run(
                "delete from User where id = :fId", Map.of("fId", userId)
        );
    }

    /**
     * Список пользователей, отсортированных по id
     * @return список пользователей
     */
    public List<User> findAllOrderById() {
        return crudRepository.query("from User order by id asc", User.class);
    }

    /**
     * Найти пользователя по id
     * @param userId id пользователя
     * @return пользователь
     */
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

    /**
     * Найти пользователя по логину
     * @param login логин
     * @return пользователь
     */
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "from User where login = :fLogin", User.class, Map.of("fLogin", login)
        );
    }
}
