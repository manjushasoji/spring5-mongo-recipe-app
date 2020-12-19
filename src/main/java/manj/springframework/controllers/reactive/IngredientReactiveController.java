package manj.springframework.controllers.reactive;

import lombok.extern.slf4j.Slf4j;
import manj.springframework.commands.IngredientCommand;
import manj.springframework.commands.RecipeCommand;
import manj.springframework.commands.UnitOfMeasureCommand;
import manj.springframework.services.reactive.IngredientReactiveService;
import manj.springframework.services.reactive.RecipeReactiveService;
import manj.springframework.services.reactive.UnitOfMeasureReactiveService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class IngredientReactiveController {

	private final IngredientReactiveService ingredientReactiveService;
	private final RecipeReactiveService recipeReactiveService;
	private final UnitOfMeasureReactiveService unitOfMeasureReactiveService;

	public IngredientReactiveController(IngredientReactiveService ingredientReactiveService, RecipeReactiveService recipeReactiveService,
			UnitOfMeasureReactiveService unitOfMeasureReactiveService) {
		this.ingredientReactiveService = ingredientReactiveService;
		this.recipeReactiveService = recipeReactiveService;
		this.unitOfMeasureReactiveService = unitOfMeasureReactiveService;
	}

	@GetMapping("/recipe/{recipeId}/ingredients")
	public String listIngredients(@PathVariable String recipeId, Model model) {
		log.debug("Getting ingredient list for recipe id: " + recipeId);

		// use command object to avoid lazy load errors in Thymeleaf.
		model.addAttribute("recipe", recipeReactiveService.findCommandById(recipeId));

		return "recipe/ingredient/list";
	}

	@GetMapping("recipe/{recipeId}/ingredient/{id}/show")
	public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
		model.addAttribute("ingredient", ingredientReactiveService.findByRecipeIdAndIngredientId(recipeId, id));
		return "recipe/ingredient/show";
	}

	@GetMapping("recipe/{recipeId}/ingredient/new")
	public String newRecipe(@PathVariable String recipeId, Model model) {

		// make sure we have a good id value
		RecipeCommand recipeCommand = recipeReactiveService.findCommandById(recipeId).block();
		// todo raise exception if null

		// need to return back parent id for hidden form property
		IngredientCommand ingredientCommand = new IngredientCommand();
		model.addAttribute("ingredient", ingredientCommand);

		// init uom
		ingredientCommand.setUom(new UnitOfMeasureCommand());

		model.addAttribute("uomList", unitOfMeasureReactiveService.listAllUoms().collectList().block());

		return "recipe/ingredient/ingredientform";
	}

	@GetMapping("recipe/{recipeId}/ingredient/{id}/update")
	public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
		model.addAttribute("ingredient", ingredientReactiveService.findByRecipeIdAndIngredientId(recipeId, id));

		model.addAttribute("uomList", unitOfMeasureReactiveService.listAllUoms().collectList().block());
		return "recipe/ingredient/ingredientform";
	}

	@PostMapping("recipe/{recipeId}/ingredient")
	public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
		IngredientCommand savedCommand = ingredientReactiveService.saveIngredientCommand(command).block();

		log.debug("saved ingredient id:" + savedCommand.getId());

		return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
	}

	@GetMapping("recipe/{recipeId}/ingredient/{id}/delete")
	public String deleteIngredient(@PathVariable String recipeId, @PathVariable String id) {

		log.debug("deleting ingredient id:" + id);
		ingredientReactiveService.deleteById(recipeId, id);

		return "redirect:/recipe/" + recipeId + "/ingredients";
	}
}
