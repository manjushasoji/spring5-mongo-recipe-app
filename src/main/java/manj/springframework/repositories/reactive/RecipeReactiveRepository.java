package manj.springframework.repositories.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import manj.springframework.domain.reactive.ReactiveRecipe;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<ReactiveRecipe, String> {

}
