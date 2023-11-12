package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "auto_post")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private LocalDateTime created = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "auto_user_id")
    private User user;
    private long price;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "auto_post_id")
    private List<PriceHistory> priceHistories = new ArrayList<>();
}
