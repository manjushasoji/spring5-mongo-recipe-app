package manj.springframework.domain.reactive;

import lombok.Getter;
import lombok.Setter;
import manj.springframework.domain.Difficulty;
import manj.springframework.domain.Notes;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document
public class ReactiveRecipe {

	@Id
	private String id;
	private String description;
	private Integer prepTime;
	private Integer cookTime;
	private Integer servings;
	private String source;
	private String url;
	private String directions;
	private Set<ReactiveIngredient> ingredients = new HashSet<>();
	private Byte[] image;
	private Difficulty difficulty;
	private Notes notes;
	private Set<ReactiveCategory> categories = new HashSet<>();

	public void setNotes(Notes notes) {
		if (notes != null) {
			this.notes = notes;
		}
	}

	public ReactiveRecipe addIngredient(ReactiveIngredient ingredient) {
		this.ingredients.add(ingredient);
		return this;
	}
}
