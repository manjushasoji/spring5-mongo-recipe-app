package manj.springframework.services.reactive;

import manj.springframework.commands.UnitOfMeasureCommand;
import reactor.core.publisher.Flux;

public interface UnitOfMeasureReactiveService {

	Flux<UnitOfMeasureCommand> listAllUoms();
}
