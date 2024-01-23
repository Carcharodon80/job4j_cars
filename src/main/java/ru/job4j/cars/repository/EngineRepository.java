package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class EngineRepository {
    private final CrudRepository crudRepository;

    public Engine create(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
        return engine;
    }

    public Engine findEngineById(int engineId) {
        return crudRepository.optional("from Engine where id = :id",
                Engine.class, Map.of("id", engineId)).get();
    }

    public List<Engine> findAllEngines() {
        return crudRepository.query("from Engine order by id asc", Engine.class);
    }

    public void deleteEngine(Engine engine) {
        crudRepository.run("delete from Engine where id = :id", Map.of("id", engine.getId()));
    }
}
