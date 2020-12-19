package manj.springframework.converters.reactive;

import lombok.Synchronized;
import manj.springframework.commands.RecipeCommand;
import manj.springframework.converters.NotesToNotesCommand;
import manj.springframework.domain.reactive.ReactiveCategory;
import manj.springframework.domain.reactive.ReactiveRecipe;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
public class RetvRecipeToRecipeCommand implements Converter<ReactiveRecipe, RecipeCommand>{

    private final RetvCategoryToCategoryCommand categoryConveter;
    private final RetvIngredientToIngredientCommand ingredientConverter;
    private final NotesToNotesCommand notesConverter;

    public RetvRecipeToRecipeCommand(RetvCategoryToCategoryCommand categoryConveter, RetvIngredientToIngredientCommand ingredientConverter,
                                 NotesToNotesCommand notesConverter) {
        this.categoryConveter = categoryConveter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(ReactiveRecipe source) {
        if (source == null) {
            return null;
        }

        final RecipeCommand command = new RecipeCommand();
        command.setId(source.getId());
        command.setCookTime(source.getCookTime());
        command.setPrepTime(source.getPrepTime());
        command.setDescription(source.getDescription());
        command.setDifficulty(source.getDifficulty());
        command.setDirections(source.getDirections());
        command.setServings(source.getServings());
        command.setSource(source.getSource());
        command.setUrl(source.getUrl());
        command.setImage(source.getImage());
        command.setNotes(notesConverter.convert(source.getNotes()));

        if (source.getCategories() != null && source.getCategories().size() > 0){
            source.getCategories()
                    .forEach((ReactiveCategory category) -> command.getCategories().add(categoryConveter.convert(category)));
        }

        if (source.getIngredients() != null && source.getIngredients().size() > 0){
            source.getIngredients()
                    .forEach(ingredient -> command.getIngredients().add(ingredientConverter.convert(ingredient)));
        }

        return command;
    }
}
