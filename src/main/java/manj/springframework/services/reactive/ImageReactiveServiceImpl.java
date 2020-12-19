package manj.springframework.services.reactive;

import lombok.extern.slf4j.Slf4j;
import manj.springframework.domain.reactive.ReactiveRecipe;
import manj.springframework.repositories.reactive.RecipeReactiveRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class ImageReactiveServiceImpl implements ImageReactiveService {

	private final RecipeReactiveRepository recipeReactiveRepository;

	public ImageReactiveServiceImpl(RecipeReactiveRepository recipeService) {

		this.recipeReactiveRepository = recipeService;
	}

	@Override
	@Transactional
	public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {

		Mono<ReactiveRecipe> recipeMono = recipeReactiveRepository.findById(recipeId).map(recipe -> {

			try {

				Byte[] byteObjects = new Byte[file.getBytes().length];
				int i = 0;
				for (byte b : file.getBytes()) {
					byteObjects[i++] = b;
				}
				recipe.setImage(byteObjects);
				return recipe;

			} catch (IOException e) {
				log.error("Error occurred", e);
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});
		recipeReactiveRepository.save(recipeMono.block()).block();
		return Mono.empty();
	}
}
