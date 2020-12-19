package manj.springframework.services.reactive;

import manj.springframework.commands.IngredientCommand;
import reactor.core.publisher.Mono;


public interface IngredientReactiveService {

    Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

    Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command);

    Mono<Void> deleteById(String recipeId, String idToDelete);
}
