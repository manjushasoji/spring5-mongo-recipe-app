package manj.springframework.services.reactive;

import lombok.extern.slf4j.Slf4j;
import manj.springframework.commands.IngredientCommand;
import manj.springframework.converters.reactive.IngredientCommandToRetvIngredient;
import manj.springframework.converters.reactive.RetvIngredientToIngredientCommand;
import manj.springframework.domain.reactive.ReactiveIngredient;
import manj.springframework.domain.reactive.ReactiveRecipe;
import manj.springframework.repositories.reactive.RecipeReactiveRepository;
import manj.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientReactiveServiceImpl implements IngredientReactiveService {

	private final RetvIngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToRetvIngredient ingredientCommandToIngredient;
	private final RecipeReactiveRepository recipeReactiveRepository;
	private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

	public IngredientReactiveServiceImpl(RetvIngredientToIngredientCommand ingredientToIngredientCommand,
			IngredientCommandToRetvIngredient ingredientCommandToIngredient,
			RecipeReactiveRepository recipeReactiveRepository,
			UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.recipeReactiveRepository = recipeReactiveRepository;
		this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
	}

	@Override
	public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

		return recipeReactiveRepository.findById(recipeId).flatMapIterable(ReactiveRecipe::getIngredients)
				.filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId)).single().map(ingredient -> {
					IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
					command.setRecipeId(recipeId);
					return command;
				});
	}

	@Override
	@Transactional
	public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
		
		ReactiveRecipe reactiveRecipe = recipeReactiveRepository.findById(command.getRecipeId()).block();
		if (reactiveRecipe == null) {

			// todo toss error if not found!
			log.error("ReactiveRecipe not found for id: " + command.getRecipeId());
			return Mono.just(new IngredientCommand());
			
		} else {
			

			Optional<ReactiveIngredient> ingredientOptional = reactiveRecipe.getIngredients().stream()
					.filter(ingredient -> ingredient.getId().equals(command.getId())).findFirst();

			if (ingredientOptional.isPresent()) {
				ReactiveIngredient ingredientFound = ingredientOptional.get();
				ingredientFound.setDescription(command.getDescription());
				ingredientFound.setAmount(command.getAmount());
				ingredientFound.setUom(unitOfMeasureReactiveRepository.findById(command.getUom().getId()).block() );
				if (ingredientFound.getUom() == null) {
                    new RuntimeException("UOM NOT FOUND");
                }
						
						
			} else {
				// add new ReactiveIngredient
				ReactiveIngredient ingredient = ingredientCommandToIngredient.convert(command);
				// ingredient.setRecipe(reactiveRecipe);
				reactiveRecipe.addIngredient(ingredient);
			}

			ReactiveRecipe savedRecipe = recipeReactiveRepository.save(reactiveRecipe).block();

			Optional<ReactiveIngredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
					.filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId())).findFirst();

			// check by description
			if (!savedIngredientOptional.isPresent()) {
				// not totally safe... But best guess
				savedIngredientOptional = savedRecipe.getIngredients().stream()
						.filter(recipeIngredients -> recipeIngredients.getDescription()
								.equals(command.getDescription()))
						.filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
						.filter(recipeIngredients -> recipeIngredients.getUom().getId()
								.equals(command.getUom().getId()))
						.findFirst();
			}

			// to do check for fail
			
			//enhance with id value
            IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            ingredientCommandSaved.setRecipeId(reactiveRecipe.getId());

            return Mono.just(ingredientCommandSaved);
		}

	}

	@Override
	public Mono<Void> deleteById(String recipeId, String idToDelete) {

		log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

		ReactiveRecipe reactiveRecipe= recipeReactiveRepository.findById(recipeId).block();
				

		if (reactiveRecipe != null) {
			
			log.debug("found reactiveRecipe");

			Optional<ReactiveIngredient> ingredientOptional = reactiveRecipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(idToDelete))
                    .findFirst();

			if (ingredientOptional.isPresent()) {
				log.debug("found ReactiveIngredient");
				
				reactiveRecipe.getIngredients().remove(ingredientOptional.get());
				recipeReactiveRepository.save(reactiveRecipe).block();
				
			}
		} else {
			log.debug("ReactiveRecipe Id Not found. Id:" + recipeId);
		}
		return Mono.empty();
	}
}
