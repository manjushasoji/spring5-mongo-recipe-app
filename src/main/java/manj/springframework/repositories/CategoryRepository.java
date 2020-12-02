package manj.springframework.repositories;

import org.springframework.data.repository.CrudRepository;

import manj.springframework.domain.Category;

import java.util.Optional;


public interface CategoryRepository extends CrudRepository<Category, String> {

    Optional<Category> findByDescription(String description);
}
