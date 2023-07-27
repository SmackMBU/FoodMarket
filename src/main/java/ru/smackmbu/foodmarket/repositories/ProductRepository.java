package ru.smackmbu.foodmarket.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.smackmbu.foodmarket.models.Product;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    @Override
    Optional<Product> findById(Long aLong);
}
