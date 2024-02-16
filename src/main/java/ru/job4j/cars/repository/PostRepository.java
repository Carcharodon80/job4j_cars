package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

@AllArgsConstructor
public class PostRepository {
    private final CrudRepository crudRepository;

    public Post create(Post post) {
        crudRepository.run(session -> session.persist(post));
        return post;
    }

    public void delete(Post post) {
        crudRepository.run("delete from Post where id = :id",
                Map.of("id", post.getId()));
    }

    public List<Post> findAllPosts() {
        return crudRepository.query("from Post order by id asc", Post.class);
    }

    public Post findPostById(int postId) {
        return crudRepository.optional("select distinct p from Post p "
                        + "left join fetch p.priceHistories "
                        + "left join fetch p.participates "
                        + "left join fetch p.photos "
                        + "where p.id = :id",
                Post.class, Map.of("id", postId)).orElseThrow(NoSuchElementException::new);
    }

    public void changePrice(Post post, long newPrice) {
        post = findPostById(post.getId());
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setBefore(post.getPrice());
        priceHistory.setAfter(newPrice);
        Set<PriceHistory> priceHistories = post.getPriceHistories();
        priceHistories.add(priceHistory);
        post.setPriceHistories(priceHistories);
        post.setPrice(newPrice);
        update(post);
        notifyParticipates(post);
    }

    public void update(Post post) {
        crudRepository.run(session -> session.merge(post));
    }

    public void notifyParticipates(Post post) {
        post = findPostById(post.getId());
        for (User user : post.getParticipates()) {
            System.out.println(user.getLogin() + " : " + post.getDescription() + " change price!");
        }
    }

    public List<Post> findPostsByCarName(String carName) {
        return crudRepository.query("select distinct p from Post p "
                + "left join fetch p.priceHistories "
                + "left join fetch p.participates "
                + "left join fetch p.car c "
                + "left join fetch c.histories "
                + "left join fetch p.photos "
                + "where c.name = :carName", Post.class, Map.of("carName", carName));
    }

    public List<Post> findPostsForLastDay() {
        LocalDateTime lastDay = LocalDateTime.now().minusHours(24);
        return crudRepository.query("select distinct p from Post p "
                + "left join fetch p.priceHistories "
                + "left join fetch p.participates "
                + "left join fetch p.car c "
                + "left join fetch c.histories "
                + "left join fetch p.photos "
                + "where p.created >= :lastDay", Post.class, Map.of("lastDay", lastDay));
    }

    public List<Post> findPostsWithPhoto() {
        return crudRepository.query("select distinct p from Post p "
                + "left join fetch p.priceHistories "
                + "left join fetch p.participates "
                + "left join fetch p.car c "
                + "left join fetch c.histories "
                + "left join fetch p.photos ph  "
                + "where size (ph) > 0", Post.class);
    }
}
