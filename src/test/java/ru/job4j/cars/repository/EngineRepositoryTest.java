package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class EngineRepositoryTest {
    private static EngineRepository engineRepository;

    @BeforeAll
    public static void wipeTable() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        engineRepository = new EngineRepository(new CrudRepository(sf));
        engineRepository.findAllEngines().forEach(engineRepository::deleteEngine);
    }

    @AfterEach
    public void wipeTableAfterEachTest() {
        engineRepository.findAllEngines().forEach(engineRepository::deleteEngine);
    }

    @Test
    public void whenAddEngine() {
        Engine engine = new Engine();
        engineRepository.create(engine);
        assertEquals(engineRepository.findEngineById(engine.getId()), engine);
    }

    @Test
    public void whenFindAllEngines() {
        Engine engine = new Engine();
        Engine engine2 = new Engine();
        engineRepository.create(engine);
        engineRepository.create(engine2);
        assertEquals(engineRepository.findAllEngines(), List.of(engine, engine2));
    }

    @Test
    public void whenDeleteEngine() {
        Engine engine = new Engine();
        engineRepository.create(engine);
        engineRepository.deleteEngine(engine);
        assertEquals(engineRepository.findAllEngines().size(), 0);
        assertThrows(NoSuchElementException.class, () -> engineRepository.findEngineById(engine.getId()));
    }
}