package manj.springframework.services;

import java.util.Set;

import manj.springframework.commands.RecipeCommand;
import manj.springframework.domain.Recipe;


public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(String id);

    RecipeCommand findCommandById(String id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    void deleteById(String idToDelete);
}
