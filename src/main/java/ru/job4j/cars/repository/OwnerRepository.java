package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class OwnerRepository {
    private final CrudRepository crudRepository;

    public Owner create(Owner owner) {
        crudRepository.run(session -> session.persist(owner));
        return owner;
    }

    public List<Owner> findAllOwners() {
        return crudRepository.query("from Owner o left join fetch o.histories order by o.id asc", Owner.class);
    }

    public Owner findOwnerById(int id) {
        return crudRepository.optional("from Owner o left join fetch o.histories where o.id = :id",
                Owner.class, Map.of("id", id)).orElseThrow(NoSuchElementException::new);
    }

    public void deleteOwner(Owner owner) {
        crudRepository.run("delete from Owner where id = :id", Map.of("id", owner.getId()));
    }
}
