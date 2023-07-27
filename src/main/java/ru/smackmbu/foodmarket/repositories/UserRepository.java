package ru.smackmbu.foodmarket.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.smackmbu.foodmarket.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
