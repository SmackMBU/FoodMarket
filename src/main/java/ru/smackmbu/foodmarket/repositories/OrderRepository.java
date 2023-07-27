package ru.smackmbu.foodmarket.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.smackmbu.foodmarket.models.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
