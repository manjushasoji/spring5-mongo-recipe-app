package manj.springframework.services.reactive;

import manj.springframework.commands.RecipeCommand;
import manj.springframework.domain.reactive.ReactiveRecipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeReactiveService {

	Flux<ReactiveRecipe> getRecipes();

	Mono<ReactiveRecipe> findById(String id);

	Mono<RecipeCommand> findCommandById(String id);

	Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);

	Mono<Void> deleteById(String idToDelete);
}
