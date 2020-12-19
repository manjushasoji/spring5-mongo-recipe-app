package manj.springframework.domain.reactive;

import lombok.Getter;
import lombok.Setter;
import manj.springframework.domain.UnitOfMeasure;
import org.springframework.data.annotation.Id;
import java.math.BigDecimal;

@Getter
@Setter
public class ReactiveIngredient {

	@Id
	private String id;
	private String description;
	private BigDecimal amount;

	private UnitOfMeasure uom;

	public ReactiveIngredient() {
	}

	public ReactiveIngredient(String description, BigDecimal amount, UnitOfMeasure uom) {
		this.description = description;
		this.amount = amount;
		this.uom = uom;
	}

	public ReactiveIngredient(String description, BigDecimal amount, UnitOfMeasure uom, ReactiveRecipe recipe) {
		this.description = description;
		this.amount = amount;
		this.uom = uom;
		// this.recipe = recipe;
	}

}
