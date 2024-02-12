package ru.job4j.cars.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "price_history")
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "before")
    private long before;

    @Column(name = "after")
    private long after;

    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
}
