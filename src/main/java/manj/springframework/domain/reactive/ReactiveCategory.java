package manj.springframework.domain.reactive;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@Document
public class ReactiveCategory {
	@Id
	private String id;
	private String description;
	private Set<ReactiveRecipe> recipes;
}
