package ru.job4j.cars.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "engines")
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;
}
