package manj.springframework.converters.reactive;

import lombok.Synchronized;
import manj.springframework.commands.CategoryCommand;
import manj.springframework.domain.reactive.ReactiveCategory;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


@Component
public class RetvCategoryToCategoryCommand implements Converter<ReactiveCategory, CategoryCommand> {

    @Synchronized
    @Nullable
    @Override
    public CategoryCommand convert(ReactiveCategory source) {
        if (source == null) {
            return null;
        }

        final CategoryCommand categoryCommand = new CategoryCommand();

        categoryCommand.setId(source.getId());
        categoryCommand.setDescription(source.getDescription());

        return categoryCommand;
    }
}
