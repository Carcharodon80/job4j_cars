package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import ru.job4j.cars.model.PriceHistory;

@AllArgsConstructor
public class PriceHistoryRepository {
    private final CrudRepository crudRepository;

    public PriceHistory create(PriceHistory priceHistory) {
        crudRepository.run(session -> session.persist(priceHistory));
        return priceHistory;
    }
}
