package manj.springframework.repositories;

import org.springframework.data.repository.CrudRepository;

import manj.springframework.domain.Recipe;


public interface RecipeRepository extends CrudRepository<Recipe, String> {
}
