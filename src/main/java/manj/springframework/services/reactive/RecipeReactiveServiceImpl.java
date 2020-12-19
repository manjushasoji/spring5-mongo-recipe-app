package manj.springframework.services.reactive;

import lombok.extern.slf4j.Slf4j;
import manj.springframework.commands.RecipeCommand;
import manj.springframework.converters.reactive.RecipeCommandToRetvRecipe;
import manj.springframework.converters.reactive.RetvRecipeToRecipeCommand;
import manj.springframework.domain.reactive.ReactiveRecipe;
import manj.springframework.repositories.reactive.RecipeReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RecipeReactiveServiceImpl implements RecipeReactiveService {

	private final RecipeReactiveRepository recipeReactiveRepository;
	private final RecipeCommandToRetvRecipe recipeCommandToRecipe;
	private final RetvRecipeToRecipeCommand recipeToRecipeCommand;

	public RecipeReactiveServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
			RecipeCommandToRetvRecipe recipeCommandToRecipe, RetvRecipeToRecipeCommand recipeToRecipeCommand) {
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.recipeCommandToRecipe = recipeCommandToRecipe;
		this.recipeToRecipeCommand = recipeToRecipeCommand;
	}

	@Override
	public Flux<ReactiveRecipe> getRecipes() {
		log.debug("I'm in the service");

		return recipeReactiveRepository.findAll();
	}

	@Override
	public Mono<ReactiveRecipe> findById(String id) {

		return recipeReactiveRepository.findById(id);
	}

	@Override
	@Transactional
	public Mono<RecipeCommand> findCommandById(String id) {
		return recipeReactiveRepository.findById(id).map(recipe -> {
			RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);

			recipeCommand.getIngredients().forEach(rc -> {
				rc.setRecipeId(recipeCommand.getId());
			});

			return recipeCommand;
		});
	}

	@Override
	@Transactional
	public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {
		return recipeReactiveRepository.save(recipeCommandToRecipe.convert(command))
				.map(recipeToRecipeCommand::convert);
	}

	@Override
	public Mono<Void> deleteById(String idToDelete) {
		recipeReactiveRepository.deleteById(idToDelete).block();
		return Mono.empty();
	}
}
