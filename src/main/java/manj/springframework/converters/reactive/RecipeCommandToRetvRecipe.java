package manj.springframework.converters.reactive;

import lombok.Synchronized;
import manj.springframework.commands.RecipeCommand;
import manj.springframework.converters.NotesCommandToNotes;
import manj.springframework.domain.reactive.ReactiveRecipe;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
public class RecipeCommandToRetvRecipe implements Converter<RecipeCommand, ReactiveRecipe> {

    private final CategoryCommandToRetvCategory categoryConveter;
    private final IngredientCommandToRetvIngredient ingredientConverter;
    private final NotesCommandToNotes notesConverter;

    public RecipeCommandToRetvRecipe(CategoryCommandToRetvCategory categoryConveter, IngredientCommandToRetvIngredient ingredientConverter,
                                 NotesCommandToNotes notesConverter) {
        this.categoryConveter = categoryConveter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public ReactiveRecipe convert(RecipeCommand source) {
        if (source == null) {
            return null;
        }

        final ReactiveRecipe recipe = new ReactiveRecipe();
        recipe.setId(source.getId());
        recipe.setCookTime(source.getCookTime());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setDescription(source.getDescription());
        recipe.setDifficulty(source.getDifficulty());
        recipe.setDirections(source.getDirections());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setUrl(source.getUrl());
        recipe.setNotes(notesConverter.convert(source.getNotes()));

        if (source.getCategories() != null && source.getCategories().size() > 0){
            source.getCategories()
                    .forEach( category -> recipe.getCategories().add(categoryConveter.convert(category)));
        }

        if (source.getIngredients() != null && source.getIngredients().size() > 0){
            source.getIngredients()
                    .forEach(ingredient -> recipe.getIngredients().add(ingredientConverter.convert(ingredient)));
        }

        return recipe;
    }
}
