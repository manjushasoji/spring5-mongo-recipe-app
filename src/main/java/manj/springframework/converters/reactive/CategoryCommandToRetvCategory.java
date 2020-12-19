package manj.springframework.converters.reactive;

import lombok.Synchronized;
import manj.springframework.commands.CategoryCommand;
import manj.springframework.domain.reactive.ReactiveCategory;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryCommandToRetvCategory implements Converter<CategoryCommand, ReactiveCategory> {

	@Synchronized
	@Nullable
	@Override
	public ReactiveCategory convert(CategoryCommand source) {
		if (source == null) {
			return null;
		}

		final ReactiveCategory category = new ReactiveCategory();
		category.setId(source.getId());
		category.setDescription(source.getDescription());
		return category;
	}
}
