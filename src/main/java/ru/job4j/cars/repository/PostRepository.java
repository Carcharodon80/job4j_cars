package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class PostRepository {
    private final CrudRepository crudRepository;
    //private final PriceHistoryRepository priceHistoryRepository;

    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    public void delete(Post post) {
        crudRepository.run("delete from Post where id = :id", Map.of("id", post.getId()));
    }

    public List<Post> findAllPosts() {
        return crudRepository.query("from Post order by id asc", Post.class);
    }

    public Post findPostById(int postId) {
        return crudRepository.optional("from Post where id = :id", Post.class, Map.of("id", postId)).get();
    }

    public void changePrice(Post post, long newPrice) {
        post = findPostById(post.getId());
        System.out.println(post);
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setBefore(post.getPrice());
        priceHistory.setAfter(newPrice);
        priceHistory.setCreated(LocalDateTime.now());
        post.getPriceHistories().add(priceHistory);
        post.setPrice(newPrice);
        update(post);
    }

    public void update(Post post) {
        crudRepository.run(session -> session.merge(post));
    }
}
