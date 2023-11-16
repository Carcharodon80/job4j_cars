package ru.job4j.cars.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "auto_post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private LocalDateTime created = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "auto_user_id")
    private User user;
    private long price;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "auto_post_id")
    @ToString.Exclude
    private List<PriceHistory> priceHistories = new ArrayList<>();
}
