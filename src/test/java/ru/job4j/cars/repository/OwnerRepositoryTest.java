package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class OwnerRepositoryTest {
    private static OwnerRepository ownerRepository;

    @BeforeAll
    public static void wipeTable() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        ownerRepository = new OwnerRepository(new CrudRepository(sf));
        ownerRepository.findAllOwners().forEach(ownerRepository::deleteOwner);
    }

    @AfterEach
    public void wipeTableAfterEachTest() {
        ownerRepository.findAllOwners().forEach(ownerRepository::deleteOwner);
    }

    @Test
    public void whenAddOwner() {
        Owner owner = new Owner();
        owner.setName("owner");
        owner.setUser(new User());
        ownerRepository.create(owner);
        assertEquals(ownerRepository.findOwnerById(owner.getId()), owner);
    }

    @Test
    public void whenAddOwnerWithoutNameAndUser() {
        Owner owner = new Owner();
        assertThrows(PersistenceException.class, () -> ownerRepository.create(owner));
    }

    @Test
    public void whenNotFoundOwnerById() {
        assertThrows(NoSuchElementException.class, () -> ownerRepository.findOwnerById(1));
    }

    @Test
    public void whenFindAllOwners() {
        Owner owner = new Owner();
        owner.setName("owner");
        owner.setUser(new User());
        Owner owner2 = new Owner();
        owner2.setName("owner2");
        owner2.setUser(new User());
        ownerRepository.create(owner);
        ownerRepository.create(owner2);
        assertEquals(ownerRepository.findAllOwners(), List.of(owner, owner2));
    }

    @Test()
    public void whenVariousOwnersHadSameUser() {
        User user = new User();
        Owner owner = new Owner();
        owner.setName("owner");
        owner.setUser(user);
        Owner owner2 = new Owner();
        owner2.setName("owner2");
        owner2.setUser(user);
        ownerRepository.create(owner);
        assertThrows(PersistenceException.class, () -> ownerRepository.create(owner2));
    }

    @Test
    public void whenDeleteOwner() {
        Owner owner = new Owner();
        owner.setName("owner");
        owner.setUser(new User());
        ownerRepository.create(owner);
        ownerRepository.deleteOwner(owner);
        assertEquals(ownerRepository.findAllOwners().size(), 0);
    }
}