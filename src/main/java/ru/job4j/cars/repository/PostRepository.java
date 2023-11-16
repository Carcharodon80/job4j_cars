package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class PostRepository {
    private final CrudRepository crudRepository;

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
        return crudRepository.optional("from Post p left join fetch p.priceHistories where p.id = :id",
                Post.class, Map.of("id", postId)).get();
    }

    public void changePrice(Post post, long newPrice) {
        post = findPostById(post.getId());
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setBefore(post.getPrice());
        priceHistory.setAfter(newPrice);
        priceHistory.setCreated(LocalDateTime.now());
        List<PriceHistory> priceHistories = post.getPriceHistories();
        priceHistories.add(priceHistory);
        post.setPriceHistories(priceHistories);
        post.setPrice(newPrice);
        update(post);
    }

    public void update(Post post) {
        crudRepository.run(session -> session.merge(post));
    }
}