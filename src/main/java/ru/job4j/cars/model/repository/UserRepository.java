package ru.job4j.cars.model.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    /**
     * Сохранить в базе, если нет с таким логином (поле login в базе - помечено как уникальное)
     *
     * @param user - пользователь
     * @return - пользователь с id
     */
    public User create(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return user;
    }

    /**
     * Обновить пользователя
     *
     * @param user - пользователь
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("UPDATE User SET password = :uPassword WHERE id = :uId")
                    .setParameter("uPassword", user.getPassword())
                    .setParameter("uId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Удалить пользователя с указанным id
     *
     * @param userId - id пользователя
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE User WHERE id = :uId")
                    .setParameter("uId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Список пользователей, отсортированных по id
     *
     * @return список пользователей
     */
    public List<User> findAllOrderById() {
        List<User> users = List.of();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            users = session.createQuery("FROM User ORDER BY id", User.class).list();
            session.beginTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return users;
    }

    /**
     * Найти пользователя по id
     *
     * @param userId id пользователя
     * @return пользователь
     */
    public Optional<User> findById(int userId) {
        Optional<User> optionalUser = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("FROM User AS u WHERE u.id = :uId", User.class);
            query.setParameter("uId", userId);
            optionalUser = query.uniqueResultOptional();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return optionalUser;
    }

    /**
     * Найти пользователей с указанным ключом в логине (в любой позиции)
     *
     * @param key - ключ
     * @return список пользователей
     */
    public List<User> findByLikeLogin(String key) {
        List<User> users = List.of();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User as u where u.login like :uLogin", User.class);
            query.setParameter("uLogin", "%" + key + "%");
            users = query.list();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        return users;
    }

    /**
     * Найти пользователя по логину
     *
     * @param login логин
     * @return пользователь
     */
    public Optional<User> findByLogin(String login) {
        Optional<User> optionalUser = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User as u where u.login = :uLogin", User.class);
            query.setParameter("uLogin", login);
            optionalUser = query.uniqueResultOptional();
        } catch (Exception e) {
            session.beginTransaction().rollback();
        } finally {
            session.close();
        }
        return optionalUser;
    }
}
