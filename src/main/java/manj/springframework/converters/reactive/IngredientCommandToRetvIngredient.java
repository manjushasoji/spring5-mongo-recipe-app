package manj.springframework.converters.reactive;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import manj.springframework.commands.IngredientCommand;
import manj.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import manj.springframework.domain.reactive.ReactiveIngredient;
import manj.springframework.domain.reactive.ReactiveRecipe;

@Component
public class IngredientCommandToRetvIngredient implements Converter<IngredientCommand, ReactiveIngredient> {

	private final UnitOfMeasureCommandToUnitOfMeasure uomConverter;

	public IngredientCommandToRetvIngredient(UnitOfMeasureCommandToUnitOfMeasure uomConverter) {
		this.uomConverter = uomConverter;
	}

	@Nullable
	@Override
	public ReactiveIngredient convert(IngredientCommand source) {
		if (source == null) {
			return null;
		}

		final ReactiveIngredient ingredient = new ReactiveIngredient();
		ingredient.setId(source.getId());

		if (source.getRecipeId() != null) {
			ReactiveRecipe recipe = new ReactiveRecipe();
			recipe.setId(source.getRecipeId());
			recipe.addIngredient(ingredient);
		}

		ingredient.setAmount(source.getAmount());
		ingredient.setDescription(source.getDescription());
		ingredient.setUom(uomConverter.convert(source.getUom()));
		return ingredient;
	}
}
