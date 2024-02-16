package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PostRepositoryTest {
    private static PostRepository postRepository;
    private static CrudRepository crudRepository;

    @BeforeAll
    public static void wipeTable() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        postRepository = new PostRepository(crudRepository);
        postRepository.findAllPosts().forEach(postRepository::delete);
    }

    @AfterEach
    public void wipeTableAfterEachTest() {
        postRepository.findAllPosts().forEach(postRepository::delete);
    }

    @Test
    public void whenAddPost() {
        Post post = new Post();
        postRepository.create(post);
        assertEquals(postRepository.findPostById(post.getId()), post);
    }

    @Test
    public void whenDeletePost() {
        Post post = new Post();
        postRepository.create(post);
        postRepository.delete(post);
        assertEquals(postRepository.findAllPosts().size(), 0);
    }

    @Test
    public void whenNotFoundPostById() {
        Post post = new Post();
        postRepository.create(post);
        assertThrows(NoSuchElementException.class, () -> postRepository.findPostById(post.getId() + 1));
    }

    @Test
    public void whenChangePrice() {
        Post post = new Post();
        postRepository.create(post);
        long newPrice = 10;
        postRepository.changePrice(post, newPrice);
        assertEquals(postRepository.findPostById(post.getId()).getPrice(), newPrice);
        assertEquals(postRepository.findPostById(post.getId()).getPriceHistories().size(), 1);
    }

    @Test
    public void whenUpdatePost() {
        Post post = new Post();
        postRepository.create(post);
        post.setPrice(10);
        postRepository.update(post);
        assertEquals(postRepository.findPostById(post.getId()), post);
    }

    @Test
    public void whenNotifyParticipates() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        UserRepository userRepository = new UserRepository(crudRepository);
        userRepository.findAllUsers().forEach(userRepository::delete);
        User user = new User();
        user.setLogin("login");
        Post post = new Post();
        post.setDescription("description");
        post.setUser(user);
        post.setParticipates(Set.of(user));
        postRepository.create(post);
        postRepository.notifyParticipates(post);
        assertEquals("login : description change price!\r\n", output.toString());
        System.setOut(null);
    }

    @Test
    public void whenPostsByCarName() {
        CarRepository carRepository = new CarRepository(crudRepository);
        carRepository.findAllCars().forEach(carRepository::deleteCar);
        Car car1 = new Car();
        car1.setName("Car1");
        car1.setEngine(new Engine());
        Car car2 = new Car();
        car2.setName("car2");
        car2.setEngine(new Engine());
        Post post1 = new Post();
        post1.setCar(car1);
        Post post2 = new Post();
        post2.setCar(car2);
        postRepository.create(post1);
        postRepository.create(post2);
        assertEquals(postRepository.findPostsByCarName("Car1").size(), 1);
        assertTrue(postRepository.findPostsByCarName("Car1").contains(post1));
    }

    @Test
    public void whenFindPostsForLastDay() {
        Post post1 = new Post();
        Post post2 = new Post();
        post2.setCreated(post2.getCreated().minusHours(25));
        postRepository.create(post1);
        postRepository.create(post2);
        assertEquals(postRepository.findPostsForLastDay().size(), 1);
        assertTrue(postRepository.findPostsForLastDay().contains(post1));
    }

    @Test
    public void whenFindPostsWithPhotos() {
        PhotoRepository photoRepository = new PhotoRepository(crudRepository);
        photoRepository.findAllPhotos().forEach(photoRepository::deletePhoto);
        Post post1 = new Post();
        Photo photo = new Photo();
        post1.setPhotos(Set.of(photo));
        Post post2 = new Post();
        postRepository.create(post1);
        postRepository.create(post2);
        assertEquals(postRepository.findPostsWithPhoto().size(), 1);
        assertTrue(postRepository.findPostsWithPhoto().contains(post1));
        photoRepository.findAllPhotos().forEach(photoRepository::deletePhoto);
    }
}